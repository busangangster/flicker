// Review.tsx
import React from "react";
import starFull from "../assets/review/star.png";
import starHalf from "../assets/review/star_half.png";
import star_outline from "../assets/review/star_outline.png";
import thumbUpOutline from "../assets/review/thumb_up_outline.png";
import thumbUp from "../assets/review/thumb_up.png";
import { ReviewType } from "../type";

interface ReviewProps {
  review: ReviewType;
  onLikeToggle: (reviewSeq: number) => void; // 좋아요 토글 함수
  liked: boolean;
  likes: number;
  nickname: string;
}

const Review: React.FC<ReviewProps> = ({ review, onLikeToggle }) => {
  const [showContent, setShowContent] = React.useState(!review.isSpoiler); // 스포일러 여부에 따른 내용 표시 여부

  const toggleContent = () => {
    if (review.isSpoiler) {
      setShowContent((prev) => !prev);
    }
  };

  return (
    <div key={review.reviewSeq} className="border-b border-gray-700 mb-2">
      <div className="flex items-start mt-5">
        {/* 프로필 */}
        <div className="rounded-full bg-gray-500 w-8 h-8 flex items-center justify-center text-white font-bold">
          {review.nickname.charAt(0)}
        </div>
        <div className="ml-4 flex flex-col w-full">
          <div className="flex items-center justify-between mb-1 ">
            <div className="flex items-center">
              {/* 닉네임 및 별점 */}
              <span className="font-semibold">{review.nickname}</span>
              <span className="text-gray-400 text-sm ml-2">
                's flick record is
              </span>
              {/* 별점 표시 */}
              <span className="ml-2 text-[#4D7FFF] flex items-center">
                {Array.from(
                  { length: Math.floor(review.reviewRating) },
                  (_, index) => (
                    <img
                      key={index}
                      src={starFull}
                      alt="Full Star"
                      className="w-5 h-5"
                    />
                  )
                )}
                {review.reviewRating % 1 !== 0 && (
                  <img src={starHalf} alt="Half Star" className="w-5 h-5" />
                )}
                {Array.from({ length: 5 - review.reviewRating }, (_, index) => (
                  <img
                    key={index}
                    src={star_outline}
                    alt="empty Star"
                    className="w-5 h-5"
                  />
                ))}
                <span className="ml-2 text-white font-bold">
                  {review.reviewRating}
                </span>
              </span>
            </div>
            {/* 좋아요 버튼 */}
            <button
              className="flex items-center p-0 bg-transparent border-none outline-none"
              onClick={() => onLikeToggle(review.reviewSeq)} // 좋아요 토글 함수 호출
            >
              <img
                src={review.liked ? thumbUp : thumbUpOutline}
                alt="Thumb Up"
                className="w-4 h-4 mr-1"
              />
              <span className=" text-gray-300">{review.likes}</span>
            </button>
          </div>
          {/* 리뷰 내용 표시 */}
          {showContent ? (
            <p className="text-white mt-1 mb-1" onClick={toggleContent}>
              {review.content}
            </p>
          ) : (
            <p
              className="text-gray-400 cursor-pointer mt-1 mb-1"
              onClick={toggleContent}
            >
              스포일러 내용이 포함되어 있어요! 클릭하면, 내용을 볼 수 있어요.
            </p>
          )}
          {/* 작성 날짜 표시 */}
          <span className="text-gray-400 text-sm self-end mb-1">
            {new Date(review.createdAt).toLocaleDateString("ko-KR", {
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
