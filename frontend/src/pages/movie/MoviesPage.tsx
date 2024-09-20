import React, { useEffect, useRef, useState } from "react";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import Navbar from "../../components/common/Navbar";
import "../../css/MoivesPage.css";
import MoviesList from "../../components/MoviesList";

const MoviesPage = () => {
  const [isExpanded, setIsExpanded] = useState(false);
  const searchRef = useRef<HTMLDivElement | null>(null);

  const handleClick = () => {
    setIsExpanded((prev) => !prev);
  };

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        searchRef.current &&
        !searchRef.current.contains(event.target as Node)
      ) {
        setIsExpanded(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

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

  return (
    <div className="flex flex-col bg-black h-screen overflow-y-auto">
      <header className="sticky top-0 bg-transparent z-10">
        <Navbar />
      </header>

      <div className="mt-[100px] flex justify-end items-end w-[1800px]">
        <div
          ref={searchRef}
          className={`flex items-center transition-all duration-400 ${
            isExpanded
              ? "w-[370px] border border-white p-2 rounded"
              : "w-[50px]"
          }`}
        >
          <div className="flex items-center">
            <svg
              onClick={handleClick}
              width="30"
              height="30"
              viewBox="0 0 24 24"
              fill="none"
              xmlns="http://www.w3.org/2000/svg"
              className={`cursor-pointer transition-transform duration-400 ${
                isExpanded ? "translate-x-[-50px] " : ""
              }`}
            >
              <circle
                cx="11"
                cy="11"
                r="7"
                stroke="white"
                strokeWidth="2"
                fill="none"
              />
              <line
                x1="16"
                y1="16"
                x2="22"
                y2="22"
                stroke="white"
                strokeWidth="2"
              />
            </svg>
            <input
              type="text"
              placeholder="제목, 사람, 장르"
              className={`bg-transparent border-none outline-none text-white text-lg transition-all duration-400 ${
                isExpanded ? "w-[200px] opacity-100" : "w-0 opacity-0"
              }`}
            />
          </div>
        </div>
      </div>

      <MoviesList category="한국 영화" movieImg={movieImg} />
      <MoviesList category="액션 & 어드벤처 시리즈" movieImg={movieImg} />
      <MoviesList category="진심이 느껴지는 영화" movieImg={movieImg} />
    </div>
  );
};

export default MoviesPage;
