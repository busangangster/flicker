import React from "react";
import { Navigation, Pagination } from "swiper/modules";
import { Swiper, SwiperSlide } from "swiper/react";

// MoviesList 컴포넌트에 필요한 props 타입 정의
interface MoviesListProps {
  category: string; // 카테고리 제목
  movieImg: string[]; // 영화 이미지 배열
}

const MoviesList: React.FC<MoviesListProps> = ({ category, movieImg }) => {
  return (
    <div className="relative h-[300px] w-[1800px] flex-shrink-0 mb-[100px]">
      <h3 className="text-white mb-[20px] text-[27px] ml-[50px]">{category}</h3>
      {/* 커스텀 네비게이션 버튼 */}
      <div className="swiper-button-prev-custom">
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
      <div className="swiper-button-next-custom">
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
        slidesPerView={8}
        spaceBetween={20}
        slidesOffsetBefore={50}
        navigation={{
          nextEl: ".swiper-button-next-custom",
          prevEl: ".swiper-button-prev-custom",
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
  );
};

export default MoviesList;
