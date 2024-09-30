// PhotoCardPage.tsx
import React, { useRef, useEffect } from "react";
import HTMLFlipBook from "react-pageflip";
import "../../css/photobook.css";
import { useUserQuery } from "../../hooks/useUserQuery";
import PhotoCardFront from "../../components/PhotoCardFront";

// IFlipBook 인터페이스 정의
interface IFlipBook {
  flipNext: () => void;
  flipPrev: () => void;
  pageFlip: () => { flipNext: () => void; flipPrev: () => void };
}

const PhotoCardPage: React.FC = () => {
  const book = useRef<IFlipBook | null>(null);

  useEffect(() => {
    if (book.current) {
      setTimeout(() => {
        book.current?.pageFlip().flipNext();
      }, 10);
    }
  }, []);

  const { data, error, isLoading } = useUserQuery();

  if (!data) return <p>유저 정보가 없습니다.</p>;
  if (isLoading) return <p>로딩 중...</p>;
  if (error) return <p>유저 정보를 불러오는데 실패했습니다.</p>;

  const images = [
    { src: "/assets/survey/image3.jpg", alt: "Movie 1" },
    { src: "/assets/survey/image5.jpg", alt: "Movie 2" },
    { src: "/assets/survey/image7.jpg", alt: "Movie 3" },
    { src: "/assets/survey/image10.jpg", alt: "Movie 4" },
    { src: "/assets/survey/image8.jpg", alt: "Movie 5" },
    { src: "/assets/survey/image14.jpg", alt: "Movie 6" },
    { src: "/assets/survey/image17.jpg", alt: "Movie 7" },
    { src: "/assets/survey/image20.jpg", alt: "Movie 8" },
  ];

  const imagesPerPage = 4;
  const totalPages = 7;

  // 페이지 생성 함수 (이미지를 반복해서 페이지에 할당)
  const createPages = (
    images: { src: string; alt: string }[],
    totalPages: number
  ) => {
    const pages = [];
    let imageIndex = 0;

    for (let i = 0; i < totalPages; i++) {
      const imageSlice = [];
      for (let j = 0; j < imagesPerPage; j++) {
        imageSlice.push(images[imageIndex % images.length]);
        imageIndex++;
      }

      pages.push({
        id: i + 2, // 첫 번째 페이지가 고정이므로 2부터 시작
        content: <PhotoCardFront images={imageSlice} pageIndex={i + 2} />,
        className: i % 2 === 0 ? "left-page" : "right-page", // 짝수 페이지는 왼쪽, 홀수 페이지는 오른쪽
      });
    }

    return pages;
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
            made by {data.userId}
          </p>
          <img
            src="https://via.placeholder.com/500x300"
            alt="Cover Image"
            className="mt-6"
          />
        </div>
      ),

    },
    ...createPages(images, totalPages), // 이미지로 페이지 생성 (7페이지까지)
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
