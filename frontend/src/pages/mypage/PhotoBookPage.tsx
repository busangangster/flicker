// PhotoBookPage.tsx
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
      sidebarElement.style.transition = "opacity 0.3s ease"; // 0.3초 동안 서서히 사라짐
      sidebarElement.style.opacity = "0";
    }

    // photobook 이미지도 0.5초 후 점차적으로 사라지게 함
    const photobookElement = document.querySelector(
      ".photobook-img"
    ) as HTMLElement;
    if (photobookElement) {
      photobookElement.classList.add("book-animation-out"); // 확대 애니메이션 추가
    }

  
    setTimeout(() => {
      navigate("/mypage/photocard");
    }, 400); 
  };

  return (
    <div className="bg-black p-8 rounded-lg w-[1200px] relative">
      <img
        src={photobook}
        alt="photobook"
        onClick={goToPhotoCard}
        className="photobook-img" 
      />
    </div>
  );
};

export default PhotoBookPage;
