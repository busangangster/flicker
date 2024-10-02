import React, { useEffect, useState, useRef } from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation } from "swiper/modules";
import "swiper/css"; // Swiper 기본 CSS
import "swiper/css/navigation"; // 네비게이션 모듈 사용 시 필요한 CSS
import "../css/TopTen.css";
import { getTopTenMovies } from "../apis/movieApi";
import { TopTenMovie } from "../type"; // 타입 정의
import { useNavigate } from "react-router-dom";

// Font import for Rubik Doodle Shadow
const GOOGLE_FONT_LINK =
  "https://fonts.googleapis.com/css2?family=Rubik+Doodle+Shadow&display=swap";

const TopTen: React.FC = () => {
  const [movies, setMovies] = useState<TopTenMovie[]>([]);
  const prevRef = useRef<HTMLDivElement | null>(null);
  const nextRef = useRef<HTMLDivElement | null>(null);
  const navigate = useNavigate();

  // Fetch top 10 movies from backend
  useEffect(() => {
    const getMovies = async () => {
      try {
        const response = await getTopTenMovies(); // API 호출
        setMovies(response.data); // API 응답 데이터 설정
      } catch (error) {
        console.error("영화 목록을 불러오는 데 실패했습니다.", error);
      }
    };

    getMovies();

    // Load the Google font for Rubik Doodle Shadow
    const fontLink = document.createElement("link");
    fontLink.href = GOOGLE_FONT_LINK;
    fontLink.rel = "stylesheet";
    document.head.appendChild(fontLink);
  }, []);

  const goToDetail = (movieSeq:number) => {
    navigate(`/moviedetail/${movieSeq}`)
  }

  return (
    <div className="flex items-center h-[450px]">
      {/* Left Side: Static text "TODAY TOP 10" */}
      <div className="today-top-10 flex flex-col justify-center text-right mr-8 text-white ml-10">
        <h1
          className="text-[60px] mb-4 mt-14"
          style={{ fontFamily: "Rubik Doodle Shadow, cursive" }}
        >
          TODAY
        </h1>
        <h1
          className="text-[70px] mr-1 mb-2"
          style={{ fontFamily: "Rubik Doodle Shadow, cursive" }}
        >
          TOP
        </h1>
        <h1
          className="text-[70px] mr-2"
          style={{ fontFamily: "Rubik Doodle Shadow, cursive" }}
        >
          10
        </h1>
      </div>

      {/* Right Side: Movie posters with Swiper */}
      <div className="movie-swiper-container relative w-[1500px]">
        {/* Swiper Navigation Buttons */}
        <div ref={prevRef} className="swiper-button-prev-custom2">
          {/* SVG for Left Arrow */}
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

        <div ref={nextRef} className="swiper-button-next-custom2">
          {/* SVG for Right Arrow */}
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

        {/* Movie Poster Swiper */}
        <Swiper
          slidesPerView={6} // Number of visible posters
          loop={true} // Infinite scrolling
          spaceBetween={30}
          navigation={{
            prevEl: prevRef.current,
            nextEl: nextRef.current,
          }}
          modules={[Navigation]}
          onSwiper={(swiper) => {
            // Type guard to check if navigation is not false
            if (
              swiper.params.navigation &&
              typeof swiper.params.navigation !== "boolean"
            ) {
              swiper.params.navigation.prevEl = prevRef.current;
              swiper.params.navigation.nextEl = nextRef.current;
              swiper.navigation.init();
              swiper.navigation.update();
            }
          }}
        >
          {Array.isArray(movies) && movies.length > 0 ? (
            movies.map((movie, index) => (
              <SwiperSlide key={movie.movieSeq} className="swiper-slide-item">
                <div className="movie-poster-container relative flex flex-col items-center">
                  {/* Number Overlay */}
                  <span
                    className="movie-number text-white text-[70px] mb-4 relative"
                    style={{
                      left: "-75px",
                      top: "30px",
                      fontFamily: "Rubik Doodle Shadow, cursive",
                    }}
                  >
                    {index + 1}
                  </span>
                  {/* Movie Poster */}
                  <img
                    src={movie.moviePosterUrl}
                    alt={movie.movieTitle}
                    onClick={() => goToDetail(movie.movieSeq)}
                    className="h-[350px] rounded-lg object-cover transform transition-transform duration-300 hover:-translate-y-2"
                  />
                </div>
              </SwiperSlide>
            ))
          ) : (
            <p className="text-white">Loading...</p> // 데이터 로딩 중 표시
          )}
        </Swiper>
      </div>
    </div>
  );
};

export default TopTen;
