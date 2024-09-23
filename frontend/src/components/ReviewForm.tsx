import React, { useState } from "react";
import starOutline from "../assets/review/star_outline.png";
import starHalf from "../assets/review/star_half.png";
import starFull from "../assets/review/star.png";

// 현재 로그인한 유저의 닉네임 (임시로 처리)
const currentUserNickname = "HyunJeong";

const ReviewForm: React.FC<{ onSubmit: (review: any) => void }> = ({
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

    // 드래그 상태에서 첫 번째 별의 왼쪽으로 벗어났다면 강제로 0으로 설정
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

      // 첫 번째 별의 왼쪽으로 벗어났는지 확인
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
      review_seq: Date.now(),
      member_seq: 999,
      movie_id: 101,
      review_rating: rating,
      content: content,
      created_at: new Date().toISOString(),
      likes: 0,
      liked: false,
      nickname: currentUserNickname,
      isSpoiler: isSpoiler,
    };
    onSubmit(newReview);
    setIsFormSubmitted(true); // 폼이 제출되면 폼을 숨김
  };

  // 폼이 제출된 경우 더 이상 폼을 보여주지 않음
  if (isFormSubmitted) {
    return null;
  }

  return (
    <form onSubmit={handleSubmit} className="border-b border-gray-700 mb-4">
      <div className="flex items-center mb-2">
        {/* 유저 정보 및 별점 */}
        <div className="rounded-full bg-gray-500 w-8 h-8 flex items-center justify-center text-white font-bold">
          {currentUserNickname.charAt(0)}
        </div>
        <span className="ml-4 font-semibold ">{currentUserNickname}</span>
        <span className="text-gray-400 text-sm ml-2">'s flick record is</span>
        {/* 별점 입력 */}
        <div className="flex ml-2" onMouseLeave={handleMouseLeave}>
          {Array.from({ length: 5 }, (_, index) => (
            <div
              key={index}
              className="relative w-5 h-5"
              style={{ marginRight: 0 }}
            >
              {/* 왼쪽 반쪽 클릭 */}
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
          <span className="ml-2 text-white">{rating.toFixed(1)}</span>
        </div>
      </div>
      {/* 리뷰 내용 입력 */}
      <textarea
        value={content}
        onChange={(e) => setContent(e.target.value)}
        placeholder="리뷰 내용을 입력하세요."
        className="p-2 w-full border border-white rounded-sm bg-black text-white mb-1 mt-4"
      />
      <div className="flex items-center mb-2 mt-5">
        {/* 스포일러 체크박스 */}
        <input
          type="checkbox"
          checked={isSpoiler}
          onChange={() => setIsSpoiler(!isSpoiler)}
          className="mr-2"
        />
        <label className="text-gray-200">
          스포일러 내용이 포함되어 있어요!
        </label>
        {/* 작성 버튼 */}
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
