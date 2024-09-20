import React, { useState, useRef, useEffect } from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation, Pagination } from "swiper/modules";
import { Swiper as SwiperInstance, NavigationOptions } from "swiper/types";
import { useNavigate } from "react-router-dom";

// MoviesList 컴포넌트에 필요한 props 타입 정의
interface MoviesListProps {
  category: string; // 카테고리 제목
  movieImg: string[]; // 영화 이미지 배열
}

const MoviesList: React.FC<MoviesListProps> = ({ category, movieImg }) => {
  const prevRef = useRef<HTMLDivElement | null>(null);
  const nextRef = useRef<HTMLDivElement | null>(null);
  const [swiperInstance, setSwiperInstance] = useState<SwiperInstance | null>(
    null
  );
  const [isBeginning, setIsBeginning] = useState(true);
  const [isEnd, setIsEnd] = useState(false);
  const navigate = useNavigate();

  // Swiper 인스턴스가 생성되면 navigation 설정 및 이벤트 리스너 등록
  useEffect(() => {
    if (swiperInstance && prevRef.current && nextRef.current) {
      swiperInstance.params.navigation = {
        ...(swiperInstance.params.navigation as NavigationOptions),
        prevEl: prevRef.current,
        nextEl: nextRef.current,
      };

      swiperInstance.navigation.destroy();
      swiperInstance.navigation.init();
      swiperInstance.navigation.update();

      // 현재 슬라이드 상태에 따라 화살표 버튼 상태 설정
      swiperInstance.on("slideChange", () => {
        setIsBeginning(swiperInstance.isBeginning);
        setIsEnd(swiperInstance.isEnd);
      });

      // 초기 상태 설정
      setIsBeginning(swiperInstance.isBeginning);
      setIsEnd(swiperInstance.isEnd);
    }
  }, [swiperInstance]);

  // Swiper 인스턴스를 설정하고 초기 상태를 업데이트
  const handleSwiper = (swiper: SwiperInstance) => {
    setSwiperInstance(swiper);

    swiper.on("slideChange", () => {
      setIsBeginning(swiper.isBeginning);
      setIsEnd(swiper.isEnd);
    });

    // 초기 상태 설정
    setIsBeginning(swiper.isBeginning);
    setIsEnd(swiper.isEnd);
  };

  // 이전 버튼 클릭 시
  const handlePrevClick = () => {
    if (swiperInstance && !isBeginning) {
      swiperInstance.slidePrev();
    }
  };

  // 다음 버튼 클릭 시
  const handleNextClick = () => {
    if (swiperInstance && !isEnd) {
      swiperInstance.slideNext();
    }
  };

  const goToDetail = () => {
    navigate("/moviedetail");
  };

  return (
    <div className="relative h-[300px] w-[1800px] flex-shrink-0 mb-[100px]">
      <h3 className="text-white mb-[20px] text-[27px] ml-[50px]">{category}</h3>

      {/* 커스텀 네비게이션 버튼 */}
      {!isBeginning && (
        <div
          ref={prevRef}
          className="swiper-button-prev-custom"
          onClick={handlePrevClick}
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-10 w-10 text-white"
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
      )}

      {!isEnd && (
        <div
          ref={nextRef}
          className="swiper-button-next-custom"
          onClick={handleNextClick}
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-10 w-10 text-white"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            strokeWidth={4}
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              d="M9 5l7 7-7 7"
            />
          </svg>
        </div>
      )}

      <Swiper
        slidesPerView={8}
        spaceBetween={20}
        slidesOffsetBefore={50}
        navigation={{
          nextEl: nextRef.current,
          prevEl: prevRef.current,
        }}
        onSwiper={handleSwiper}
        modules={[Navigation, Pagination]}
      >
        {movieImg.map((img, index) => (
          <SwiperSlide key={index} className="flex justify-center items-center">
            <img
              src={img}
              alt={`Movie ${index + 1}`}
              onClick={goToDetail}
              className="rounded-lg shadow-md object-cover w-full h-[306px] cursor-pointer"
            />
          </SwiperSlide>
        ))}
      </Swiper>
    </div>
  );
};

export default MoviesList;
