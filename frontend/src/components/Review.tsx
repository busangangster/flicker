import React, { useState } from "react";
import starFull from "../assets/review/star.png";
import starHalf from "../assets/review/star_half.png";
import star_outline from "../assets/review/star_outline.png";
import thumbUpOutline from "../assets/review/thumb_up_outline.png";
import thumbUp from "../assets/review/thumb_up.png";
import { ReviewProps } from "../type";
import { likeReview, cancelLikeReview } from "../apis/movieApi";

const Review: React.FC<ReviewProps> = ({ review, userSeq, onDelete }) => {
  const [showContent, setShowContent] = useState(!review.spoiler);
  const [liked, setLiked] = useState(review.liked);
  const [likes, setLikes] = useState(review.likes);
  const MAX_LENGTH = 250;
  const content = review.content || "";
  const isLongContent = content.length > MAX_LENGTH;
  const [showMore, setShowMore] = useState(false);

  const toggleContent = () => {
    if (review.spoiler) {
      setShowContent((prev) => !prev);
    }
  };

  const toggleShowMore = () => {
    setShowMore((prev) => !prev);
  };

  // 리뷰 삭제 처리
  const handleDeleteReview = async () => {
    if (onDelete) {
      const confirmDelete = window.confirm(
        "정말로 이 리뷰를 삭제하시겠습니까?"
      );
      if (confirmDelete) {
        await onDelete(review.reviewSeq);
        alert("리뷰가 삭제되었습니다.");
        window.location.reload(); // 페이지 새로고침
      }
    }
  };

  // 좋아요 버튼 클릭 시 처리
  const handleLikeToggle = async () => {
    try {
      if (liked) {
        // 좋아요 취소
        await cancelLikeReview(userSeq, review.reviewSeq);
        setLiked(false);
        setLikes((prevLikes) => prevLikes - 1);
      } else {
        // 좋아요 추가
        await likeReview(userSeq, review.reviewSeq);
        setLiked(true);
        setLikes((prevLikes) => prevLikes + 1);
      }
    } catch (error) {
      console.error("좋아요 처리 중 오류 발생:", error);
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
          <div className="flex items-center justify-between mb-1">
            <div className="flex items-center">
              {/* 닉네임 및 별점 */}
              <span className="font-semibold">{review.nickname}</span>
              <span className="text-gray-400 text-sm ml-2">
                's flick record is
              </span>
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

            <div className="flex items-center">
              {/* 본인 리뷰에만 삭제 버튼 추가 */}
              {onDelete && (
                <button
                  onClick={handleDeleteReview}
                  className="mr-3 text-white bg-[#44547B] px-2 py-1 rounded-md hover:bg-gray-700 text-sm border-none cursor-pointer"
                >
                  삭제
                </button>
              )}
              {/* 좋아요 버튼 */}
              <button
                className="flex items-center p-0 bg-transparent border-none outline-none"
                onClick={handleLikeToggle}
              >
                <img
                  src={liked ? thumbUp : thumbUpOutline}
                  alt="Thumb Up"
                  className="w-4 h-4 mr-1"
                />
                <span className=" text-gray-300">{likes}</span>
              </button>
            </div>
          </div>

          {showContent ? (
            <>
              <p className="text-white mt-1 mb-1" onClick={toggleContent}>
                {showMore || !isLongContent
                  ? review.content
                  : review.content.slice(0, MAX_LENGTH)}
                {isLongContent && (
                  <button
                    className="text-gray-400 ml-2 underline"
                    onClick={toggleShowMore}
                  >
                    {showMore ? "접기" : "더보기"}
                  </button>
                )}
              </p>
            </>
          ) : (
            <p
              className="text-gray-400 cursor-pointer mt-1 mb-1"
              onClick={toggleContent}
            >
              스포일러 내용이 포함되어 있어요! 클릭하면, 내용을 볼 수 있어요.
            </p>
          )}
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
