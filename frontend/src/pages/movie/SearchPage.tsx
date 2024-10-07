import React, { useState, useEffect, useCallback } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import { fetchMoviesBySearch } from "../../apis/axios";
import Navbar from "../../components/common/Navbar";
import SearchBar from "../../components/SearchBar";
import SelectionList from "../../components/SelectionList";
import { useUserQuery } from "../../hooks/useUserQuery";
import { Movie } from "../../type";
import { IoIosArrowRoundBack } from "react-icons/io";
import { throttle } from "lodash"; // lodash로 스크롤 이벤트를 최적화

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
  const [isLoading, setIsLoading] = useState(false); // 로딩 상태 추가

  useEffect(() => {
    setMovies([]); // 기존 데이터 초기화
    setPage(0); // 페이지 초기화
  }, [searchQuery]);

  useEffect(() => {
    const fetchMovies = async () => {
      if (!userSeq || !searchQuery || page > 3 || isLoading) return; // 조건이 맞지 않으면 중단

      setIsLoading(true); // 로딩 시작
      try {
        const newMovies = await fetchMoviesBySearch(
          searchQuery,
          userSeq,
          page,
          20
        );
        setMovies((prevMovies) => [...prevMovies, ...newMovies]);
        if (newMovies.length < 15 || page === 3) {
          setHasMore(false);
        }
      } catch (error) {
        console.error("Error fetching search results:", error);
      } finally {
        setIsLoading(false); // 로딩 종료
      }
    };

    fetchMovies();
  }, [searchQuery, page, userSeq, isLoading]);

  // 스크롤 이벤트 감지하여 페이지 증가
  const handleScroll = useCallback(
    throttle(() => {
      const scrollableElement = document.documentElement;
      if (
        scrollableElement.scrollTop + window.innerHeight >=
          scrollableElement.scrollHeight - 50 &&
        hasMore &&
        !isLoading
      ) {
        setPage((prevPage) => prevPage + 1);
      }
    }, 1000),
    [hasMore, isLoading]
  );

  // 스크롤 이벤트 추가
  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, [handleScroll]);

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
        loadMoreMovies={() => setPage((prevPage) => prevPage + 1)}
        hasMore={hasMore}
      />

      {isLoading && <p className="text-center text-gray-500">로딩 중...</p>}
    </div>
  );
};

export default SearchPage;
