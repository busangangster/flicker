// PhotoCardDetailPage.tsx
import React, { useState } from "react";
import { useNavigate } from "react-router-dom"; // useNavigate import 추가
import thumbUpOutline from "../../assets/review/thumb_up_outline.png"; // 좋아요 버튼 이미지 import
import starFull from "../../assets/review/star.png"; // 채워진 별 이미지 import
import starHalf from "../../assets/review/star_half.png"; // 반쪽 별 이미지 import
import closeIcon from "../../assets/common/x.png"; // 닫기 버튼 이미지 import
import "../../css/photocard.css";

const movieData = {
  movie_title: "탑건 : 매버릭",
  movie_year: 2022,
  movie_rating: 5.0,
  background_url:
    "https://muko.kr/files/attach/images/2022/09/06/81fe88533ab019b09f731f641a1a3e42.jpg",
  review: {
    content:
      "탑건1(1986년)의 36년만의 나온 속편. 매우 만족 스럽고 매우 재밌었다 무조건 특별관에서 봐야되는 영화 2022년 개봉작 영화중에서 범죄도시2 이후 2번째로 엄청 좋았던 영화 톰 크루즈 미모는 여전히 잘생겼다 1편을 보고 가야되는 질문에서 답을 하자면 1편 보고 가는게 더 좋다 감동도 2배 더 느낄 수 있음",
    created_at: "2024.09.04",
    likes: 320,
  },
  info: "방구석 1열",
};

const PhotoCardDetailPage: React.FC = () => {
  const [isFlipped, setIsFlipped] = useState(false);
  const navigate = useNavigate();

  // 카드를 클릭했을 때 회전시키는 함수
  const handleCardClick = () => {
    setIsFlipped(!isFlipped); // 클릭 시 상태를 변경하여 회전
  };

  // 닫기 버튼 클릭 시 이전 페이지로 이동하는 함수
  const handleClose = () => {
    navigate(-1); // 이전 경로로 이동
  };

  return (
    <div
      className="min-h-screen flex items-center justify-center relative w-screen"
      style={{
        backgroundImage: `url(${movieData.background_url})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
        backgroundAttachment: "fixed",
      }}
    >
      {/* 검정색 그라데이션 오버레이 */}
      <div className="absolute inset-0 bg-gradient-to-b from-black/80 to-white/30" />

      {/* 포토카드 컨테이너 */}
      <div
        className="photo-card-container w-[450px] h-[650px] photo-card-animation"
        onClick={handleCardClick}
      >
        <div className={`photo-card ${isFlipped ? "flipped" : ""}`}>
          {/* 앞면 */}
          <div className="photo-card-front bg-white p-4 opacity-95 shadow-xl rounded-lg">
            <img
              src="/assets/survey/image9.jpg"
              alt="Front"
              className="w-full h-[550px] object-cover rounded-md"
            />
            <h2 className="text-center text-xl font-bold mt-4">
              {movieData.movie_title}
            </h2>
            <p className="text-center text-sm">{movieData.movie_year}</p>
          </div>

          {/* 뒷면 */}
          <div className="photo-card-back bg-white p-10 opacity-95 shadow-xl rounded-lg">
            {/* 닫기 버튼 */}
            <img
              src={closeIcon}
              alt="Close"
              className="absolute top-4 right-4 w-3 h-3 cursor-pointer"
              onClick={handleClose}
            />

            <h2 className="text-center text-xl italic mb-3 text-black">
              My Photo Card
            </h2>
            <hr className="mb-2 border-gray-600" />
            <p className="text-center text-sm mb-2 text-black">
              {movieData.movie_year}
            </p>
            <h1 className="text-center text-2xl font-bold mb-4 text-black">
              {movieData.movie_title}
            </h1>
            <hr className="mb-6 border-gray-600" />

            {/* 별점 및 평점 */}
            <div className="flex items-center justify-center mb-6">
              {Array.from({ length: 5 }, (_, index) => (
                <img
                  key={index}
                  src={
                    index + 1 <= movieData.movie_rating ? starFull : starHalf
                  }
                  alt="Star"
                  className="w-7 h-7 ml-1"
                />
              ))}
              <span className="ml-3 text-xl text-black font-bold">
                {movieData.movie_rating.toFixed(1)}
              </span>
            </div>

            {/* info 섹션 */}
            <div className="mb-4">
              <h3 className="font-semibold mb-2 ml-2 text-black text-lg italic">
                info
              </h3>
              <div className="border border-gray-500 rounded-sm p-4 text-sm text-black font-semibold">
                <p className="mb-1">{movieData.info}</p>
                <p>{movieData.review.created_at}</p>
              </div>
            </div>

            {/* 리뷰 섹션 */}
            <div className="mb-4">
              <div className="flex items-center justify-between">
                <h3 className="font-semibold ml-2 mb-2 text-black text-lg italic">
                  my review
                </h3>
                {/* 좋아요 섹션 */}
                <div className="flex items-center mr-1">
                  <img
                    src={thumbUpOutline}
                    alt="Thumb Up"
                    className="w-4 h-4 mr-1 cursor-pointer"
                  />
                  <span className="text-xs text-gray-600 font-semibold">
                    {movieData.review.likes}
                  </span>
                </div>
              </div>
              <div className="border border-gray-500 rounded-sm p-4 text-sm text-black font-semibold leading-relaxed">
                <p>{movieData.review.content}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PhotoCardDetailPage;
