// PhotoCardPage.tsx
import React, { useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import HTMLFlipBook from "react-pageflip";
import "../../css/PhotoBook.css";

// IFlipBook 인터페이스 정의
interface IFlipBook {
  flipNext: () => void;
  flipPrev: () => void;
  pageFlip: () => { flipNext: () => void; flipPrev: () => void };
}

const PhotoCardPage: React.FC = () => {
  const book = useRef<IFlipBook | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    // 컴포넌트가 마운트되면 첫 페이지를 자동으로 넘겨 두 번째 페이지부터 시작
    if (book.current) {
      setTimeout(() => {
        book.current?.pageFlip().flipNext();
      }, 10); // 페이지 넘김 딜레이 (0.1초 후 넘김)
    }
  }, []);

  const goTophotocarddetail = () => {
    navigate("/mypage/photocarddetail"); // photocarddetail 페이지로 이동
  };
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
            made by nickname
          </p>
          <img
            src="https://via.placeholder.com/500x300"
            alt="Cover Image"
            className="mt-6"
          />
        </div>
      ),
    },
    {
      id: 2,
      content: (
        <div className="grid grid-cols-2 grid-row-2 gap-4 p-4 bg-white h-full w-full page-content left-page">
          {images.slice(0, 4).map((image, index) => (
            <div
              key={index}
              className="bg-white rounded-lg border border-gray-700 pl-3 pr-3 pt-3 pb-10 flex justify-center items-center shadow-lg"
            >
              <img
                src={image.src}
                alt={image.alt}
                className="border rounded-md object-cover w-full h-[320px]"
                onClick={goTophotocarddetail}
              />
            </div>
          ))}
          <p className="text-lg text-gray-700 mt-1 text-right w-full">2</p>
        </div>
      ),
    },
    {
      id: 3,
      content: (
        <div className="grid grid-cols-2 grid-row-2 gap-4 p-4 bg-white h-full w-full page-content right-page">
          {images.slice(4, 8).map((image, index) => (
            <div
              key={index}
              className="bg-white rounded-lg border border-gray-700 pl-3 pr-3 pt-3 pb-10 flex justify-center items-center shadow-lg"
            >
              <img
                src={image.src}
                alt={image.alt}
                className="border rounded-md object-cover w-full h-[320px]"
                onClick={goTophotocarddetail}
              />
            </div>
          ))}
          <p className="text-lg text-gray-700 mt-1 text-right w-full">3</p>
        </div>
      ),
    },
    {
      id: 4,
      content: (
        <div className="grid grid-cols-2 grid-row-2 gap-4 p-4 bg-white h-full page-content left-page">
          {images.slice(0, 4).map((image, index) => (
            <div
              key={index}
              className="bg-white rounded-lg border border-gray-700 pl-3 pr-3 pt-3 pb-10 flex justify-center items-center shadow-lg"
            >
              <img
                src={image.src}
                alt={image.alt}
                className="border rounded-md object-cover w-full h-[320px]"
                onClick={goTophotocarddetail}
              />
            </div>
          ))}
          <p className="text-lg text-gray-700 mt-1 text-right w-full">4</p>
        </div>
      ),
    },
    {
      id: 5,
      content: (
        <div className="grid grid-cols-2 grid-row-2 gap-4 p-4 bg-white h-full page-content right-page">
          {images.slice(2, 6).map((image, index) => (
            <div
              key={index}
              className="bg-white rounded-lg border border-gray-700 pl-3 pr-3 pt-3 pb-10 flex justify-center items-center shadow-lg"
            >
              <img
                src={image.src}
                alt={image.alt}
                className="border rounded-md object-cover w-full h-[320px]"
                onClick={goTophotocarddetail}
              />
            </div>
          ))}
          <p className="text-lg text-gray-700 mt-1 text-right w-full">5</p>
        </div>
      ),
    },
    {
      id: 6,
      content: (
        <div className="grid grid-cols-2 grid-row-2 gap-4 p-4 bg-white h-full page-content left-page">
          {images.slice(1, 5).map((image, index) => (
            <div
              key={index}
              className="bg-white rounded-lg border border-gray-700 pl-3 pr-3 pt-3 pb-10 flex justify-center items-center shadow-lg"
            >
              <img
                src={image.src}
                alt={image.alt}
                className="border rounded-md object-cover w-full h-[320px]"
                onClick={goTophotocarddetail}
              />
            </div>
          ))}
          <p className="text-lg text-gray-700 mt-1 text-right w-full">6</p>
        </div>
      ),
    },
    {
      id: 7,
      content: (
        <div className="grid grid-cols-2 grid-row-2 gap-4 p-4 bg-white h-full page-content right-page">
          {images.slice(4, 8).map((image, index) => (
            <div
              key={index}
              className="bg-white rounded-lg border border-gray-700 pl-3 pr-3 pt-3 pb-10 flex justify-center items-center shadow-lg"
            >
              <img
                src={image.src}
                alt={image.alt}
                className="border rounded-md object-cover w-full h-[320px]"
                onClick={goTophotocarddetail}
              />
            </div>
          ))}
          <p className="text-lg text-gray-700 mt-1 text-right w-full">7</p>
        </div>
      ),
    },
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
          className="shadow-2xl book-animation-in"
          ref={book}
          drawShadow={true}
          flippingTime={700}
          useMouseEvents={true}
          startPage={0}
          usePortrait={true} // 세로 모드 지원
          startZIndex={0} // 기본 z-index 설정
          autoSize={true} // 자동 크기 조정
          style={{}} // 필요시 스타일 추가
          maxShadowOpacity={0.5} // 그림자의 최대 투명도
          clickEventForward={true} // 클릭 이벤트를 페이지 안쪽으로 전달
          swipeDistance={30} // 스와이프 감지 거리
          showPageCorners={true} // 페이지 모서리 표시
          disableFlipByClick={false} // 클릭을 통한 페이지 넘김 비활성화 여부
        >
          {pages.map((page, index) => (
            <div
              key={page.id}
              className={`page ${index % 2 === 0 ? "left-page" : "right-page"}`}
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
