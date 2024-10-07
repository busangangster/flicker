import React, { useEffect, useCallback, useState } from "react";
import { useNavigate } from "react-router-dom";
import { SelectionListProps } from "../type";
import { throttle } from "lodash"; // lodash의 throttle을 사용하여 스크롤 이벤트 최적화

const SelectionList: React.FC<SelectionListProps> = ({
  movies,
  loadMoreMovies,
  hasMore,
}) => {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false); // 로딩 상태

  const goToDetail = (movieSeq: number) => {
    navigate(`/moviedetail/${movieSeq}`);
  };

  // 스크롤 감지하여 하단에 도달하면 loadMoreMovies 호출
  const handleScroll = useCallback(
    throttle(() => {
      const scrollableElement = document.documentElement; // 스크롤이 발생하는 요소를 document로 설정
      if (
        scrollableElement.scrollTop + window.innerHeight >=
          scrollableElement.scrollHeight - 50 &&
        hasMore &&
        !isLoading
      ) {
        setIsLoading(true); // 로딩 상태로 설정
        loadMoreMovies();
        setIsLoading(false); // 로딩 종료
      }
    }, 1000),
    [isLoading, hasMore, loadMoreMovies]
  );

  // 스크롤 이벤트 추가 및 제거
  useEffect(() => {
    window.addEventListener("scroll", handleScroll);
    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, [handleScroll]);

  return (
    <div className="grid grid-cols-6 gap-x-5 gap-y-12 px-16 py-8 w-full scroll-container">
      {movies.length > 0 ? (
        movies.map((movie) => (
          <div key={movie.movieSeq} className="w-[270px] h-[350px]">
            <img
              src={movie.moviePosterUrl}
              alt={`Movie Poster ${movie.movieSeq}`}
              onClick={() => goToDetail(movie.movieSeq)}
              className="w-full h-full object-cover rounded-md photo-card-hover cursor-pointer"
            />
          </div>
        ))
      ) : (
        <p className="text-white">해당 장르의 영화가 없습니다.</p>
      )}
    </div>
  );
};

export default SelectionList;
