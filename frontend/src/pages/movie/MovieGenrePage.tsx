import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import SelectionList from "../../components/SelectionList";
import Navbar from "../../components/common/Navbar";
import { fetchMovieGenre } from "../../apis/axios";
import { Movie } from "../../type";
import Filter from "../../components/Filter";

const MovieGenrePage: React.FC = () => {
  const { genre } = useParams<{ genre: string }>();
  const [movies, setMovies] = useState<Movie[]>([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState<number>(0);
  const [hasMore, setHasMore] = useState(true);
  const [selectedGenre, setSelectedGenre] = useState(genre || "");

  const navigate = useNavigate();

  // 초기 page 값에 따라 MAX_PAGE 설정
  const initialPage = 0;
  const MAX_PAGE = initialPage + 3;

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
    { value: "애니메이션", label: "애니메이션" },
    { value: "액션", label: "액션" },
    { value: "역사", label: "역사" },
    { value: "음악", label: "음악" },
    { value: "재난", label: "재난" },
    { value: "전쟁", label: "전쟁" },
    { value: "코미디", label: "코미디" },
    { value: "판타지", label: "판타지" },
  ];

  const loadMoreMovies = async () => {
    // 조건을 명확하게 해서 MAX_PAGE를 초과했을 때 함수를 종료
    if (loading || !hasMore || page > MAX_PAGE) return;

    setLoading(true);
    try {
      console.log(`페이지 ${page}에 대해 영화 데이터를 로드 중...`); // 페이지 로드 시점 확인

      if (genre) {
        const encodedGenre = encodeURIComponent(genre);
        const response = await fetchMovieGenre(encodedGenre, page, 30);

        setMovies((prevMovies) => {
          const allMovies = [...prevMovies, ...response];
          const uniqueMovies = allMovies.filter(
            (movie, index, self) =>
              index === self.findIndex((m) => m.movieSeq === movie.movieSeq)
          );
          return uniqueMovies;
        });

        if (response.length < 30 || page >= MAX_PAGE) {
          setHasMore(false);
        } else {
          setPage((prevPage) => prevPage + 1);
        }
      }
    } catch (error) {
      console.error("Error fetching more movies:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    setMovies([]);
    setPage(initialPage);
    setHasMore(true);
    loadMoreMovies();
  }, [genre]);

  useEffect(() => {
    const handleScroll = () => {
      if (
        window.innerHeight + window.scrollY >=
          document.documentElement.scrollHeight - 40 &&
        hasMore &&
        !loading &&
        page <= MAX_PAGE
      ) {
        loadMoreMovies();
      }
    };

    window.addEventListener("scroll", handleScroll);
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, [hasMore, loading, page]);

  const goBack = () => {
    navigate("/movies");
  };

  const handleGenreChange = (value: string) => {
    setSelectedGenre(value);
    navigate(`/movies/genre/${value}`);
  };

  return (
    <div className="flex flex-col bg-black min-h-screen">
      <header className="sticky top-0 bg-transparent z-10">
        <Navbar />
      </header>

      <div className="flex-1 overflow-y-auto">
        <div className="mt-[100px] flex justify-between items-end w-full pl-10">
          <div className="mt-3 ml-51">
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
            className="text-gray-300 text-2xl ml-16 cursor-pointer hover:underline"
            onClick={goBack}
          >
            영화
          </span>
          <span className="text-white text-4xl ml-4">&gt; {genre}</span>
        </div>

        {loading && page === initialPage ? (
          <p className="text-white ml-16">로딩 중...</p>
        ) : (
          <SelectionList
            movies={movies}
          />
        )}
      </div>
    </div>
  );
};

export default MovieGenrePage;
