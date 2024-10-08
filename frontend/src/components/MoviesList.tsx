import React, { useRef, useEffect, useState, Fragment } from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation, Pagination } from "swiper/modules";
import { Swiper as SwiperInstance, NavigationOptions } from "swiper/types";
import { useNavigate } from "react-router-dom";
import { MoviesListProps } from "../type";
import "../css/MovieList.css"; 
import { FaStar, FaStarHalfAlt } from "react-icons/fa"; 


const MoviesList: React.FC<MoviesListProps> = ({ category, movies }) => {
  const prevRef = useRef<HTMLDivElement | null>(null);
  const nextRef = useRef<HTMLDivElement | null>(null);
  const [swiperInstance, setSwiperInstance] = useState<SwiperInstance | null>(
    null
  );
  const [isBeginning, setIsBeginning] = useState(true);
  const [isEnd, setIsEnd] = useState(false);
  const navigate = useNavigate();

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

  const goToDetail = (movieSeq: number) => {
    navigate(`/moviedetail/${movieSeq}`);
  };

  const renderStars = (rating: number) => {
    const totalStars = 5;
    return [...Array(totalStars)].map((_, index) => {
      const ratingValue = index + 1;
      return (
        <Fragment key={index}>
          {rating >= ratingValue ? (
            <FaStar className="star" color="#ffc107" size={16} />
          ) : rating >= ratingValue - 0.5 ? (
            <FaStarHalfAlt className="star" color="#ffc107" size={16} />
          ) : (
            <FaStar className="star" color="#e4e5e9" size={16} />
          )}
        </Fragment>
      );
    });
  };

  return (
    <div className="relative w-full flex-shrink-0 mb-[50px]">
      <h3 className="text-white mb-[10px] text-[27px] ml-[50px] mt-[20px]">
        {category}
      </h3>

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
        spaceBetween={20}
        slidesOffsetBefore={50}
        navigation={{
          nextEl: nextRef.current,
          prevEl: prevRef.current,
        }}
        onSwiper={handleSwiper}
        modules={[Navigation, Pagination]}
        breakpoints={{
          320: {
            slidesPerView: 2, // 작은 화면 (모바일)
            spaceBetween: 10,
          },
          640: {
            slidesPerView: 3, // 중간 화면 (작은 태블릿)
            spaceBetween: 15,
          },
          768: {
            slidesPerView: 4, // 태블릿
            spaceBetween: 20,
          },
          1024: {
            slidesPerView: 6, // 작은 데스크탑
            spaceBetween: 20,
          },
          1280: {
            slidesPerView: 7, // 큰 데스크탑
            spaceBetween: 20,
          },
          1700: {
            slidesPerView: 8,
            spaceBetween: 25,
          },
          1920: {
            slidesPerView: 9,
            spaceBetween: 28,
          },
          2150: {
            slidesPerView: 10,
            spaceBetween: 30,
          },
          2300: {
            slidesPerView: 11,
            spaceBetween: 30,
          },
        }}
      >
        {movies && movies.length > 0 ? (
          movies.map((movie) => (
            <SwiperSlide
              key={movie.movieSeq}
              className="flex justify-center items-center mt-4 card-wrapper" // 카드 스타일 추가
            >
              <div className="card">
                <div className="poster">
                  <img
                    src={movie.moviePosterUrl}
                    alt={`Movie ${movie.movieSeq}`}
                    onClick={() => goToDetail(movie.movieSeq)}
                    className="card-poster"
                  />
                </div>
                <div className="details">
                  <h1 className="text-[20px]">{movie.movieTitle}</h1>
                  <div className="text-[15px]">
                    {`${movie.movieYear || "정보 없음"} • ${
                      movie.runningTime || "정보 없음"
                    } • ${movie.audienceRating || "정보 없음"}`}
                  </div>
                  <div className="rating flex items-center">
                    {renderStars(movie.movieRating ?? 0)}{" "}
                    <span className="ml-2">{movie.movieRating ?? 0}/5</span>{" "}
                  </div>
                </div>
              </div>
            </SwiperSlide>
          ))
        ) : (
          <p className="text-white ml-[50px]">영화 데이터가 없습니다.</p>
        )}
      </Swiper>
    </div>
  );
};

export default MoviesList;
