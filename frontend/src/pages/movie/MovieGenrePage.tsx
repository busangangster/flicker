import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import SelectionList from "../../components/SelectionList";
import Navbar from "../../components/common/Navbar";
import { fetchMovieGenre } from "../../apis/axios"; // 영화 장르별 데이터 호출 API
import { Movie } from "../../type";
import Filter from "../../components/Filter";

const MovieGenrePage: React.FC = () => {
  const { genre } = useParams<{ genre: string }>();
  const [movies, setMovies] = useState<Movie[]>([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState<number>(Math.floor(Math.random() * 6));
  const [hasMore, setHasMore] = useState(true);
  const [selectedGenre, setSelectedGenre] = useState(genre || ""); // 초기값으로 현재 URL에서 받은 genre 설정

  const navigate = useNavigate();

  const MAX_PAGE = page + 4;

  const genres = [
    { value: "SF", label: "SF" },
    { value: "가족", label: "가족" },
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
    { value: "판타지", label: "판타지" },
  ];

  const loadMoreMovies = async () => {
    if (loading || !hasMore || page > MAX_PAGE) return;

    setLoading(true);
    try {
      console.log(`페이지 ${page}에 대해 영화 데이터를 로드 중...`); // 페이지 로드 시점 확인

      if (genre) {
        const encodedGenre = encodeURIComponent(genre);
        const response = await fetchMovieGenre(encodedGenre, page, 30);
        console.log("가져온 영화 데이터:", response.length); // 실제로 가져온 데이터 개수 확인

        setMovies((prevMovies) => [...prevMovies, ...response]);

        if (response.length < 30 || page >= MAX_PAGE) {
          setHasMore(false);
        }
        setPage((prevPage) => prevPage + 1);
      }
    } catch (error) {
      console.error("Error fetching more movies:", error);
    } finally {
      setLoading(false);
    }
  };

  // selectedGenre 대신 genre를 의존성 배열에 추가
  useEffect(() => {
    setMovies([]); // 새로운 장르 선택 시 영화 리스트 초기화
    setPage(Math.floor(Math.random() * 6)); // 새로운 장르로 페이지 초기화
    loadMoreMovies();
  }, [genre]); // genre가 변경될 때마다 데이터 재로드

  const goBack = () => {
    navigate("/movies");
  };

  const handleGenreChange = (value: string) => {
    setSelectedGenre(value); // 선택한 장르 업데이트
    navigate(`/movies/genre/${value}`); // URL 변경
  };

  return (
    <div className="flex flex-col bg-black h-screen overflow-y-auto">
      <header className="sticky top-0 bg-transparent z-10">
        <Navbar />
      </header>

      <div className="mt-[100px] flex justify-between items-end w-[1800px] pl-10 ml-5">
        <div className=" mt-10">
          <Filter
            options={genres}
            onChange={handleGenreChange}
            defaultValue={selectedGenre || "장르"}
            customClass="grid grid-cols-3 gap-2 w-96"
          />
        </div>
      </div>
      <div className="mt-10 mb-4">
        <span
          className="text-gray-300 text-2xl ml-16 cursor-pointer"
          onClick={goBack}
        >
          영화
        </span>
        <span className="text-white text-4xl ml-4">&gt; {genre}</span>
      </div>
      {loading && page === 1 ? (
        <p className="text-white ml-16">로딩 중...</p>
      ) : (
        <SelectionList
          movies={movies}
          loadMoreMovies={loadMoreMovies}
          hasMore={hasMore}
        />
      )}
    </div>
  );
};

export default MovieGenrePage;
