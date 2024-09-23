// PhotoBookPage.tsx
import React from "react";
import FlipPage from "react-flip-page";

const PhotoBookPage: React.FC = () => {
  const images = [
    { src: "/assets/survey/image1.jpg", alt: "Movie 1" },
    { src: "/assets/survey/image2.jpg", alt: "Movie 2" },
    { src: "/assets/survey/image3.jpg", alt: "Movie 3" },
    { src: "/assets/survey/image10.jpg", alt: "Movie 4" },
    { src: "/assets/survey/image5.jpg", alt: "Movie 5" },
    { src: "/assets/survey/image6.jpg", alt: "Movie 6" },
    { src: "/assets/survey/image7.jpg", alt: "Movie 7" },
    { src: "/assets/survey/image8.jpg", alt: "Movie 8" },
  ];

  // 목업 데이터 설정
  const pages = [
    {
      id: 1,
      content: (
        <div className="flex flex-col items-center justify-center w-full h-full bg-white">
          <h1 className="text-5xl font-bold italic text-black mb-3">
            MOVIE MEMORIES
          </h1>
          <hr className="border-t-2 border-neutral-500 my-1 w-2/3" />
          <hr className="border-t-2 border-neutral-500 my-1 w-2/3" />
          <p className="text-lg mt-2 text-gray-700 self-end mr-40">
            made by nickname
          </p>
          <img
            src="https://via.placeholder.com/600x400"
            alt="Cover Image"
            className="mt-6"
          />
        </div>
      ),
    },
    {
      id: 2,
      content: (
        <div className="grid grid-cols-4 gap-4 p-4">
          {/* 8개의 영화 포스터를 두 페이지에 배치 */}
          {images.map((image, index) => (
            <img
              key={index} // 각 요소에 고유한 key를 부여
              src={image.src}
              alt={image.alt}
              className="border rounded-md object-cover w-full h-[306px]" // 원하는 스타일 적용
            />
          ))}
        </div>
      ),
    },
  ];

  return (
    <div className="bg-black p-8 rounded-lg w-[1200px] relative">
      <div className="fixed top-0 left-0 p-4"></div>

      <div className="flex w-full h-full justify-center items-center mt-8">
        <FlipPage
          orientation="horizontal"
          showSwipeHint
          uncutPages
          className="shadow-2xl !w-[1000px] !h-[700px]" // 높이를 조정한 클래스
        >
          {/* 페이지 내용 */}
          {pages.map((page) => (
            <article key={page.id} className="flip-page bg-white p-8">
              {page.content}
            </article>
          ))}
        </FlipPage>
      </div>
    </div>
  );
};

export default PhotoBookPage;
