import React, { useRef, useEffect, useState } from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import { Navigation, Pagination } from "swiper/modules";
import { Swiper as SwiperInstance, NavigationOptions } from "swiper/types";
import { useQuery } from "@tanstack/react-query";
import { fetchFavoriteMovies } from "../../apis/axios";
import { useUserQuery } from "../../hooks/useUserQuery";
import { useNavigate } from "react-router-dom";

interface FavoriteMovie {
  movieSeq: number;
  moviePosterUrl: string;
}

const FavoritePage: React.FC = () => {
  const navigate = useNavigate();
  // 사용자 정보 가져오기
  const { data: userData } = useUserQuery();
  const userSeq = userData?.userSeq;

  // 즐겨찾기 영화 가져오기
  // 하나의 객체로 묶어서 전달해야함 !!
  const { data: favoriteMovies } = useQuery({
    queryKey: ["favoriteMovies", userSeq], // queryKey
    queryFn: () => fetchFavoriteMovies(userSeq!), // queryFn
    enabled: !!userSeq, // userSeq가 존재할 때만 쿼리 실행, options
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
  if (favoriteMovies) {
    for (let i = 0; i < favoriteMovies.length; i += 10) {
      slides.push(favoriteMovies.slice(i, i + 10));
    }
  }

  console.log(slides);

  return (
    <div className="bg-black p-8 rounded-lg w-[1200px] relative">
      <h2 className="text-2xl font-semibold italic text-white mb-6">
        My Favorite Movies
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
        {slides.map((slide, index) => (
          <SwiperSlide key={index}>
            <div className="grid grid-cols-5 gap-6">
              {slide.map((movie: FavoriteMovie, idx: number) => (
                <img
                  key={idx}
                  src={movie.moviePosterUrl}
                  alt={`Movie ${idx + 1}`}
                  className="rounded-lg object-cover cursor-pointer"
                  onClick={() => goToDetail(movie.movieSeq)}
                  style={{ width: "250px", height: "300px" }}
                />
              ))}
            </div>
          </SwiperSlide>
        ))}
      </Swiper>
    </div>
  );
};

export default FavoritePage;
