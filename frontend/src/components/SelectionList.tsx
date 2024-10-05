// SelectionList.tsx
import React, { useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { SelectionListProps } from "../type";

const SelectionList: React.FC<SelectionListProps> = ({
  movies,
  loadMoreMovies,
  hasMore,
}) => {
  const navigate = useNavigate();
  const observerRef = useRef<HTMLDivElement | null>(null);

  const goToDetail = (movieSeq: number) => {
    navigate(`/moviedetail/${movieSeq}`);
  };

  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && hasMore) {
          console.log("스크롤 감지, 추가 영화 로드 시작"); // 스크롤 감지 시점 확인
          loadMoreMovies(); // 페이지 끝에 도달하면 추가 영화 로드
        }
      },
      { threshold: 1.0 }
    );

    if (observerRef.current) {
      observer.observe(observerRef.current);
    }

    return () => {
      if (observerRef.current) {
        observer.unobserve(observerRef.current);
      }
    };
  }, [hasMore, loadMoreMovies]);

  return (
    <div className="grid grid-cols-6 gap-x-5 gap-y-12 px-16 py-8 w-full">
      {movies.length > 0 ? (
        movies.map((movie) => (
          <div key={movie.movieSeq} className="w-full h-[350px]">
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
      <div ref={observerRef} /> 
    </div>
  );
};

export default SelectionList;
