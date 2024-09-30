import React, { useEffect, useState, useRef } from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation } from "swiper/modules";
import "swiper/css"; // Swiper 기본 CSS
import "swiper/css/navigation"; // 네비게이션 모듈 사용 시 필요한 CSS
import "../css/TopTen.css";
import { TopTenMovie } from "../type"

// Font import for Rubik Doodle Shadow
const GOOGLE_FONT_LINK =
  "https://fonts.googleapis.com/css2?family=Rubik+Doodle+Shadow&display=swap";


const TopTen: React.FC = () => {
  const [movies, setMovies] = useState<TopTenMovie[]>([]);
  const prevRef = useRef<HTMLDivElement | null>(null);
  const nextRef = useRef<HTMLDivElement | null>(null);

  // Simulate fetching movie data from assets
  useEffect(() => {
    const getMovies = async () => {
      // Hardcoded movie data with 10 poster images from assets
      const movieData = Array.from({ length: 10 }, (_, index) => ({
        movieSeq: index,
        movieTitle: `Movie ${index + 1}`,
        moviePosterUrl: `/assets/survey/image${index + 1}.jpg`,
      }));

      setMovies(movieData);
    };
    getMovies();

    // Load the Google font for Rubik Doodle Shadow
    const fontLink = document.createElement("link");
    fontLink.href = GOOGLE_FONT_LINK;
    fontLink.rel = "stylesheet";
    document.head.appendChild(fontLink);
  }, []);

  return (
    <div className="flex items-center">
      {/* Left Side: Static text "TODAY TOP 10" */}
      <div className="today-top-10 flex flex-col justify-center text-right mr-8 text-white ml-10">
        <h1 className="text-[60px] mb-4 mt-14">TODAY</h1>
        <h1 className="text-[70px] mr-1 mb-2">TOP</h1>
        <h1 className="text-[70px] mr-2">10</h1>
      </div>

      {/* Right Side: Movie posters with Swiper */}
      <div className="movie-swiper-container relative w-[1800px]">
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
          {movies.map((movie, index) => (
            <SwiperSlide key={movie.movieSeq} className="swiper-slide-item">
              <div className="movie-poster-container relative flex flex-col items-center">
                {/* Number Overlay */}
                <span
                  className="movie-number text-white text-[70px] mb-4 relative"
                  style={{ left: "-75px", top: "30px" }}
                >
                  {index + 1}
                </span>
                {/* Movie Poster */}
                <img
                  src={movie.moviePosterUrl}
                  alt={movie.movieTitle}
                  className="h-[350px] rounded-lg object-cover transform transition-transform duration-300 hover:-translate-y-2"
                />
              </div>
            </SwiperSlide>
          ))}
        </Swiper>
      </div>
    </div>
  );
};

export default TopTen;
