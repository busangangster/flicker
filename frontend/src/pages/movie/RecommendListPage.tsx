// RecommandListPage.tsx
import React from "react";
import "../../css/RecommendList.css";
import { SwiperSlide, Swiper } from "swiper/react";
import { Navigation, Pagination } from "swiper/modules";

// 목업 포스터 이미지 임의 설정
const movieImg = [
  "/assets/survey/image1.jpg",
  "/assets/survey/image2.jpg",
  "/assets/survey/image3.jpg",
  "/assets/survey/image4.jpg",
  "/assets/survey/image5.jpg",
  "/assets/survey/image6.jpg",
  "/assets/survey/image7.jpg",
  "/assets/survey/image8.jpg",
  "/assets/survey/image9.jpg",
  "/assets/survey/image10.jpg",
  "/assets/survey/image11.jpg",
  "/assets/survey/image12.jpg",
  "/assets/survey/image13.jpg",
];

const RecommandListPage: React.FC = () => (
  <div
    className="relative bg-black text-white min-h-screen flex items-center justify-center"
    style={{
      backgroundImage: `url(src/assets/movie/theater3.jpg)`,
      backgroundSize: "cover",
      backgroundPosition: "center",
    }}
  >
    {/* 영화관 스타일 화면 */}
    <div className="relative w-[70%] h-[60vh] rounded-md overflow-hidden">
      <h2 className="text-center text-2xl font-bold text-black mb-[80px] mt-[70px]">
        My own movie theater
      </h2>
      <div className="swiper-button-prev-custom1">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          className="h-10 w-10"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          strokeWidth={4}
        >
          <path
            strokeLinecap="round"
            strokeLinejoin="round"
            d="M15 19l-7-7 7-7"
          />
        </svg>
      </div>
      <div className="swiper-button-next-custom1">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          className="h-10 w-10"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
          strokeWidth={4}
        >
          <path strokeLinecap="round" strokeLinejoin="round" d="M9 5l7 7-7 7" />
        </svg>
      </div>
      <Swiper
        slidesPerView={6}
        spaceBetween={10}
        slidesOffsetBefore={50}
        navigation={{
          nextEl: ".swiper-button-next-custom1",
          prevEl: ".swiper-button-prev-custom1",
        }}
        modules={[Navigation, Pagination]}
      >
        {movieImg.map((img, index) => (
          <SwiperSlide key={index} className="flex justify-center items-center">
            <img
              src={img}
              alt={`Movie ${index + 1}`}
              className="rounded-lg shadow-md object-cover w-full h-[306px]"
            />
          </SwiperSlide>
        ))}
      </Swiper>
    </div>
  </div>
);

export default RecommandListPage;
