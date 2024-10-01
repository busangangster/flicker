import React, { useRef, useEffect, useState } from "react";
import HTMLFlipBook from "react-pageflip";
import "../../css/photobook.css";
import { useUserQuery } from "../../hooks/useUserQuery";
import { getPhotocard } from "../../apis/photocardApi"; // 포토카드 API 호출 추가
import PhotoCardFront from "../../components/PhotoCardFront";
import { IFlipBook, PhotocardResponse } from "../../type";

const PhotoCardPage: React.FC = () => {
  const book = useRef<IFlipBook | null>(null);
  const [photocardData, setPhotocardData] = useState<PhotocardResponse[]>([]); // 포토카드 데이터를 상태로 관리
  const {
    data: userData,
    error: userError,
    isLoading: isUserLoading,
  } = useUserQuery(); // 유저 정보

  useEffect(() => {
    if (book.current) {
      setTimeout(() => {
        book.current?.pageFlip().flipNext();
      }, 10);
    }
  }, []);

  // 유저 정보가 로딩 중이거나 없을 때 처리
  if (isUserLoading) return <p>로딩 중...</p>;
  if (userError || !userData)
    return <p>유저 정보를 불러오는데 실패했습니다.</p>;

  const userSeq = userData.userSeq;

  // 포토카드 데이터를 가져오는 함수
  useEffect(() => {
    const fetchPhotocardData = async () => {
      try {
        const response = await getPhotocard(userSeq); // userSeq로 포토카드 데이터 가져오기
        setPhotocardData(response.data);
      } catch (error) {
        console.error("포토카드 데이터를 불러오는데 실패했습니다.", error);
      }
    };
    fetchPhotocardData();
  }, [userSeq]);

  // 포토카드 데이터를 기반으로 페이지 생성
  const createPages = (photocardData: PhotocardData[]) => {
    return photocardData.map((photocard, index) => {
      const { moviePosterUrl } = photocard.movieImageDto;

      return {
        id: index + 2, // 첫 번째 페이지가 고정이므로 2부터 시작
        content: (
          <PhotoCardFront
            images={[{ src: moviePosterUrl, alt: `Movie ${index + 1}` }]}
            pageIndex={index + 2}
          />
        ),
        className: index % 2 === 0 ? "left-page" : "right-page", // 짝수 페이지는 왼쪽, 홀수 페이지는 오른쪽
      };
    });
  };

  const pages = [
    {
      id: 1,
      content: (
        <div className="flex flex-col items-center justify-center w-full h-full bg-[#FFFDF8]">
          <h1 className="text-5xl font-bold italic text-black mb-3">
            MOVIE MEMORIES
          </h1>
          <hr className="border-t-2 border-neutral-500 my-1 w-4/5" />
          <hr className="border-t-2 border-neutral-500 my-1 w-4/5" />
          <p className="text-lg italic mt-2 text-gray-700 self-end mr-16 mb-10">
            made by {userData.userId}
          </p>
          <img
            src="https://via.placeholder.com/500x300"
            alt="Cover Image"
            className="mt-6"
          />
        </div>
      ),
    },
    ...createPages(photocardData), // 포토카드 데이터를 기반으로 페이지 생성
  ];

  return (
    <div className="bg-black rounded-lg w-[1200px] relative">
      <div className="flex w-full h-full justify-center items-center">
        <HTMLFlipBook
          width={1000}
          height={1380}
          size="stretch"
          minWidth={300}
          maxWidth={1000}
          minHeight={400}
          maxHeight={1380}
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
          style={{}}
          maxShadowOpacity={0.5}
          clickEventForward={true}
          swipeDistance={30}
          showPageCorners={true}
          disableFlipByClick={false}
        >
          {pages.map((page) => (
            <div
              key={page.id}
              className={`page ${"className" in page ? page.className : ""}`}
            >
              {page.content}
            </div>
          ))}
        </HTMLFlipBook>
      </div>
    </div>
  );
};

export default PhotoCardPage;
