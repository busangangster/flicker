// PhotoCardDetailPage.tsx
import React, { useState } from "react";
import { useLocation, useNavigate } from "react-router-dom"; // useNavigate import 추가
import thumbUpOutline from "../../assets/review/thumb_up_outline.png"; // 좋아요 버튼 이미지 import
import starFull from "../../assets/review/star.png"; // 채워진 별 이미지 import
import starHalf from "../../assets/review/star_half.png"; // 반쪽 별 이미지 import
import "../../css/PhotoCard.css";


const PhotoCardDetailPage: React.FC = () => {
  const [isFlipped, setIsFlipped] = useState(false);
  // const { movieSeq } = useParams<{ movieSeq: string }>(); 
  const { state } = useLocation(); 
  const navigate = useNavigate();

  // 카드를 클릭했을 때 회전시키는 함수
  const handleCardClick = () => {
    setIsFlipped(!isFlipped); // 클릭 시 상태를 변경하여 회전
  };

  // 뒤로가기 버튼 클릭 시 이전 페이지로 이동하는 함수
  const handleGoBack = () => {
    navigate(-1); // 이전 경로로 이동
  };

  return (
    <div
      className="min-h-screen flex items-center justify-center relative w-screen"
      style={{
        backgroundImage: `url(${state.backgroundUrl})`,
        backgroundSize: "cover",
        backgroundPosition: "center",
        backgroundRepeat: "no-repeat",
        backgroundAttachment: "fixed",
      }}
    >
      {/* 검정색 그라데이션 오버레이 */}
      <div className="absolute inset-0 bg-gradient-to-b from-black/80 to-white/30" />

      {/* 뒤로가기 버튼 */}
      <button
        className="absolute bottom-24 left-8 bg-white opacity-70 text-black py-2 px-4 rounded-lg shadow-md hover:bg-gray-400 font-bold italic"
        onClick={handleGoBack}
      >
        PhotoBook
      </button>

      {/* 포토카드 컨테이너 */}
      <div
        className="photo-card-container w-[450px] h-[650px] photo-card-animation"
        onClick={handleCardClick}
      >
        <div className={`photo-card ${isFlipped ? "flipped" : ""}`}>
          {/* 앞면 */}
          <div className="photo-card-front bg-white p-4 opacity-95 shadow-xl rounded-lg">
            <img
              src={state.src}
              alt="Front"
              className="w-full h-[550px] object-cover rounded-md"
            />
            <h2 className="text-right text-xl mr-2 mt-4 text-black">
              {state.createdAt.slice(0, 10)}
            </h2>
          </div>

          {/* 뒷면 */}
          <div className="photo-card-back bg-white p-10 opacity-95 shadow-xl rounded-lg">
            <h2 className="text-center text-xl italic mb-3 text-black">
              My Photo Card
            </h2>
            <hr className="mb-2 border-gray-600" />
            <p className="text-center text-sm mb-2 text-black">
              {state?.movieYear}
            </p>
            <h1 className="text-center text-2xl font-bold mb-4 text-black">
              {state?.movieTitle}
            </h1>
            <hr className="mb-6 border-gray-600" />

            {/* 별점 및 평점 */}
            <div className="flex items-center justify-center mb-6">
              {Array.from({ length: 5 }, (_, index) => (
                <img
                  key={index}
                  src={index + 1 <= state.reviewRating ? starFull : starHalf}
                  alt="Star"
                  className="w-7 h-7 ml-1"
                />
              ))}
              <span className="ml-3 text-xl text-black font-bold">
                {state.reviewRating.toFixed(1)}
              </span>
            </div>

            {/* info 섹션 */}
            <div className="mb-4">
              <h3 className="font-semibold mb-2 ml-2 text-black text-lg italic">
                info
              </h3>
              <div className="border border-gray-500 rounded-sm p-4 text-sm text-black font-semibold">
                {/* <p className="mb-1">{movieData.info}</p> */}
                <p>{state.createdAt}</p>
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
                    {state.likes}
                  </span>
                </div>
              </div>
              <div className="border border-gray-500 rounded-sm p-4 text-sm text-black font-semibold leading-relaxed">
                <p>{state.content}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default PhotoCardDetailPage;
