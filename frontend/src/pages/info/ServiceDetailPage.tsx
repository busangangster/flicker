import React from "react";
import Navbar from "../../components/common/Navbar";
import recommend from "/assets/service/recommend.png";
import photocard from "/assets/service/photocard.png";
import favorite from "/assets/service/favorite2.png";
import serviceImage from "/assets/service/serviceImage.png";
import ServiceContent from "../../components/SeviceContent";
import AboutMain from "./AboutMain";

const ServiceDetailPage: React.FC = () => {
  return (
    <div className="bg-black text-white pb-10 overflow-y-auto">
      {/* 상단 네비게이션 바 */}
      <header className="sticky top-0 bg-transparent z-10">
        <Navbar />
      </header>

      {/* AboutMain 컴포넌트 - 전체 화면을 차지 */}
      <div className="h-screen">
        <AboutMain />
      </div>

      {/* AboutMain 아래의 컨텐츠 스크롤 가능 */}
      <div className="px-12 py-10">
        <div className="text-center py-24">
          <p className="text-[5rem] font-bold">Description</p>
          <p className="text-[35px] leading-relaxed mt-10">
            취향에 맞춘 영화 추천과 감상 기록을 한 곳에서 관리할 수 있는 맞춤형
            영화 큐레이션 서비스입니다. 당신만의 영화 라이브러리를 완성하고,
            언제든지 원하는 영화를 찾을 수 있는 최적의 플랫폼을 경험하세요.
          </p>
        </div>

        {/* ServiceContent 컴포넌트로 반복되는 부분 */}
        <ServiceContent
          title="추천"
          description="오롯이 나의 취향을 반영한\n나를 위한 영화 추천"
          tags={["별점", "리뷰", "검색", "클릭", "찜"]}
          imageUrl={recommend}
        />

        <ServiceContent
          title="추천"
          description="별점과 리뷰\n분석 기반 추천"
          tags={["별점", "리뷰"]}
          imageUrl={serviceImage}
        />

        <ServiceContent
          title="추천"
          description="검색 및 클릭 데이터\n분석 기반 추천"
          tags={["검색", "클릭"]}
          imageUrl={serviceImage}
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
