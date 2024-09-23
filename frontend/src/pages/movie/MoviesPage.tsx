import React, { useState } from "react";
import Navbar from "../../components/common/Navbar";
import "../../css/MoviesPage.css";
import MoviesList from "../../components/MoviesList";
import SearchBar from "../../components/SearchBar";
import Filter from "../../components/Filter"; // Filter 컴포넌트 추가

const MoviesPage: React.FC = () => {
  const [isExpanded, setIsExpanded] = useState(false); // 검색창 상태 관리
  const [selectedGenre, setSelectedGenre] = useState(""); // 선택된 장르 관리

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

  const genres = [
    { value: "SF", label: "SF" },
    { value: "가족", label: "가족" },
    { value: "공연실황", label: "공연실황" },
    { value: "공포", label: "공포" },
    { value: "다큐멘터리", label: "다큐멘터리" },
    { value: "단편", label: "단편" },
    { value: "드라마", label: "드라마" },
    { value: "로맨스", label: "로맨스" },
    { value: "로맨틱 코미디", label: "로맨틱 코미디" },
    { value: "모험", label: "모험" },
    { value: "뮤지컬", label: "뮤지컬" },
    { value: "미스터리", label: "미스터리" },
    { value: "범죄", label: "범죄" },
    { value: "서부극", label: "서부극" },
    { value: "스릴러", label: "스릴러" },
    { value: "스포츠", label: "스포츠" },
    { value: "시대극", label: "시대극" },
    { value: "애니메이션", label: "애니메이션" },
    { value: "액션", label: "액션" },
    { value: "역사", label: "역사" },
    { value: "예능", label: "예능" },
    { value: "음악", label: "음악" },
    { value: "재난", label: "재난" },
    { value: "전기", label: "전기" },
    { value: "전쟁", label: "전쟁" },
    { value: "코미디", label: "코미디" },
    { value: "키즈", label: "키즈" },
    { value: "틴에이저", label: "틴에이저" },
    { value: "판타지", label: "판타지" },
  ];

  return (
    <div className="flex flex-col bg-black h-screen overflow-y-auto">
      <header className="sticky top-0 bg-transparent z-10">
        <Navbar />
      </header>

      <div className="mt-[100px] flex justify-between items-end w-[1800px] pl-10">
        {/* Filter 컴포넌트 추가 */}
        <div className="mb-3 mt-1">
          {/* {" "} */}
          <Filter
            options={genres}
            onChange={(value) => setSelectedGenre(value)}
            defaultValue={selectedGenre || "장르"}
            customClass="grid grid-cols-3 gap-2 w-96" // MoviesPage에서만 3열 적용
          />
        </div>
        <SearchBar isExpanded={isExpanded} setIsExpanded={setIsExpanded} />
      </div>

      <MoviesList category="한국 영화" movieImg={movieImg} />
      <MoviesList category="액션 & 어드벤처 시리즈" movieImg={movieImg} />
      <MoviesList category="진심이 느껴지는 영화" movieImg={movieImg} />
    </div>
  );
};

export default MoviesPage;
