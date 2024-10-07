import React from "react";
import { useNavigate } from "react-router-dom";
import { SelectionListProps } from "../type";

const SelectionList: React.FC<SelectionListProps> = ({ movies }) => {
  const navigate = useNavigate();

  const goToDetail = (movieSeq: number) => {
    navigate(`/moviedetail/${movieSeq}`);
  };

  return (
    <div className="grid grid-cols-6 gap-x-5 gap-y-12 px-10 py-8 w-full">
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
    </div>
  );
};

export default SelectionList;
