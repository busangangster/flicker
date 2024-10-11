import React, { useRef, useEffect, useState } from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import { Navigation, Pagination } from "swiper/modules";
import { Swiper as SwiperInstance, NavigationOptions } from "swiper/types";
import { useQuery } from "@tanstack/react-query";
import { fetchDislikeMovies } from "../../apis/axios";
import { useUserQuery } from "../../hooks/useUserQuery";
import { useNavigate } from "react-router-dom";
import  "../../css/MovieList.css";
import { FavoriteMovie } from "../../type";

const DislikePage: React.FC = () => {
  const navigate = useNavigate();
  const { data: userData } = useUserQuery();
  const userSeq = userData?.userSeq;
  const { data: dislikeMovies } = useQuery({
    queryKey: ["dislikeMovies", userSeq], 
    queryFn: () => fetchDislikeMovies(userSeq!), 
    enabled: !!userSeq, 
  });

  const prevRef = useRef<HTMLDivElement | null>(null);
  const nextRef = useRef<HTMLDivElement | null>(null);
  const [swiperInstance, setSwiperInstance] = useState<SwiperInstance | null>(
    null
  );
  const [isBeginning, setIsBeginning] = useState(true);
  const [isEnd, setIsEnd] = useState(false);

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
  };

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

  const goToDetail = (movieSeq: number) => {
    navigate(`/moviedetail/${movieSeq}`);
  };

  const slides = [];
  if (dislikeMovies) {
    for (let i = 0; i < dislikeMovies.length; i += 10) {
      slides.push(dislikeMovies.slice(i, i + 10));
    }
  }

  return (
    <div className="bg-black p-8 rounded-lg w-[1200px] relative h-5/6">
      <h2 className="text-2xl font-semibold italic text-white mb-6">
        My Dislike Movies
      </h2>

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
        spaceBetween={30}
        slidesPerView={1}
        style={{ overflow: "hidden" }}
      >
        {slides && slides.length > 0 ? (
          slides.map((slide, index) => (
            <SwiperSlide key={index}>
              <div className="grid grid-cols-5 gap-6">
                {slide.map((movie: FavoriteMovie, idx: number) => (
                  <img
                    key={idx}
                    src={movie.moviePosterUrl}
                    alt={`Movie ${idx + 1}`}
                    className="rounded-lg object-cover card"
                    onClick={() => goToDetail(movie.movieSeq)}
                    style={{ width: "200px", height: "270px" }}
                  />
                ))}
              </div>
            </SwiperSlide>
          ))
        ) : (
          <p>관심 없는 영화 목록이 없습니다.</p>
        )}
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

export default DislikePage;
