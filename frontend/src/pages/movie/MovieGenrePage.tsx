import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import SelectionList from "../../components/SelectionList";
import Navbar from "../../components/common/Navbar";
import { fetchMovieGenre } from "../../apis/axios"; // 영화 장르별 데이터 호출 API
import { Movie } from "../../type";

const MovieGenrePage: React.FC = () => {
  const { genre } = useParams<{ genre: string }>();
  const [movies, setMovies] = useState<Movie[]>([]); // 영화 데이터를 저장할 상태
  const [loading, setLoading] = useState(false); // 로딩 상태 관리
  const [page, setPage] = useState<number>(Math.floor(Math.random() * 6)); // 0~5 중 랜덤으로 시작
  const [hasMore, setHasMore] = useState(true); // 추가 데이터 여부 관리
  const navigate = useNavigate();

  // 최대 5번까지만 API 호출하는 로직 추가
  const MAX_PAGE = page + 4; // 시작 페이지 +4까지만 API 호출

  const loadMoreMovies = async () => {
    if (loading || !hasMore || page > MAX_PAGE) return; // MAX_PAGE 이상이면 종료

    setLoading(true);
    try {
      console.log(`페이지 ${page}에 대해 영화 데이터를 로드 중...`); // 페이지 로드 시점 확인
      if (genre) {
        const encodedGenre = encodeURIComponent(genre);
        const response = await fetchMovieGenre(encodedGenre, page, 30); // 30개씩 데이터 로드
        console.log("가져온 영화 데이터:", response.length); // 실제로 가져온 데이터 개수 확인
        setMovies((prevMovies) => [...prevMovies, ...response]);

        // 만약 가져온 영화 데이터가 30개 미만이라면 더 이상 데이터가 없다고 판단
        if (response.length < 30 || page >= MAX_PAGE) {
          setHasMore(false);
        }

        setPage((prevPage) => prevPage + 1); // 다음 페이지로 이동
      }
    } catch (error) {
      console.error("Error fetching more movies:", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadMoreMovies(); // 초기 데이터 로드
  }, [genre]);

  const goBack = () => {
    navigate(-1);
  };

  return (
    <div className="flex flex-col bg-black h-screen overflow-y-auto">
      <header className="sticky top-0 bg-transparent z-10">
        <Navbar />
      </header>
      <div className="mt-32 mb-4">
        <span
          className="text-gray-300 text-2xl ml-16 cursor-pointer"
          onClick={goBack}
        >
          영화{" "}
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
