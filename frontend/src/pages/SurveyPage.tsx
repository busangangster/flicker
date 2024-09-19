import React, { useState } from "react";
import "../App.css";
const movies = Array.from({ length: 30 }, (_, i) => ({
  id: i,
  title: `Movie ${i + 1}`,
  image: "https://via.placeholder.com/135x177", // 예시 이미지 URL 사용
  //   image: "/src/assets/avengers1.jpg", // 예시 이미지 URL 사용
}));

const Survey: React.FC = () => {
  const [selectedMovies, setSelectedMovies] = useState<number[]>([]);

  const handleSelect = (id: number) => {
    if (selectedMovies.includes(id)) {
      setSelectedMovies(selectedMovies.filter((movieId) => movieId !== id));
    } else if (selectedMovies.length < 3) {
      setSelectedMovies([...selectedMovies, id]);
    }
  };

  return (
    <div className="flex p-8 h-screen bg-black justify-center w-screen">
      <div className="flex flex-col text-white">
        <div className="text-[70px] font-bold mb-4 mt-[40px]">Flicker</div>
        <h1 className="text-[50px] font-bold mb-4 leading-[75px] mt-[20px] ">
          닉네임님,
          <br />
          좋아하는 영화를 <br />
          3개 선택하세요 !
        </h1>
        <p className="mb-8">
          현정님의 취향에 꼭 맞는 영화를 추천하는데 도움이 돼요.
        </p>
        <button
          className={`px-4 py-2 w-[240px] h-[50px] bg-blue-500 text-white rounded-[10px] ${
            selectedMovies.length < 3 ? "opacity-50" : ""
          }`}
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
                className={`w-full h-full rounded-[5px]  shadow-md transition ${
                  selectedMovies.includes(movie.id) ? "blur-[2px]" : ""
                }`}
              />
              {selectedMovies.includes(movie.id) && (
                <div className="absolute inset-0 flex items-center justify-center">
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    className="h-12 w-12 text-blue-500"
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
                  </svg>{" "}
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
