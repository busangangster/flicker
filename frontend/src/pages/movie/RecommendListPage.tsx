import React, { useRef, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { SwiperSlide, Swiper } from "swiper/react";
import { Navigation, Pagination } from "swiper/modules";
import { Swiper as SwiperInstance, NavigationOptions } from "swiper/types";
import exit from "/assets/movie/exit.jpg";

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
  const navigate = useNavigate();

  // 페이지 진입 애니메이션 상태
  const [isLoaded, setIsLoaded] = useState(false);

  useEffect(() => {
    // 페이지 로드 시 애니메이션을 시작
    setTimeout(() => {
      setIsLoaded(true);
    }, 0); // 약간의 지연을 두고 애니메이션 시작
  }, []);

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

      swiperInstance.on("slideChange", () => {
        setIsBeginning(swiperInstance.isBeginning);
        setIsEnd(swiperInstance.isEnd);
      });

      setIsBeginning(swiperInstance.isBeginning);
      setIsEnd(swiperInstance.isEnd);
    }
  }, [swiperInstance]);

  const handleSwiper = (swiper: SwiperInstance) => {
    setSwiperInstance(swiper);

    swiper.on("slideChange", () => {
      setIsBeginning(swiper.isBeginning);
      setIsEnd(swiper.isEnd);
    });

    setIsBeginning(swiper.isBeginning);
    setIsEnd(swiper.isEnd);
  };

  const handlePrevClick = () => {
    if (swiperInstance && !isBeginning) {
      swiperInstance.slidePrev();
    }
  };

  const handleNextClick = () => {
    if (swiperInstance && !isEnd) {
      swiperInstance.slideNext();
    }
  };

  const goToRecommend = () => {
    navigate("/recommend");
  };

  return (
    <div
      className={`relative bg-black text-white min-h-screen flex items-center justify-center transition-all duration-700 ${
        isLoaded
          ? "scale-100 opacity-100 bg-transparent "
          : "scale-110 opacity-50 bg-black"
      }`}
      style={{
        backgroundColor: "black", 
        backgroundImage: isLoaded
          ? `url(src/assets/movie/theater3.jpg)`
          : "none",
        backgroundSize: "cover",
        backgroundPosition: "center",
      }}
    >
      {/* 영화관 스타일 화면 */}
      <div className="relative w-[75%] h-[60vh] rounded-md overflow-hidden">
        <img
          src={exit}
          alt="exit"
          className="w-24 mt-4 opacity-75"
          onClick={goToRecommend}
        />
        <h2 className="text-center text-3xl font-bold text-black mb-[80px] mt-2">
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
