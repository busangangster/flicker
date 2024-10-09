import React, { useState } from "react";
import "../App.css";
import { useUserQuery } from "../hooks/useUserQuery";
import { addFavoriteMovies } from "../apis/axios";
import { useNavigate } from "react-router-dom";

const movieSeqs = [
  20020, 16767, 24350, 17263, 20205, 23601, 20616, 24782, 23165, 17648, 22716,
  16864, 17898, 24602, 20403, 23024, 16281, 22427, 19771, 18193, 22626, 21560,
  26284, 24484, 25767, 25767, 22910, 26112, 20977, 21932,
];

const imagePaths = Array.from(
  { length: 30 },
  (_, i) => `/assets/survey/image${i + 1}.jpg`
);

const movies = Array.from({ length: 30 }, (_, i) => ({
  id: i,
  title: `Movie ${i + 1}`,
  image: imagePaths[i],
  movieSeq: movieSeqs[i],
}));

const Survey: React.FC = () => {
  const [selectedMovies, setSelectedMovies] = useState<number[]>([]);
  const navigate = useNavigate();
  const { data, error, isLoading } = useUserQuery();

  if (!data) return <p>유저 정보가 없습니다.</p>;
  if (isLoading) return <p>로딩 중...</p>;
  if (error) return <p>유저 정보를 불러오는데 실패했습니다.</p>;

  const handleSelect = (id: number) => {
    if (selectedMovies.includes(id)) {
      setSelectedMovies(selectedMovies.filter((movieId) => movieId !== id));
    } else if (selectedMovies.length < 3) {
      setSelectedMovies([...selectedMovies, id]);
    }
  };

  const handleSubmit = async () => {
    const selectedMovieSeqs = selectedMovies.map((id) => movieSeqs[id]);
    try {
      await addFavoriteMovies(data.userSeq, selectedMovieSeqs);
      navigate("/home");
    } catch (error) {
      console.error("Failed to submit favorite movies:", error);
    }
  };

  return (
    <div className="flex p-8 h-full bg-black justify-center w-screen">
      <div className="flex flex-col text-white">
        <div className="text-[70px] font-bold mb-4 mt-[40px]">Flicker</div>
        <h1 className="text-[50px] font-bold mb-4 leading-[75px] mt-[20px] ">
          {data.nickname}님,
          <br />
          좋아하는 영화를 <br />
          3개 선택하세요 !
        </h1>
        <p className="mb-8">
          {data.nickname}님의 취향에 꼭 맞는 영화를 추천하는데 도움이 돼요.
        </p>
        <button
          className={`px-4 py-2 w-[240px] h-[50px] bg-[#4D7FFF] text-white rounded-[10px] ${
            selectedMovies.length < 3
              ? "opacity-50"
              : "hover:bg-[#3256B0] taransition-transform duration-300 ese-in-out"
          }`}
          disabled={selectedMovies.length < 3} // 3개 선택하지 않으면 비활성화
          onClick={handleSubmit} // 선택 완료 버튼 클릭 시 서버로 전송
        >
          3개 선택 완료
        </button>
      </div>
      <div className="bg-gray w-[700px] h-[750px] ml-[50px] mt-[160px] overflow-y-scroll custom-scrollbar">
        <div className="grid grid-cols-5 gap-3 p-3">
          {movies.map((movie) => (
            <div
              key={movie.id}
              className="relative cursor-pointer"
              onClick={() => handleSelect(movie.id)}
            >
              <img
                src={movie.image}
                alt={movie.title}
                className="w-full h-full rounded-[5px] shadow-md transition"
              />
              {selectedMovies.includes(movie.id) && (
                <div className="absolute inset-0 bg-white opacity-70 flex items-center justify-center rounded-[5px]">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    className="h-16 w-16 text-blue-500"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                    strokeWidth={4}
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      d="M5 13l4 4L19 7"
                    />
                  </svg>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
};

export default Survey;
