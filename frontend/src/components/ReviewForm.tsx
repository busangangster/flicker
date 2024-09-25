import React, { useState } from "react";
import starOutline from "../assets/review/star_outline.png";
import starHalf from "../assets/review/star_half.png";
import starFull from "../assets/review/star.png";
import { ReviewType } from "../type";
import { IoMdCheckboxOutline, IoMdSquareOutline } from "react-icons/io";

// 현재 로그인한 유저의 닉네임 (임시로 처리)
const currentUserNickname = "HyunJeong";

const ReviewForm: React.FC<{ onSubmit: (review: ReviewType) => void }> = ({
  onSubmit,
}) => {
  const [rating, setRating] = useState(0);
  const [content, setContent] = useState("");
  const [isSpoiler, setIsSpoiler] = useState(false);
  const [isFormSubmitted, setIsFormSubmitted] = useState(false); // 폼 제출 여부 상태 관리
  const [isDragging, setIsDragging] = useState(false); // 드래그 상태 관리

  const handleRatingChange = (index: number, isLeftHalf: boolean) => {
    const newRating = isLeftHalf ? index + 0.5 : index + 1;
    setRating(newRating);
  };

  const handleMouseDown = () => {
    setIsDragging(true);
  };

  const handleMouseUp = () => {
    setIsDragging(false);
    if (rating === 0.5) {
      setRating(0);
    }
  };

  const handleMouseMove = (
    index: number,
    event: React.MouseEvent<HTMLImageElement, MouseEvent>
  ) => {
    if (isDragging) {
      const starElement = event.currentTarget.getBoundingClientRect();
      const mouseX = event.clientX;

      if (index === 0 && mouseX < starElement.left) {
        setRating(0);
      } else {
        const isLeftHalf =
          event.nativeEvent.offsetX < event.currentTarget.width / 2;
        handleRatingChange(index, isLeftHalf);
      }
    }
  };

  const handleMouseLeave = () => {
    if (isDragging) {
      setIsDragging(false);
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (content.trim() === "" || rating === 0) {
      alert("별점과 내용을 모두 입력해주세요.");
      return;
    }
    const newReview = {
      reviewSeq: Date.now(),
      userSeq: 999,
      movieId: 101,
      reviewRating: rating,
      content: content,
      createdAt: new Date().toISOString(),
      isSpoiler: isSpoiler,
      likes: 0,
      liked: false,
      nickname: currentUserNickname,
    };
    onSubmit(newReview);
    setIsFormSubmitted(true); // 폼이 제출되면 폼을 숨김
  };

  if (isFormSubmitted) {
    return null;
  }

  return (
    <form onSubmit={handleSubmit} className="border-b border-gray-700 mb-4">
      <div className="flex items-center mb-2">
        <div className="rounded-full bg-gray-500 w-8 h-8 flex items-center justify-center text-white font-bold">
          {currentUserNickname.charAt(0)}
        </div>
        <span className="ml-4 font-semibold ">{currentUserNickname}</span>
        <span className="text-gray-400 text-sm ml-2">'s flick record is</span>
        <div className="flex ml-2" onMouseLeave={handleMouseLeave}>
          {Array.from({ length: 5 }, (_, index) => (
            <div
              key={index}
              className="relative w-5 h-5"
              style={{ marginRight: 0 }}
            >
              <img
                src={
                  rating > index
                    ? rating === index + 0.5
                      ? starHalf
                      : starFull
                    : starOutline
                }
                alt="Star"
                className="w-5 h-5 cursor-pointer"
                onDragStart={(e) => e.preventDefault()} // 드래그 금지
                onMouseDown={handleMouseDown}
                onMouseUp={handleMouseUp}
                onMouseMove={(e) => handleMouseMove(index, e)}
                onClick={(e) =>
                  handleRatingChange(
                    index,
                    e.nativeEvent.offsetX < e.currentTarget.width / 2
                  )
                }
              />
            </div>
          ))}
          <span className="ml-2 text-white font-bold">{rating.toFixed(1)}</span>
        </div>
      </div>
      <textarea
        value={content}
        onChange={(e) => setContent(e.target.value)}
        placeholder="리뷰 내용을 입력하세요."
        className="p-2 w-full border border-white rounded-sm bg-black text-white mb-1 mt-4"
      />
      <div className="flex items-center mb-2 mt-4 ml-1">
        <div
          className="mr-2 cursor-pointer"
          onClick={() => setIsSpoiler(!isSpoiler)}
        >
          {isSpoiler ? (
            <IoMdCheckboxOutline className="text-gray-200" size={22} />
          ) : (
            <IoMdSquareOutline className="text-gray-200" size={22} />
          )}
        </div>
        <label className="text-gray-200">
          스포일러 내용이 포함되어 있어요!
        </label>
        <button
          type="submit"
          className="ml-auto bg-[#4D7FFF] text-white px-2 py-1 rounded hover:bg-blue-700 "
        >
          작성하기
        </button>
      </div>
    </form>
  );
};

export default ReviewForm;
