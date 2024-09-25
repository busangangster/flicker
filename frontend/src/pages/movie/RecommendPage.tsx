// RecommendPage.tsx
import React from "react";
import { useNavigate } from "react-router-dom"; // useNavigate 훅 가져오기
import Navbar from "../../components/common/Navbar";
import theaterDoorImage from "../../assets/movie/theater_door.jpg"; // 이미지 경로

const RecommendPage: React.FC = () => {
  const navigate = useNavigate();

  // 왼쪽 영역 클릭 시 이동할 경로
  const gotoLeftRecommend = () => {
    navigate("/recommendlist");
  };

  // 오른쪽 영역 클릭 시 이동할 경로
  const gotoRightRecommend = () => {
    navigate("/recommendlist");
  };

  return (
    <div className="flex flex-col h-screen bg-black">
      <header className="sticky top-0 bg-transparent z-10">
        <Navbar />
      </header>

      {/* 배경 이미지 설정 */}
      <div
        className="relative flex items-center justify-center w-full h-full"
        style={{
          backgroundImage: `url(${theaterDoorImage})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
        }}
      >
        {/* 왼쪽 반 클릭 시 이동 */}
        <div
          className="absolute left-0 top-0 h-full w-1/2 cursor-pointer"
          onClick={gotoLeftRecommend}
        />

        {/* 오른쪽 반 클릭 시 이동 */}
        <div
          className="absolute right-0 top-0 h-full w-1/2 cursor-pointer"
          onClick={gotoRightRecommend}
        />
      </div>
    </div>
  );
};

export default RecommendPage;
