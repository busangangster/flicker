import React from "react";
import Navbar from "../../components/common/Navbar"; // NavBar 컴포넌트 경로에 맞춰주세요

const ServiceDetailPage: React.FC = () => {
  return (
    <div className="flex flex-col h-screen bg-black text-white pb-10 overflow-y-auto">
      {/* 상단 네비게이션 바 */}
      <header className="sticky top-0 bg-transparent z-10">
        <Navbar />
      </header>

      {/* 페이지 내용 */}
      <div className="flex flex-col justify-between mt-[120px]">
        {/* 상단 내용 */}
        <div className="flex justify-between px-48 py-24">
          <div>
            <p className="text-[#4D7FFF] text-xl font-semibold mb-2">
              our service
            </p>
            <h1 className="text-[9rem] font-bold leading-tight">Flicker</h1>
          </div>

          <div className="text-right">
            <p className="text-[40px] font-bold leading-snug mt-96">
              서비스 설명을 한 2줄 작성 예정
              <br />
              서비스를 간략하게 정리(영화 추천)
            </p>
          </div>
        </div>

        {/* 추가 스크롤 가능한 내용 */}
        <div className="flex justify-between px-48 py-20 mt-20 mb-16">
          <div>
            <h2 className="text-4xl ml-2">추천</h2>
            <p className="text-5xl mt-5 font-semibold leading-snug">
              오롯이 나의 취향을 반영한 <br /> 나를 위한 영화 추천
            </p>
            <div className="flex space-x-6 mt-5 ml-1">
              <span className="text-xl text-gray-400">별점</span>
              <span className="text-xl text-gray-400">리뷰</span>
              <span className="text-xl text-gray-400">행동</span>
            </div>
          </div>

          {/* 서비스 이미지 자리 */}
          <img
            src="https://via.placeholder.com/500x600?text=service+image"
            alt="Service Placeholder"
            className="w-[500px] h-[600px] object-cover"
          />
        </div>
      </div>
    </div>
  );
};

export default ServiceDetailPage;
