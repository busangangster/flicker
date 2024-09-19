import React, { useState } from "react";
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import { Swiper, SwiperSlide } from "swiper/react";
import Navbar from "../../components/common/Navbar";

const MovieDetailPage: React.FC = () => {
  const actors = [
    "Tom Cruise",
    "Miles Teller",
    "Monica Barbaro",
    "Lewis Pullman",
    "Jay Eills",
    "Danny Ramirez",
    "Glem Powell",
    "Kara Wang",
    "Greg Tarzan Davis",
    "Bashir Salahuddin",
    "Jon Hamm",
    "Tom Cruise",
    "Miles Teller",
    "Monica Barbaro",
    "Lewis Pullman",
    "Jay Eills",
    "Danny Ramirez",
  ];

  const movieImg = [
    "https://via.placeholder.com/200x300?text=Movie+2",
    "https://via.placeholder.com/200x300?text=Movie+2",
    "https://via.placeholder.com/200x300?text=Movie+2",
    "https://via.placeholder.com/200x300?text=Movie+2",
    "https://via.placeholder.com/200x300?text=Movie+2",
    "https://via.placeholder.com/200x300?text=Movie+2",
    "https://via.placeholder.com/200x300?text=Movie+2",
    "https://via.placeholder.com/200x300?text=Movie+2",
    "https://via.placeholder.com/200x300?text=Movie+2",
    "https://via.placeholder.com/200x300?text=Movie+2",
    "https://via.placeholder.com/200x300?text=Movie+2",
    "https://via.placeholder.com/200x300?text=Movie+2",
  ];

  const [isFilled, setIsFilled] = useState(false);

  const toggleHeart = () => {
    setIsFilled(!isFilled);
  };

  return (
    <div className="flex flex-col bg-black h-screen overflow-y-auto">
      <div className="relative h-auto">
        {/* 배경 이미지 영역 */}
        <div
          className="absolute inset-0 h-[650px] w-full bg-cover bg-center"
          style={{ backgroundImage: "url('src/assets/topgun-bg.png')" }}
        >
          <div className="absolute inset-0 bg-black opacity-70"></div>
        </div>
        {/* Header with Navbar */}
        <header className="sticky top-0 bg-transparent z-10">
          <Navbar />
        </header>
        {/* Top section */}
        <div className="relative flex items-end text-white p-3 w-[1100px] h-[480px] bg-transparent ml-[50px] mt-[70px] overflow-hidden">
          {/* Left Section: Movie Poster and Details */}
          <div className="flex flex-col lg:flex-row">
            <img
              src="src/assets/topgun-poster.jpg"
              alt="Movie Poster"
              className="w-[270px] h-[410px] shadow-md border"
            />
            <div className="mt-4 ml-[60px] flex-1">
              <div className="flex">
                <h2 className="text-4xl font-bold flex items-center">
                  탑건: 매버릭
                  <span className="flex items-end ml-4">
                    <span className="text-blue-500 text-2xl">⭐</span>
                    <span className="text-2xl">4.1</span>
                  </span>
                </h2>
                <div className="flex items-end ml-[400px]">
                  <svg
                    className={`w-6 h-6 cursor-pointer ${
                      isFilled
                        ? "fill-red-500"
                        : "fill-none stroke-red-500 stroke-[2px]"
                    }`}
                    viewBox="0 0 24 24"
                    xmlns="http://www.w3.org/2000/svg"
                    onClick={toggleHeart}
                  >
                    <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" />
                  </svg>
                  <button className="text-white text-3xl ml-2">⋮</button>
                </div>
              </div>

              <p className="mt-6 text-lg">
                최고의 파일럿이자 전설적인 인물 매버릭(톰 크루즈)은 자신이
                졸업한 훈련학교 교관으로 발탁된다. 그의 명성을 모르던 팀원들은
                매버릭의 지시를 무시하지만 실전을 방불케 하는 상공 훈련에서
                눈으로 봐도 믿기 힘든 전설적인 조종 실력에 모두가 압도된다.
                매버릭의 지휘아래 견고한 팀워크를 쌓아가던 팀원들에게 국경을
                뛰어넘는 위험한 임무가 주어지자 매버릭은 자신이 가르친 동료들과
                함께 마지막이 될 지 모를 하늘 위 비행에 나서는데…
              </p>

              <div className="flex w-full h-[40px] bg-transparent border-b border-opacity-50 border-white text-white justify-start items-center space-x-4 mt-4">
                <div className="font-bold text-[18px]">배우</div>
                <div className="font-bold text-[18px]">감독</div>
                <div className="font-bold text-[18px]">장르</div>
              </div>

              <div className="flex flex-wrap gap-2 mt-6">
                {actors.map((actor, index) => (
                  <span
                    key={index}
                    className="px-3 py-1 relative text-[15px] rounded-[5px] text-white z-10"
                  >
                    <span className="absolute inset-0 bg-black opacity-70 rounded-[5px] z-[-1]"></span>
                    {actor}
                  </span>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="flex">
        {/* 리뷰 섹션 */}
        <div className="p-4 bg-black text-black rounded-md w-[800px] h-[400px] mt-8 ml-[150px] border-b border-white">
          <div className="flex w-[800px] justify-between">
            <h3 className="text-2xl font-bold text-white">Reviews</h3>
            <div className="text-white flex ml-auto items-end">more</div>
          </div>
          <div className="mt-4 space-y-4 text-white text-[14px]">
            <div className="flex items-start space-x-2">
              <div className="bg-gray-800 w-8 h-8 flex items-center justify-center rounded-full">
                <span className="text-white font-bold">B</span>
              </div>
              <p className="flex-1 ">
                <strong>busangangstar</strong> • 5.0 ⭐️
                <br />
                탑건1(1986년)의 36년만의 나온 속편. 매우 만족 스러웠고 매우
                재밌었다 무조건 특별관에서 봐야되는 영화 2022년 개봉작
                영화중에서 범죄도시2 이후 2번째로 엄청 좋았던 영화 톰 크루즈
                미모는 여전히 잘생겼다 1편을 보고 가야되는 질문에서 답을 하자면
                1편 보고 가는게 더 좋다 감동도 2배 더 느낄 수 있음
              </p>
            </div>
            {/* 다른 리뷰 추가 */}
            <div className="flex items-start space-x-2">
              <div className="bg-gray-800 w-8 h-8 flex items-center justify-center rounded-full">
                <span className="text-white font-bold">J</span>
              </div>
              <p className="flex-1">
                <strong>jaechan</strong> • 4.5 ⭐️
                <br />
                영화관에서 영화관에서 탑건 보고 집에 가려고 차 핸들 잡는데 F-18
                탄것도 아니면서 쓸데없이 비장해짐 보고 집에 가려고 할 때 듣는
                느낌...
              </p>
            </div>
            <div className="flex items-start space-x-2">
              <div className="bg-gray-800 w-8 h-8 flex items-center justify-center rounded-full">
                <span className="text-white font-bold">B</span>
              </div>
              <p className="flex-1 ">
                <strong>busangangstar</strong> • 5.0 ⭐️
                <br />
                탑건1(1986년)의 36년만의 나온 속편. 매우 만족 스러웠고 매우
                재밌었다 무조건 특별관에서 봐야되는 영화 2022년 개봉작
                영화중에서 범죄도시2 이후 2번째로 엄청 좋았던 영화 톰 크루즈
                미모는 여전히 잘생겼다 1편을 보고 가야되는 질문에서 답을 하자면
                1편 보고 가는게 더 좋다 감동도 2배 더 느낄 수 있음
              </p>
            </div>
          </div>
        </div>

        {/* 트레일러 섹션 */}
        <div className="w-[700px] bg-black text-white flex justify-center items-center m-4 p-4 h-[400px] ml-[50px] mt-[50px]">
          {/* 부모 요소에 높이 설정 */}
          <div className="relative w-full max-w-4xl h-full">
            <iframe
              src="https://www.youtube.com/embed/qSqVVswa420"
              title="YouTube video player"
              className="w-full h-full rounded-lg shadow-md"
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
              allowFullScreen
            ></iframe>
            <button className="absolute inset-0 flex items-center justify-center text-white pointer-events-none">
              <svg className="w-12 h-12" />
            </button>
            <div className="absolute top-0 left-0 bg-black bg-opacity-50 text-white px-3 py-1 rounded-br-lg">
              절찬 상영중
            </div>
          </div>
        </div>
      </div>
      <div className="h-[300px] w-[1700px] flex-shrink-0 mb-[50px] ml-[80px] mt-[20px]">
        <h2 className="text-[24px] text-white flex mb-[20px]">
          탑건: 매버릭과 유사한 장르 작품들
        </h2>
        <Swiper
          slidesPerView={8}
          // spaceBetween={}
          // slidesOffsetBefore={50} // 시작할 때 한 칸 걸쳐 보이도록 설정
          navigation
          pagination={{ clickable: true }}
          // style={{ padding: "0 0" }} // 슬라이더 여백 조정
        >
          {movieImg.map((img, index) => (
            <SwiperSlide
              key={index}
              className="flex justify-center items-center"
            >
              <img
                src={img}
                alt={`Movie ${index + 1}`}
                className="rounded-lg shadow-md"
              />
            </SwiperSlide>
          ))}
        </Swiper>
      </div>
    </div>
  );
};

export default MovieDetailPage;
