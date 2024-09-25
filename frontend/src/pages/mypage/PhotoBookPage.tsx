import React from "react";
import { useNavigate } from "react-router-dom";
import photobook from "../../assets/photobook/photobook.png";
import "../../css/photobook.css";

const PhotoBookPage: React.FC = () => {
  const navigate = useNavigate();

  const goToPhotoCard = () => {
    // 사이드바를 먼저 점차적으로 사라지게 함
    const sidebarElement = document.querySelector(".SideBar") as HTMLElement;
    if (sidebarElement) {
      sidebarElement.style.transition = "opacity 0.3s ease";
      sidebarElement.style.opacity = "0";
    }

    // 이미지와 텍스트 모두에 애니메이션 적용
    const photobookElement = document.querySelector(
      ".photobook-img"
    ) as HTMLElement;
    const textElements = document.querySelectorAll(
      ".photobook-text"
    ) as NodeListOf<HTMLElement>; // 모든 텍스트 요소 선택
    const coverImageElement = document.querySelector(
      ".cover-image"
    ) as HTMLElement;

    if (photobookElement) {
      photobookElement.classList.add("book-animation-out");
    }
    // 모든 .photobook-text 요소에 애니메이션 추가
    textElements.forEach((textElement) => {
      textElement.classList.add("text-animation-out");
    });
    if (coverImageElement) {
      coverImageElement.classList.add("cover-animation-out");
    }

    setTimeout(() => {
      navigate("/mypage/photocard");
    }, 400);
  };

  return (
    <div className="bg-black p-8 rounded-lg w-[1200px] relative">
      {/* 클릭 가능한 전체 영역 */}
      <div className="relative cursor-pointer" onClick={goToPhotoCard}>
        {/* 이미지 */}
        <img src={photobook} alt="photobook" className="photobook-img w-full" />
        {/* 이미지 위의 텍스트 및 추가 요소 */}
        <div className="absolute -top-10 left-10 w-full h-full flex flex-col items-center justify-center">
          <h1 className="photobook-text text-5xl font-bold italic text-black mb-3">
            MOVIE MEMORIES
          </h1>
          <hr className="photobook-text border-t-2 border-neutral-500 my-1 w-3/5" />
          <hr className="photobook-text border-t-2 border-neutral-500 my-1 w-3/5" />
          <p className="photobook-text text-lg italic mt-2 text-black self-end mr-60">
            made by nickname
          </p>
          <img
            src="https://via.placeholder.com/600x400"
            alt="Cover Image"
            className="cover-image mt-6"
          />
        </div>
      </div>
    </div>
  );
};

export default PhotoBookPage;
