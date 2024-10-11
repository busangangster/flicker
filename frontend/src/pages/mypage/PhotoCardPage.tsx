import React, { useRef, useState } from "react";
import HTMLFlipBook from "react-pageflip";
import "../../css/PhotoBook.css";
import Navbar from "../../components/common/Navbar";
import { useUserQuery } from "../../hooks/useUserQuery";
import { getPhotocard } from "../../apis/photocardApi";
import PhotoCardFront from "../../components/PhotoCardFront";
import PhotoCardDetailPage from "./PhotoCardDetailPage"; 
import { IFlipBook, Page, PhotocardDataItem } from "../../type";
import { useQuery } from "@tanstack/react-query";

const PhotoCardPage: React.FC = () => {
  const book = useRef<IFlipBook | null>(null);
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
  } | null>(null); 
  const [isModalOpen, setIsModalOpen] = useState(false); 

  const {
    data: userData,
    error: userError,
    isLoading: isUserLoading,
  } = useUserQuery();

  const userSeq = userData?.userSeq; 

  const {
    data: photocardResponse,
    error: photocardError,
    isLoading: isPhotocardLoading,
  } = useQuery({
    queryKey: ["photocardData", userSeq], 
    queryFn: () => getPhotocard(userSeq!), 
    enabled: !!userSeq, 
    retry: 1, 
    refetchOnWindowFocus: false, 
  });

  const handleBookInit = () => {
    if (book.current) {
      requestAnimationFrame(() => {
        setTimeout(() => {
          book.current?.pageFlip().flipNext();
        }, 1000);
      });
    }
  };

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
    setIsModalOpen(true); 
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedCard(null);
  };

  if (isUserLoading) return <p>로딩 중...</p>;
  if (userError) return <p>유저 정보를 불러오는데 실패했습니다.</p>;
  if (!userData) return <p>유저 정보가 없습니다.</p>;

  const createPages = (photocardData: PhotocardDataItem[]) => {
    const imagesPerPage = 4;
    const pages = [];

    for (let i = 0; i < photocardData.length; i += imagesPerPage) {
      const imageSlice = photocardData
        .slice(i, i + imagesPerPage)
        .map((photocard) => ({
          src: photocard.movieImageDto.moviePosterUrl, 
          alt: photocard.movieImageDto.movieTitle, 
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
            onCardClick={(card) => handleCardClick(card)} 
          />
        ),
        className: (i / imagesPerPage) % 2 === 0 ? "left-page" : "right-page",
      });
    }
    if (pages.length % 2 !== 0) {
      pages.push({
        id: pages.length + 1,
        content: (
          <div className="flex flex-col items-center justify-center h-full p-4 bg-[#FFFDF8] rounded-sm">
            <h1 className="text-5xl font-bold italic text-gray-400">
               
            </h1>
          </div>
        ),
        className: "right-page", 
      });
    }

    return pages;
  };


  const photocardData = photocardResponse?.data || [];

  if (isPhotocardLoading) return <p>포토카드 데이터를 불러오는 중입니다...</p>;
  if (photocardError) return <p>포토카드 데이터를 불러오는 데 실패했습니다.</p>;

const pages: Page[] = [
  {
    id: 1,
    content: (
      <div className="flex flex-col items-center justify-center h-full p-4 bg-[#FFFDF8] rounded-sm  overflow-hidden">
        <h1 className="text-5xl font-bold italic text-black mb-3">
          MOVIE MEMORIES
        </h1>
        <hr className="border-t-2 border-neutral-500 my-1 w-4/5" />
        <hr className="border-t-2 border-neutral-500 my-1 w-4/5" />
        <p className="text-lg italic mt-2 text-gray-700 self-end mr-16 mb-10">
          made by {userData?.nickname}
        </p>
        <img
          src="/assets/background/photobookmain.png"
          alt="Cover Image"
          className="mt-6 w-[600px] h-[400px]"
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
          width={700}
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
          disableFlipByClick={true}
          onInit={handleBookInit}
        >
          {pages.map((page) => (
            <div key={page.id} className={`page ${page.className || ""}`}>
              {page.content}
            </div>
          ))}
        </HTMLFlipBook>
      </div>

      {isModalOpen && selectedCard && (
        <div
          className="fixed inset-0 z-50 flex items-center justify-center"
          onClick={handleCloseModal} 
        >
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
            onClick={(e) => e.stopPropagation()} 
          >
            <PhotoCardDetailPage
              card={selectedCard}
              handleCloseModal={handleCloseModal} 
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default PhotoCardPage;
