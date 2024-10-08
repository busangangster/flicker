import React, { useState, useEffect, useCallback } from "react";
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
  const { data: userData } = useUserQuery();
  const userSeq = userData?.userSeq;
  const [page, setPage] = useState<number>(0);
  const [isExpanded, setIsExpanded] = useState(false);
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);

  const initalPage = 0;
  const MAX_PAGE = initalPage + 3;


  const loadMoreMovies = async () => {
    if (isLoading || !hasMore || page > MAX_PAGE || !userSeq || !searchQuery) return;

    setIsLoading(true);
    try {
      console.log(`페이지 ${page}에 대해 영화 데이터를 로드 중...`);
      
      if (searchQuery) {
        const response = await fetchMoviesBySearch(searchQuery, userSeq, page, 20);

        console.log("새로운 영화 데이터", response);

        setMovies((preMovies) => {
          const allMovies = [...preMovies, ...response];
          const uniqueMovies = allMovies.filter(
            (movie, index, self) => index === self.findIndex((m) => m.movieSeq === movie.movieSeq)
          );

          console.log("업데이트된 영화 목록:", uniqueMovies);

          return uniqueMovies;
        });
        if (response.length < 20 || page >= MAX_PAGE) {
          setHasMore(false);
        } else {
          setPage((prevPage) => prevPage + 1);
        }
      }
    } catch (error) {
      console.error("Error fetching more movies:", error);
    } finally {
      setIsLoading(false);
    }
  }

  useEffect(() => {
    setMovies([]);
    setPage(initalPage);
    setHasMore(true);
    loadMoreMovies();
  },[searchQuery,userSeq])

  const handleScroll = useCallback(() => {
    if (
      window.innerHeight + window.scrollY >=
        document.documentElement.scrollHeight - 40 &&
      hasMore &&
      !isLoading &&
      page <= MAX_PAGE
    ) {
      loadMoreMovies();
    }
  }, [hasMore, isLoading, page]);

  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, [handleScroll]);


  return (
    <div className="flex flex-col bg-black min-h-screen text-white overflow-y-auto">
      <header className="sticky top-0 bg-transparent z-20">
        <Navbar />
        <IoIosArrowRoundBack
          onClick={() => navigate(-1)}
          className="text-gray-200 cursor-pointer fixed left-4 top-16 w-10 h-10 hover:opacity-60"
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

      <SelectionList movies={movies} />

      {isLoading && <p className="text-center text-gray-500">로딩 중...</p>}
    </div>
  );
};

export default SearchPage;
