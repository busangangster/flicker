// RecommandListPage.tsx
import React, { useRef, useEffect, useState } from "react";
import { SwiperSlide, Swiper } from "swiper/react";
import { Navigation, Pagination } from "swiper/modules";
import { Swiper as SwiperInstance, NavigationOptions } from "swiper/types";

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

const RecommandListPage: React.FC = () => {
  const prevRef = useRef<HTMLDivElement | null>(null);
  const nextRef = useRef<HTMLDivElement | null>(null);
  const [swiperInstance, setSwiperInstance] = useState<SwiperInstance | null>(
    null
  );
  const [isBeginning, setIsBeginning] = useState(true);
  const [isEnd, setIsEnd] = useState(false);

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

  return (
    <div
      className="relative bg-black text-white min-h-screen flex items-center justify-center"
      style={{
        backgroundImage: `url(src/assets/movie/theater3.jpg)`,
        backgroundSize: "cover",
        backgroundPosition: "center",
      }}
    >
      {/* 영화관 스타일 화면 */}
      <div className="relative w-[75%] h-[60vh] rounded-md overflow-hidden">
        <h2 className="text-center text-2xl font-bold text-black mb-[80px] mt-[70px]">
          My own movie theater
        </h2>
        <Swiper
          slidesPerView={6}
          spaceBetween={10}
          onSwiper={handleSwiper}
          navigation={{
            nextEl: nextRef.current,
            prevEl: prevRef.current,
          }}
          modules={[Navigation, Pagination]}
        >
          {movieImg.map((img, index) => (
            <SwiperSlide
              key={index}
              className="flex justify-center items-center"
            >
              <img
                src={img}
                alt={`Movie ${index + 1}`}
                className="rounded-lg shadow-md object-cover w-full h-[306px]"
              />
            </SwiperSlide>
          ))}
        </Swiper>
      </div>

      {/* 외부 화살표 버튼 - 슬라이드 컨테이너 밖에 배치 */}
      {!isBeginning && (
        <div
          ref={prevRef}
          className="swiper-button-prev-custom1 absolute left-[180px] top-[55%] transform translate-y-[-50%] cursor-pointer"
          onClick={handlePrevClick}
        >
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
      )}
      {!isEnd && (
        <div
          ref={nextRef}
          className="swiper-button-next-custom1 absolute right-[180px] top-[55%] transform translate-y-[-50%] cursor-pointer"
          onClick={handleNextClick}
        >
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
              d="M9 5l7 7-7 7"
            />
          </svg>
        </div>
      )}

      <style>
        {`
          .swiper-button-prev-custom1, .swiper-button-next-custom1 {
            cursor: pointer;
            color: black;
          }
        `}
      </style>
    </div>
  );
};

export default RecommandListPage;
