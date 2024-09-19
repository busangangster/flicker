// Review.tsx
import React, { useState } from "react";
import starFull from "../assets/review/star.png";
import starHalf from "../assets/review/star_half.png";
import thumbUpOutline from "../assets/review/thumb_up_outline.png";
import thumbUp from "../assets/review/thumb_up.png";

const Review: React.FC<{ review: any }> = ({ review }) => {
  const [showContent, setShowContent] = useState(!review.isSpoiler); // 스포일러 여부에 따른 내용 표시 여부

  const toggleContent = () => {
    if (review.isSpoiler) {
      setShowContent((prev) => !prev);
    }
  };

  return (
    <div key={review.review_seq} className="border-b border-gray-700 mb-2">
      <div className="flex items-start">
        {/* 프로필 */}
        <div className="rounded-full bg-gray-500 w-8 h-8 flex items-center justify-center text-white font-bold">
          {review.nickname.charAt(0)}
        </div>
        <div className="ml-4 flex flex-col w-full">
          <div className="flex items-center justify-between mb-1">
            <div className="flex items-center">
              {/* 닉네임 및 별점 */}
              <span className="font-semibold text-xs">{review.nickname}</span>
              <span className="text-gray-400 text-xs ml-2">
                's flick record is
              </span>
              {/* 별점 표시 */}
              <span className="ml-2 text-[#4D7FFF] flex items-center">
                {Array.from(
                  { length: Math.floor(review.review_rating) },
                  (_, index) => (
                    <img
                      key={index}
                      src={starFull}
                      alt="Full Star"
                      className="w-3 h-3"
                    />
                  )
                )}
                {review.review_rating % 1 !== 0 && (
                  <img src={starHalf} alt="Half Star" className="w-3 h-3" />
                )}
                <span className="ml-2 text-sm text-white font-bold">
                  {review.review_rating}
                </span>
              </span>
            </div>
            {/* 좋아요 버튼 */}
            <button
              className="flex items-center p-0 bg-transparent border-none outline-none"
              onClick={() => {
                // 좋아요 버튼 기능 추가
              }}
            >
              <img
                src={review.liked ? thumbUp : thumbUpOutline}
                alt="Thumb Up"
                className="w-3 h-3 mr-1"
              />
              <span className="text-xs text-gray-300">{review.likes}</span>
            </button>
          </div>
          {/* 리뷰 내용 표시 */}
          {showContent ? (
            <p className="text-white text-xs mt-1 mb-1">{review.content}</p>
          ) : (
            <p
              className="text-gray-400 text-xs cursor-pointer"
              onClick={toggleContent}
            >
              스포일러 내용이 포함되어 있어요! 클릭하면, 내용을 볼 수 있어요.
            </p>
          )}
          {/* 작성 날짜 표시 */}
          <span className="text-gray-400 text-[10px] self-end mb-1">
            {new Date(review.created_at).toLocaleDateString("ko-KR", {
              year: "numeric",
              month: "2-digit",
              day: "2-digit",
            })}
          </span>
        </div>
      </div>
    </div>
  );
};

export default Review;
