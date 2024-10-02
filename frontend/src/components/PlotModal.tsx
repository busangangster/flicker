import React from "react";
import { MovieDetail } from "../type";

interface PlotModalProps {
  isopen: boolean;
  onClose: () => void;
  movieDetail: MovieDetail | undefined;
}

const PlotModal: React.FC<PlotModalProps> = ({
  isopen,
  onClose,
  movieDetail,
}) => {
  if (!isopen || !movieDetail) return null;

  const {
    movieDetailResponse: {
      movieTitle,
      movieYear,
      runningTime,
      audienceRating,
      country,
      moviePosterUrl,
      moviePlot,
    },
  } = movieDetail; // movieDetailResponse에서 데이터 추출

  return (
    <div className="fixed inset-0 flex items-center justify-center z-50 bg-black bg-opacity-50 ">
      <div className="bg-[#5D5D5D] rounded-lg p-10 w-[500px] h-[75vh] overflow-y-auto">
        <div className="flex justify-end items-end mb-4 text-gray-300">
          <button onClick={onClose}>✖</button>
        </div>
        <div className="flex">
          <div className="flex-col text-white font-semibold">
            <span className="text-[35px] font-bold">{movieTitle}</span>
            <div className="mt-6 text-lg">
              <span>개봉년도</span>
              <span className="text-gray-300 ml-6">{movieYear}</span>
            </div>
            <div className="mt-4 text-lg">
              <span>상영시간</span>
              <span className="text-gray-300 ml-6">{runningTime}</span>
            </div>
            <div className="mt-4 text-lg">
              <span>관람등급</span>
              <span className="text-gray-300 ml-6">{audienceRating}</span>
            </div>
            <div className="mt-4 text-lg">
              <span>국가</span>
              <span className="text-gray-300 ml-14">{country}</span>
            </div>
          </div>
          <img
            src={moviePosterUrl}
            alt="Movie Poster"
            className="w-[100x] h-[170px] mt-16 ml-24 opacity-85 "
          />
        </div>
        {/* <p className="text-black mt-5"></p> */}
        <hr className="mt-4" />
        <div className="bg-gray w-full h-[330px] mt-5 overflow-y-scroll custom-scrollbar2 text-white pt-2">
          {moviePlot}
        </div>
      </div>
    </div>
  );
};

export default PlotModal;
