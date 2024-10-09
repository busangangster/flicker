import React, {useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../../components/common/Navbar";
import "../../css/MoviesPage.css";
import MoviesList from "../../components/MoviesList";
import SearchBar from "../../components/SearchBar";
import Filter from "../../components/Filter";
import TopTen from "../../components/TopTen";
import { useUserQuery } from "../../hooks/useUserQuery";
import useMoviesByGenre from "../../hooks/movie/useMovieByGenre";
import useMoviesByMonth from "../../hooks/movie/useMovieByMonth";
import useMoviesByCountry from "../../hooks/movie/useMovieByCountry";
import useMoviesByRate from "../../hooks/movie/useMovieByRate";
import useMoviesByYear from "../../hooks/movie/useMovieByYear";
import useMoviesByActor from "../../hooks/movie/useMovieByActor";

const MoviesPage: React.FC = () => {
  const [isExpanded, setIsExpanded] = useState(false);
  const [selectedGenre, setSelectedGenre] = useState("");
  const navigate = useNavigate();
  const { data: userData } = useUserQuery();
  const userSeq = userData?.userSeq;

    const getRandomPage = () => Math.floor(Math.random() * 5);


  const { data: fantasyMovies, isLoading: isFantasyLoading } = useMoviesByGenre(
    "판타지",
    getRandomPage()
  );
  const { data: sfMovies, isLoading: isSfLoading } = useMoviesByGenre("SF");
  const { data: romanceMovies, isLoading: isRomanceLoading } = useMoviesByGenre(
    "로맨스",
    getRandomPage()
  );
  const { data: animeMovies, isLoading: isAnimeLoading } = useMoviesByGenre(
    "애니",
    getRandomPage()
  );
  const { data: historyMovies, isLoading: isHistoryLoading } = useMoviesByGenre(
    "역사",
    getRandomPage()
  );
  const { data: adventureMovies, isLoading: isAdventureLoading } =
    useMoviesByGenre("모험", getRandomPage());
  const { data: newMovies, isLoading: isNewMoviesLoading } = useMoviesByMonth();
  const { data: koreaMovies, isLoading: isKoreaMoveisLoading } =
    useMoviesByCountry("한국", getRandomPage());
  const { data: twentyfourMovies, isLoading: isTwentyfourMoviesLoading } =
    useMoviesByYear(2024, getRandomPage());
  const { data: highRateMovies, isLoading: isHightRateMoviesLoading } =
    useMoviesByRate();
const { data: actorData, isLoading: isActorMoviesLoading } = useMoviesByActor(
  userSeq!
);

const actorMovies = actorData?.movies || [];
const actorName = actorData?.actorName || "";
const movieTitle = actorData?.movieTitle || "";


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
      <div className="mt-[100px] flex justify-between items-end w-full pl-10 pr-5">
        <div className=" mt-3">
          <Filter
            options={genres}
            onChange={handleGenreChange}
            defaultValue={selectedGenre || "장르"}
            customClass="grid grid-cols-3 gap-2 w-96"
          />
        </div>
        <SearchBar isExpanded={isExpanded} setIsExpanded={setIsExpanded} />
      </div>
      <TopTen />(
      <>
        {isNewMoviesLoading ? (
          <p className="text-white">이번 달 신작 영화 로딩 중...</p>
        ) : (
          <MoviesList category="이번 달 신작 영화" movies={newMovies || []} />
        )}
        {isActorMoviesLoading ? (
          <p className="text-white">관심 영화 로딩 중...</p>
        ) : userSeq && actorMovies.length > 0 ? (
          <MoviesList
            category={`재밌게 본 "${movieTitle}"에 출연한 "${actorName}"와 관련된 영화`}
            movies={actorMovies || []}
          />
        ) : null}
        {isFantasyLoading ? (
          <p className="text-white">판타지 영화 로딩 중...</p>
        ) : (
          <MoviesList category="판타지 영화" movies={fantasyMovies || []} />
        )}
        {isTwentyfourMoviesLoading ? (
          <p className="text-white">올해 개봉한 영화 로딩 중...</p>
        ) : (
          <MoviesList
            category="올해 개봉한 영화"
            movies={twentyfourMovies || []}
          />
        )}{" "}
        {isSfLoading ? (
          <p className="text-white">SF 영화 로딩 중...</p>
        ) : (
          <MoviesList category="SF 영화" movies={sfMovies || []} />
        )}
        {isKoreaMoveisLoading ? (
          <p className="text-white">한국 영화 로딩 중...</p>
        ) : (
          <MoviesList category="한국 영화" movies={koreaMovies || []} />
        )}
        {isHightRateMoviesLoading ? (
          <p className="text-white">별점 높은 영화 로딩 중...</p>
        ) : (
          <MoviesList category="별점 높은 영화" movies={highRateMovies || []} />
        )}
        {isRomanceLoading ? (
          <p className="text-white">로맨스 영화 로딩 중...</p>
        ) : (
          <MoviesList category="로맨스 영화" movies={romanceMovies || []} />
        )}
        {isAnimeLoading ? (
          <p className="text-white">애니메이션 영화 로딩 중...</p>
        ) : (
          <MoviesList category="애니메이션 영화" movies={animeMovies || []} />
        )}
        {isHistoryLoading ? (
          <p className="text-white">역사 영화 로딩 중...</p>
        ) : (
          <MoviesList category="역사 영화" movies={historyMovies || []} />
        )}
        {isAdventureLoading ? (
          <p className="text-white">모험 영화 로딩 중...</p>
        ) : (
          <MoviesList category="모험 영화" movies={adventureMovies || []} />
        )}
      </>
      )
    </div>
  );
};

export default MoviesPage;
