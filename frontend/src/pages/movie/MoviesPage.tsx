// src/pages/movie/MoviesPage.tsx
import React, { useState } from "react";
import Navbar from "../../components/common/Navbar";
import "../../css/MoviesPage.css";
import MoviesList from "../../components/MoviesList";
import SearchBar from "../../components/SearchBar";

const MoviesPage = () => {
  const [isExpanded, setIsExpanded] = useState(false); // 검색창 상태 관리
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
        <SearchBar isExpanded={isExpanded} setIsExpanded={setIsExpanded} />
      </div>

      <MoviesList category="한국 영화" movieImg={movieImg} />
      <MoviesList category="액션 & 어드벤처 시리즈" movieImg={movieImg} />
      <MoviesList category="진심이 느껴지는 영화" movieImg={movieImg} />
    </div>
  );
};

export default MoviesPage;
