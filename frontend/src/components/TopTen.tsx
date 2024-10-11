import React, { useEffect, useState, useRef } from "react";
import { Swiper, SwiperSlide } from "swiper/react";
import { Navigation } from "swiper/modules";
import "swiper/css";
import "swiper/css/navigation";
import "../css/TopTen.css";
import { getTopTenMovies } from "../apis/movieApi";
import { TopTenMovie } from "../type";
import { useNavigate } from "react-router-dom";

const GOOGLE_FONT_LINK =
  "https://fonts.googleapis.com/css2?family=Rubik+Doodle+Shadow&display=swap";

const TopTen: React.FC = () => {
  const [movies, setMovies] = useState<TopTenMovie[]>([]);
  const prevRef = useRef<HTMLDivElement | null>(null);
  const nextRef = useRef<HTMLDivElement | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const getMovies = async () => {
      try {
        const response = await getTopTenMovies();
        setMovies(response.data);
      } catch (error) {
        console.error("영화 목록을 불러오는 데 실패했습니다.", error);
      }
    };

    getMovies();

    const fontLink = document.createElement("link");
    fontLink.href = GOOGLE_FONT_LINK;
    fontLink.rel = "stylesheet";
    document.head.appendChild(fontLink);
  }, []);

  const goToDetail = (movieSeq: number) => {
    navigate(`/moviedetail/${movieSeq}`);
  };

  return (
    <div className="flex items-center h-[450px] mb-8 max-w-full px-0 xl:px-0 ">
      {/* Left Side: Static text "TODAY TOP 10" */}
      <div className="today-top-10 flex flex-col justify-center text-right mr-8 text-white flex-shrink-0">
        <h1
          className="text-[40px] md:text-[60px] xl:text-[70px] mb-4 mt-14 ml-10"
          style={{ fontFamily: "Rubik Doodle Shadow, cursive" }}
        >
          TODAY
        </h1>
        <h1
          className="text-[50px] md:text-[60px] xl:text-[70px] mr-1 mb-2"
          style={{ fontFamily: "Rubik Doodle Shadow, cursive" }}
        >
          TOP
        </h1>
        <h1
          className="text-[50px] md:text-[60px] xl:text-[70px] mr-2"
          style={{ fontFamily: "Rubik Doodle Shadow, cursive" }}
        >
          10
        </h1>
      </div>

      {/* Right Side: Movie posters with Swiper */}
      <div className="movie-swiper-container relative w-full xl:w-5/6 overflow-hidden">
        <div ref={prevRef} className="swiper-button-prev-custom2">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-8 w-8 xl:h-10 xl:w-10 text-white"
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
          <svg
            xmlns="http://www.w3.org/2000/svg"
            className="h-8 w-8 xl:h-10 xl:w-10 text-white"
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

        <Swiper
          slidesPerView={2}
          spaceBetween={10}
          breakpoints={{
            640: {
              slidesPerView: 3,
              spaceBetween: 15,
            },
            1024: {
              slidesPerView: 4,
              spaceBetween: 20,
            },
            1280: {
              slidesPerView: 5,
              spaceBetween: 25,
            },
            1700: {
              slidesPerView: 6,
              spaceBetween: 30,
            },
            1920: {
              slidesPerView: 6,
              spaceBetween: 35,
            },
            2150: {
              slidesPerView: 7,
              spaceBetween: 40,
            },
            2300: {
              slidesPerView: 8,
              spaceBetween: 45,
            },
          }}
          loop={true}
          navigation={{
            prevEl: prevRef.current,
            nextEl: nextRef.current,
          }}
          modules={[Navigation]}
          onSwiper={(swiper) => {
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
                  <span
                    className="movie-number text-white text-[40px] md:text-[60px] xl:text-[70px] mb-4 relative"
                    style={{
                      left: "-70px",
                      top: "30px",
                      fontFamily: "Rubik Doodle Shadow, cursive",
                    }}
                  >
                    {index + 1}
                  </span>
                  <img
                    src={movie.moviePosterUrl}
                    alt={movie.movieTitle}
                    onClick={() => goToDetail(movie.movieSeq)}
                    className="h-[250px] md:h-[300px] xl:h-[350px] rounded-lg object-cover transform transition-transform duration-300 hover:-translate-y-2 cursor-pointer"
                  />
                </div>
              </SwiperSlide>
            ))
          ) : (
            <p className="text-white">Loading...</p>
          )}
        </Swiper>
      </div>
    </div>
  );
};

export default TopTen;
