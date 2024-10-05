import React, { useState, useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { fetchMoviesBySearch } from "../../apis/axios";
import Navbar from "../../components/common/Navbar";
import SearchBar from "../../components/SearchBar";
import SelectionList from "../../components/SelectionList";
import { useUserQuery } from "../../hooks/useUserQuery";
import { Movie } from "../../type";
import { IoIosArrowRoundBack } from "react-icons/io";


const SearchPage: React.FC = () => {
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const searchQuery = searchParams.get("query") || "";

  const [movies, setMovies] = useState<Movie[]>([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(true);
  const { data: userData } = useUserQuery();
  const userSeq = userData?.userSeq;
  const [isExpanded, setIsExpanded] = useState(false);
  const navigate = useNavigate();


  useEffect(() => {
    // 새로운 검색어가 들어올 때, 기존 영화 데이터를 초기화하고 페이지를 0으로 리셋
    setMovies([]); // 기존 데이터 초기화
    setPage(0); // 페이지 초기화
  }, [searchQuery]);

  useEffect(() => {
    const fetchMovies = async () => {
      try {
        if (userSeq && page <= 3) {
          const newMovies = await fetchMoviesBySearch(
            searchQuery,
            userSeq,
            page,
            15
          );
          setMovies((prevMovies) => [...prevMovies, ...newMovies]);
          if (newMovies.length < 15 || page === 3) {
            setHasMore(false);
          }
        }
      } catch (error) {
        console.error("Error fetching search results:", error);
      }
    };

    if (searchQuery && userSeq) {
      fetchMovies();
    }
  }, [searchQuery, page, userSeq]);

  const loadMoreMovies = () => {
    if (page < 3) {
      setPage((prevPage) => prevPage + 1);
    }
  };

  return (
    <div className="flex flex-col bg-black h-screen text-white overflow-y-auto">
      <header className="sticky top-0 bg-transparent z-20">
        <Navbar />
        <IoIosArrowRoundBack
          onClick={() => navigate(-1)} // 뒤로가기 기능
          className="text-gray-200 cursor-pointer fixed left-4 top-16 w-10 h-10 hover:opacity-60" // 크기 및 위치 설정
        />
      </header>

      <div className="mt-[100px] flex justify-end items-end w-full pr-5">
        <SearchBar
          initialSearchQuery={searchQuery}
          isExpanded={isExpanded}
          setIsExpanded={setIsExpanded}
        />
      </div>

      <h1 className="text-2xl font-bold text-gray-300 mt-8 mb-4 ml-16">
        "{searchQuery}" 검색 결과
      </h1>
      <SelectionList
        movies={movies}
        loadMoreMovies={loadMoreMovies}
        hasMore={hasMore}
      />
    </div>
  );
};

export default SearchPage;
