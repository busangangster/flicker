import React, { useState, useEffect } from "react";
import { useLocation } from "react-router-dom";
import { fetchMoviesBySearch } from "../../apis/axios";
import Navbar from "../../components/common/Navbar";
import SearchBar from "../../components/SearchBar";
import SelectionList from "../../components/SelectionList";
import { useUserQuery } from "../../hooks/useUserQuery";
import { Movie } from "../../type";


const SearchPage: React.FC = () => {
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const searchQuery = searchParams.get("query") || "";
  
  const [movies, setMovies] = useState<Movie[]>([]);
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(true);
  const { data: userData } = useUserQuery();
  const userSeq = userData?.userSeq; 

  useEffect(() => {
    const fetchMovies = async () => {
      try {
        // userSeq가 undefined일 경우를 처리
        if (userSeq) {
          const newMovies = await fetchMoviesBySearch(
            searchQuery,
            userSeq,
            0,
            10
          );
          console.log(newMovies)
          setMovies((prevMovies) => [...prevMovies, ...newMovies]);
          if (newMovies.length < 20) {
            setHasMore(false); // 더 이상 로드할 데이터가 없으면 hasMore를 false로 설정
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
    setPage((prevPage) => prevPage + 1);
  };

  return (
    <div className="flex flex-col bg-black h-screen text-white overflow-y-auto">
      <header className="sticky top-0 bg-transparent z-10">
        <Navbar />
      </header>

      <div className="mt-[100px] flex justify-end items-end w-[1800px]">
        <SearchBar
          initialSearchQuery={searchQuery}
          isExpanded={false}
          setIsExpanded={() => {}}
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
