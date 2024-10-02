import React, { useState, useRef, useEffect } from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation, Pagination } from "swiper/modules";
import { Swiper as SwiperInstance, NavigationOptions } from "swiper/types";
import { useNavigate } from "react-router-dom";

interface MoviesListProps {
  category: string;
  movies: Movie[]; // movieSeq와 moviePosterUrl을 포함하는 배열
}

interface Movie {
  movieSeq: number;
  moviePosterUrl: string;
}

const MoviesList: React.FC<MoviesListProps> = ({ category, movies }) => {
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

  const goToDetail = (movieSeq: number) => {
    navigate(`/moviedetail/${movieSeq}`);
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
        {movies.map((movie) => {
          // console.log("Movie Seq:", movie.movieSeq); // movieSeq가 고유한지 확인
          return (
            <SwiperSlide
              key={movie.movieSeq} // movieSeq를 고유한 key로 사용
              className="flex justify-center items-center transition-transform duration-300 transform hover:-translate-y-2 mt-4"
            >
              <img
                src={movie.moviePosterUrl}
                alt={`Movie ${movie.movieSeq}`}
                onClick={() => goToDetail(movie.movieSeq)}
                className="rounded-lg shadow-md object-cover w-full h-[306px] cursor-pointer"
              />
            </SwiperSlide>
          );
        })}
      </Swiper>
    </div>
  );
};

export default MoviesList;
