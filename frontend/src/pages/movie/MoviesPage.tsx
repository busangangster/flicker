import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../../components/common/Navbar";
import "../../css/MoviesPage.css";
import MoviesList from "../../components/MoviesList";
import SearchBar from "../../components/SearchBar";
import Filter from "../../components/Filter";
import TopTen from "../../components/TopTen";
import { fetchMovieCountry, fetchMovieGenre, fetchMovieYear } from "../../apis/axios";
import { Movie } from "../../type"


const MoviesPage: React.FC = () => {
  const [isExpanded, setIsExpanded] = useState(false);
  const [selectedGenre, setSelectedGenre] = useState("");
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  // 각 장르별 영화 데이터 관리
  const [fantasyMovies, setFantasyMovies] = useState<Movie[]>([]);
  const [sfMovies, setSfMovies] = useState<Movie[]>([]);
  const [romanceMovies, setRomanceMovies] = useState<Movie[]>([]);
  const [animeMovies, setAnimeMovies] = useState<Movie[]>([]);
  const [historyMovies, setHistoryMovies] = useState<Movie[]>([]);
  const [adventureMovies, setAdventureMovies] = useState<Movie[]>([]);
  const [twentyfourMovies, setTwentyfourMovies] = useState<Movie[]>([]);
  const [koreaMovies, setKoreaMovies] = useState<Movie[]>([]);

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

  // 장르별 영화 데이터를 가져오는 함수
  const fetchMoviesByGenre = async (
    genre: string,
    setMovies: React.Dispatch<React.SetStateAction<Movie[]>>
  ) => {
    setLoading(true);
    try {
      const encodedGenre = encodeURIComponent(genre);
      const response = await fetchMovieGenre(encodedGenre, 1, 15);
      // console.log(response);

      // movieSeq와 moviePosterUrl을 함께 받아옴
      const movies = response.map((movie: Movie) => ({
        movieSeq: movie.movieSeq,
        moviePosterUrl: movie.moviePosterUrl,
      }));
      setMovies(movies);
    } catch (error) {
      console.error("Error fetching movies:", error);
    } finally {
      setLoading(false);
    }
  };

  // 년도별 영화 데이터를 가져오는 함수
  const fetchMoviesByYear = async (
    year: number,
    setMovies: React.Dispatch<React.SetStateAction<Movie[]>>
  ) => {
    setLoading(true);
    try {
      const response = await fetchMovieYear(year, 1, 15);
      const movies = response.map((movie: Movie) => ({
        movieSeq: movie.movieSeq,
        moviePosterUrl: movie.moviePosterUrl,
      }));
      setMovies(movies);
    } catch (error) {
      console.error("Error fetching movies by year:", error);
    } finally {
      setLoading(false);
    }
  };

  // 국가별 영화 데이터를 가져오는 함수
  const fetchMoviesByCountry = async (
    country: string,
    setMovies: React.Dispatch<React.SetStateAction<Movie[]>>
  ) => {
    setLoading(true);
    try {
      const response = await fetchMovieCountry(country, 1, 15);
      const movies = response.map((movie: Movie) => ({
        movieSeq: movie.movieSeq,
        moviePosterUrl: movie.moviePosterUrl,
      }));
      setMovies(movies);
    } catch (error) {
      console.error("Error fetching movies by country:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    // 각 장르에 맞는 영화 데이터를 가져옴
    fetchMoviesByGenre("판타지", setFantasyMovies);
    fetchMoviesByGenre("SF", setSfMovies);
    fetchMoviesByGenre("로맨스", setRomanceMovies);
    fetchMoviesByGenre("애니", setAnimeMovies);
    fetchMoviesByGenre("역사", setHistoryMovies);
    fetchMoviesByGenre("모험", setAdventureMovies);


    fetchMoviesByYear(2024, setTwentyfourMovies);
    fetchMoviesByCountry("한국",setKoreaMovies);
  }, []);

  // 장르 선택 시 해당 페이지로 이동
  const handleGenreChange = (value: string) => {
    setSelectedGenre(value);
    navigate(`/movies/genre/${value}`);
  };

  return (
    <div className="flex flex-col bg-black h-screen overflow-y-auto">
      <header className="sticky top-0 bg-transparent z-10">
        <Navbar />
      </header>

      <div className="mt-[100px] flex justify-between items-end w-[1800px] pl-10">
        <div className=" mt-10">
          <Filter
            options={genres}
            onChange={handleGenreChange}
            defaultValue={selectedGenre || "장르"}
            customClass="grid grid-cols-3 gap-2 w-96"
          />
        </div>
        <SearchBar isExpanded={isExpanded} setIsExpanded={setIsExpanded} />
      </div>
      <TopTen />
      {loading ? (
        <p className="text-white">로딩 중...</p>
      ) : (
        <>
          {/* 장르별로 영화 목록을 렌더링 */}
          <MoviesList category="판타지 영화" movies={fantasyMovies} />
          <MoviesList category="올해 개봉한 영화" movies={twentyfourMovies} />
          <MoviesList category="SF 영화" movies={sfMovies} />
          <MoviesList
            category="한국 영화"
            movies={koreaMovies}
          />
          <MoviesList category="사탕같은 영화" movies={romanceMovies} />
          <MoviesList category="애니 좋아하는 사람 ~?" movies={animeMovies} />
          <MoviesList
            category="OO을 잊은 민족에게 미래란 없다"
            movies={historyMovies}
          />
          <MoviesList category="모험 떠나볼래 ?" movies={adventureMovies} />
        </>
      )}
    </div>
  );
};

export default MoviesPage;
