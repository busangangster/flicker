import React, { useRef, useEffect, useState } from "react";
import HTMLFlipBook from "react-pageflip";
import "../../css/PhotoBook.css";
import Navbar from "../../components/common/Navbar";
import { useUserQuery } from "../../hooks/useUserQuery";
import { getPhotocard } from "../../apis/photocardApi";
import PhotoCardFront from "../../components/PhotoCardFront";
import PhotoCardDetailPage from "./PhotoCardDetailPage"; // Detail 페이지 추가
import { IFlipBook, Page, PhotocardDataItem } from "../../type";

const PhotoCardPage: React.FC = () => {
  const book = useRef<IFlipBook | null>(null);
  const [photocardData, setPhotocardData] = useState<PhotocardDataItem[]>([]);
  const [selectedCard, setSelectedCard] = useState<{
    src: string;
    alt: string;
    movieSeq: number;
    movieTitle: string;
    movieYear: number;
    reviewRating: number;
    createdAt: string;
    content: string;
    likes: number;
    backgroundUrl: string;
  } | null>(null); // 선택된 카드 상태
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태

  const {
    data: userData,
    error: userError,
    isLoading: isUserLoading,
  } = useUserQuery();

  const handleBookInit = () => {
    if (book.current) {
      requestAnimationFrame(() => {
        setTimeout(() => {
          book.current?.pageFlip().flipNext();
        }, 1000);
      });
    }
  };

  const userSeq = 181368;

  useEffect(() => {
    const fetchPhotocardData = async () => {
      try {
        const response = await getPhotocard(userSeq);
        setPhotocardData(response.data);
      } catch (error) {
        console.error("포토카드 데이터를 불러오는데 실패했습니다.", error);
      }
    };
    fetchPhotocardData();
  }, [userSeq]);

  // 포토카드 클릭 시 모달 열기
  const handleCardClick = (card: {
    src: string;
    alt: string;
    movieSeq: number;
    movieTitle: string;
    movieYear: number;
    reviewRating: number;
    createdAt: string;
    content: string;
    likes: number;
    backgroundUrl: string;
  }) => {
    setSelectedCard(card);
    setIsModalOpen(true); // 모달 열기
  };

  // 모달 닫기
  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedCard(null);
  };

  if (isUserLoading) return <p>로딩 중...</p>;
  if (userError) return <p>유저 정보를 불러오는데 실패했습니다.</p>;

  const createPages = (photocardData: PhotocardDataItem[]) => {
    const imagesPerPage = 4;
    const pages = [];

    for (let i = 0; i < photocardData.length; i += imagesPerPage) {
      const imageSlice = photocardData
        .slice(i, i + imagesPerPage)
        .map((photocard) => ({
          src: photocard.movieImageDto.moviePosterUrl, // 이미지 URL
          alt: photocard.movieImageDto.movieTitle, // 대체 텍스트 추가
          movieSeq: photocard.reviewDto.movieSeq,
          movieTitle: photocard.movieImageDto.movieTitle,
          movieYear: photocard.movieImageDto.movieYear,
          reviewRating: photocard.reviewDto.reviewRating,
          createdAt: photocard.reviewDto.createdAt,
          content: photocard.reviewDto.content,
          likes: photocard.reviewDto.likes,
          backgroundUrl: photocard.movieImageDto.backgroundUrl,
        }));

      pages.push({
        id: i / imagesPerPage + 2,
        content: (
          <PhotoCardFront
            images={imageSlice}
            pageIndex={i / imagesPerPage + 2}
            onCardClick={(card) => handleCardClick(card)} // 가공된 카드 데이터를 전달
          />
        ),
        className: (i / imagesPerPage) % 2 === 0 ? "left-page" : "right-page",
      });
    }

    return pages;
  };

  const pages: Page[] = [
    {
      id: 1,
      content: (
        <div className="flex flex-col items-center justify-center h-full p-4 bg-[#FFFDF8] rounded-sm">
          <h1 className="text-5xl font-bold italic text-black mb-3">
            MOVIE MEMORIES
          </h1>
          <hr className="border-t-2 border-neutral-500 my-1 w-4/5" />
          <hr className="border-t-2 border-neutral-500 my-1 w-4/5" />
          <p className="text-lg italic mt-2 text-gray-700 self-end mr-16 mb-10">
            made by {userData?.userId}
          </p>
          <img
            src="https://via.placeholder.com/500x300"
            alt="Cover Image"
            className="mt-6"
          />
        </div>
      ),
    },
    ...createPages(photocardData),
  ];

  return (
    <div className="bg-black min-h-screen flex flex-col justify-center items-center">
      <header className="w-full fixed top-0 z-50">
        <Navbar />
      </header>

      <div className="flex w-full justify-center items-center mt-16">
        <HTMLFlipBook
          width={800}
          height={800}
          size="fixed"
          minWidth={300}
          maxWidth={800}
          minHeight={400}
          maxHeight={800}
          showCover={true}
          mobileScrollSupport={true}
          className="book-animation-in"
          ref={book}
          drawShadow={true}
          flippingTime={700}
          useMouseEvents={true}
          startPage={0}
          usePortrait={true}
          startZIndex={0}
          autoSize={true}
          style={{
            transition: "opacity 0.5s ease",
            opacity: photocardData.length ? 1 : 0,
          }}
          maxShadowOpacity={0.5}
          clickEventForward={true}
          swipeDistance={30}
          showPageCorners={true}
          disableFlipByClick={false}
          onInit={handleBookInit}
        >
          {pages.map((page) => (
            <div key={page.id} className={`page ${page.className || ""}`}>
              {page.content}
            </div>
          ))}
        </HTMLFlipBook>
      </div>

      {/* 모달이 열려있을 때 PhotoCardDetailPage 표시 */}
      {isModalOpen && selectedCard && (
        <div
          className="fixed inset-0 z-50 flex items-center justify-center"
          onClick={handleCloseModal} // 배경 클릭 시 모달 닫기
        >
          {/* 배경에 투명도를 적용하는 ::before 가상 요소 */}
          <div
            className="absolute inset-0"
            style={{
              backgroundImage: `url(${selectedCard?.backgroundUrl})`,
              backgroundSize: "cover",
              backgroundPosition: "center",
              opacity: 1,
            }}
          />
          <div
            className="relative"
            onClick={(e) => e.stopPropagation()} // 모달 내부 클릭 시 닫히지 않도록
          >
            <PhotoCardDetailPage
              card={selectedCard}
              handleCloseModal={handleCloseModal} // 모달 닫기 함수 전달
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default PhotoCardPage;
