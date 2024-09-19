import React, { useRef, useEffect, useState } from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import { Navigation, Pagination } from "swiper/modules";
import { Swiper as SwiperInstance, NavigationOptions } from "swiper/types"; // Swiper 인스턴스 타입 가져오기

const FavoritePage: React.FC = () => {
  const movieImages = [
    "/src/assets/avengers1.jpg",
    "/src/assets/fastand.jpg",
    "/src/assets/firstandF.jpg",
    "https://via.placeholder.com/200x300?text=Movie+4",
    "https://via.placeholder.com/200x300?text=Movie+5",
    "/src/assets/lordof.jpg",
    "/src/assets/monster.jpg",
    "https://via.placeholder.com/200x300?text=Movie+8",
    "https://via.placeholder.com/200x300?text=Movie+9",
    "https://via.placeholder.com/200x300?text=Movie+10",
    "https://via.placeholder.com/200x300?text=Movie+11",
    "https://via.placeholder.com/200x300?text=Movie+12",
    "https://via.placeholder.com/200x300?text=Movie+13",
    "https://via.placeholder.com/200x300?text=Movie+14",
  ];

  // 8개씩 나누어 슬라이드 페이지로 구성
  const slides = [];
  for (let i = 0; i < movieImages.length; i += 10) {
    slides.push(movieImages.slice(i, i + 10));
  }

  const prevRef = useRef<HTMLDivElement | null>(null);
  const nextRef = useRef<HTMLDivElement | null>(null);
  const [swiperInstance, setSwiperInstance] = useState<SwiperInstance | null>(
    null
  ); // Swiper 인스턴스 타입 설정
  const [isBeginning, setIsBeginning] = useState(true);
  const [isEnd, setIsEnd] = useState(false);

  useEffect(() => {
    if (swiperInstance && prevRef.current && nextRef.current) {
      // Swiper navigation setup
      swiperInstance.params.navigation = {
        ...(swiperInstance.params.navigation as NavigationOptions), // NavigationOptions로 타입 캐스팅
        prevEl: prevRef.current,
        nextEl: nextRef.current,
      };

      // Re-initialize navigation after setting new elements
      swiperInstance.navigation.destroy();
      swiperInstance.navigation.init();
      swiperInstance.navigation.update();

      // Event listener for slide change to update button visibility
      swiperInstance.on("slideChange", () => {
        setIsBeginning(swiperInstance.isBeginning);
        setIsEnd(swiperInstance.isEnd);
      });

      // Set initial button states
      setIsBeginning(swiperInstance.isBeginning);
      setIsEnd(swiperInstance.isEnd);
    }
  }, [swiperInstance]);

  const handleSwiper = (swiper: SwiperInstance) => {
    setSwiperInstance(swiper);

    // Update navigation visibility when swiper instance is set
    swiper.on("slideChange", () => {
      setIsBeginning(swiper.isBeginning);
      setIsEnd(swiper.isEnd);
    });
  };

  // 직접 슬라이드 이동 이벤트 연결
  const handlePrevClick = () => {
    if (swiperInstance) {
      swiperInstance.slidePrev();
    }
  };

  const handleNextClick = () => {
    if (swiperInstance) {
      swiperInstance.slideNext();
    }
  };

  return (
    <div className="bg-black p-8 rounded-lg w-[1200px] relative">
      <h2 className="text-2xl font-semibold italic text-white mb-6">
        My Favorite Movies
      </h2>

      {/* 외부 화살표 버튼 - 슬라이드 상태에 따라 표시 */}
      {!isBeginning && (
        <div
          ref={prevRef}
          className="swiper-button-prev-custom"
          onClick={handlePrevClick}
        >
          &#10094;
        </div>
      )}
      {!isEnd && (
        <div
          ref={nextRef}
          className="swiper-button-next-custom"
          onClick={handleNextClick}
        >
          &#10095;
        </div>
      )}

      <Swiper
        modules={[Navigation, Pagination]}
        onSwiper={handleSwiper}
        navigation={{
          prevEl: prevRef.current,
          nextEl: nextRef.current,
          hideOnClick: true,
        }}
        pagination={{
          clickable: true,
          hideOnClick: true,
        }}
        spaceBetween={30}
        slidesPerView={1}
        style={{ overflow: "hidden" }}
      >
        {slides.map((slide, index) => (
          <SwiperSlide key={index}>
            <div className="grid grid-cols-5 gap-6">
              {slide.map((image, idx) => (
                <img
                  key={idx}
                  src={image}
                  alt={`Movie ${idx + 1}`}
                  className="rounded-lg object-cover"
                  style={{ width: "250px", height: "300px" }}
                />
              ))}
            </div>
          </SwiperSlide>
        ))}
      </Swiper>

      <style>
        {`
          .swiper-pagination {
            bottom: 10px;
            left: 0;
            right: 0;
            text-align: right;
            padding-right: 20px;
          }
          .swiper-button-prev-custom, .swiper-button-next-custom {
            color: white;
            top: 50%;
            transform: translateY(-50%);
            position: absolute;
            z-index: 100;
            font-size: 24px;
            cursor: pointer;
          }
          .swiper-button-prev-custom {
            left: -20px;
          }
          .swiper-button-next-custom {
            right: -20px;
          }
        `}
      </style>
    </div>
  );
};

export default FavoritePage;
