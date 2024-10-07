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
  const { data: userData } = useUserQuery();
  const userSeq = userData?.userSeq;
  const [isExpanded, setIsExpanded] = useState(false);
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    const fetchMovies = async () => {
      if (!userSeq || !searchQuery) return;

      setIsLoading(true);
      try {
        // 페이지를 0으로 고정하고 한 번만 호출
        const newMovies = await fetchMoviesBySearch(
          searchQuery,
          userSeq,
          0,
          20
        );
        setMovies((preMovies) => {
          const allMovies = [...preMovies, ...newMovies];
          const uniqueMovies = allMovies.filter(
            (movie, index, self) => index === self.findIndex((m) => m.movieSeq === movie.movieSeq)
          );
          return uniqueMovies;
        });
      } catch (error) {
        console.error("Error fetching search results:", error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchMovies();
  }, [searchQuery, userSeq]);

  return (
    <div className="flex flex-col bg-black h-screen text-white overflow-y-auto">
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
