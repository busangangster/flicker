// src/pages/ServiceDetailPage.tsx
import React from "react";
import Navbar from "../../components/common/Navbar";
import recommend from "/assets/service/recommend.png";
import favorite from "/assets/service/favorite2.png";
import photocard from "/assets/service/photocard.png";
import click from "/assets/service/click.png";
import star from "/assets/service/star.png";
import ServiceContent from "../../components/SeviceContent";

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
        <div>
          <h1 className="text-[11rem] font-bold leading-tight text-center p-96 italic">
            Flicker
          </h1>
        </div>
        <div className="flex justify-between px-48 py-24">
          <div>
            <p className="text-[5rem] font-bold leading-tight">Description</p>
          </div>

          <div className="text-right">
            <p className="text-[28px] ml-40 leading-relaxed mt-52 text-left">
              취향에 맞춘 영화 추천과 감상 기록을 한 곳에서 <br /> 관리할 수
              있는 맞춤형 영화 큐레이션 서비스입니다. <br /> 당신만의 영화
              라이브러리를 완성하고, <br />
              언제든지 원하는 영화를 찾을 수 있는 <br />
              최적의 플랫폼을 경험하세요.
              <br />
            </p>
          </div>
        </div>

        {/* ServiceContent 컴포넌트로 반복되는 부분 대체 */}
        <ServiceContent
          title="추천"
          description="오롯이 나의 취향을 반영한\n나를 위한 영화 추천"
          tags={["검색", "클릭", "찜한 영화", "소장"]}
          imageUrl={recommend}
        />

        <ServiceContent
          title="추천"
          description="별점과 리뷰\n분석 기반 추천"
          tags={["별점", "리뷰"]}
          imageUrl={star}
        />

        <ServiceContent
          title="추천"
          description="검색 및 클릭 데이터\n분석 기반 추천"
          tags={["검색", "클릭"]}
          imageUrl={click}
        />

        <ServiceContent
          title="추천"
          description="내가 찜한 영화 기반 추천"
          tags={["찜한 영화"]}
          imageUrl={favorite}
        />

        <ServiceContent
          title="기록"
          description="관람 영화와 감상 리뷰가 담긴\n포토카드"
          tags={["소장"]}
          imageUrl={photocard}
        />
      </div>
    </div>
  );
};

export default ServiceDetailPage;
