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
    <div
      className="fixed inset-0 flex items-center justify-center z-50 bg-black bg-opacity-50 "
      lang="ko"
    >
      <div className="bg-[#5D5D5D] rounded-lg p-10 w-[500px] overflow-y-auto">
        <div className="flex justify-end items-end text-gray-300 text-lg hover:opacity-60">
          <button onClick={onClose}>✖</button>
        </div>
        <div className="text-[35px] font-bold mb-6">{movieTitle}</div>
        <div className="flex">
          <div
            className="flex-col text-white font-semibold self-end mb-2"
            lang="ko"
          >
            <div className="text-lg">
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
              <span className="text-gray-300 ml-14">&nbsp;{country}</span>
            </div>
          </div>
          <img
            src={moviePosterUrl}
            alt="Movie Poster"
            className="w-[100x] h-[170px] ml-24 opacity-85 mb-2 self-end rounded-sm"
          />
        </div>
        {/* <p className="text-black mt-5"></p> */}
        <hr className="mt-4" />
        <div className="bg-gray w-full h-[250px] mt-5 overflow-y-scroll custom-scrollbar2 text-white">
          {moviePlot}
        </div>
      </div>
    </div>
  );
};

export default PlotModal;
