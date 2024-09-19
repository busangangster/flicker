// PhotoBookPage.tsx
import React from "react";
import FlipPage from "react-flip-page";
import { useNavigate } from "react-router-dom";

const PhotoBookPage: React.FC = () => {
  const navigate = useNavigate();

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
          <img
            src="https://via.placeholder.com/200x300?text=Movie+1"
            alt="Movie 1"
            className="border rounded-md"
          />
          <img
            src="https://via.placeholder.com/200x300?text=Movie+2"
            alt="Movie 2"
            className="border rounded-md"
          />
          <img
            src="https://via.placeholder.com/200x300?text=Movie+3"
            alt="Movie 3"
            className="border rounded-md"
          />
          <img
            src="https://via.placeholder.com/200x300?text=Movie+4"
            alt="Movie 4"
            className="border rounded-md"
          />
          <img
            src="https://via.placeholder.com/200x300?text=Movie+5"
            alt="Movie 5"
            className="border rounded-md"
          />
          <img
            src="https://via.placeholder.com/200x300?text=Movie+6"
            alt="Movie 6"
            className="border rounded-md"
          />
          <img
            src="https://via.placeholder.com/200x300?text=Movie+7"
            alt="Movie 7"
            className="border rounded-md"
          />
          <img
            src="https://via.placeholder.com/200x300?text=Movie+8"
            alt="Movie 8"
            className="border rounded-md"
          />
        </div>
      ),
    },
  ];

  return (
    <div className="flex flex-col items-center bg-black min-h-screen text-white w-screen">
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
