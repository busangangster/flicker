// ReviewForm.tsx
import React, { useState } from "react";

// 별 이미지 import
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

  const handleRatingClick = (index: number, isLeftHalf: boolean) => {
    const newRating = isLeftHalf ? index + 0.5 : index + 1;
    setRating(newRating);
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
        <span className="ml-4 font-semibold text-xs">
          {currentUserNickname}
        </span>
        <span className="text-gray-400 text-xs ml-2">'s flick record is</span>
        {/* 별점 입력 */}
        <div className="flex ml-2">
          {Array.from({ length: 5 }, (_, index) => (
            <div
              key={index}
              className="relative w-3.5 h-3.5"
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
                className="w-full h-full cursor-pointer"
                onClick={(e) =>
                  handleRatingClick(
                    index,
                    e.nativeEvent.offsetX < e.currentTarget.width / 2
                  )
                }
              />
            </div>
          ))}
          <span className="ml-2 text-sm text-white">{rating.toFixed(1)}</span>
        </div>
      </div>
      {/* 리뷰 내용 입력 */}
      <textarea
        value={content}
        onChange={(e) => setContent(e.target.value)}
        placeholder="리뷰 내용을 입력하세요."
        className="p-2 w-full border border-white rounded-sm bg-black text-xs text-white mb-1"
      />
      <div className="flex items-center mb-2">
        {/* 스포일러 체크박스 */}
        <input
          type="checkbox"
          checked={isSpoiler}
          onChange={() => setIsSpoiler(!isSpoiler)}
          className="mr-2"
        />
        <label className="text-xs text-gray-200">
          스포일러 내용이 포함되어 있어요!
        </label>
        {/* 작성 버튼 */}
        <button
          type="submit"
          className="ml-auto bg-[#4D7FFF] text-white text-xs px-2 py-1 rounded hover:bg-blue-700"
        >
          작성하기
        </button>
      </div>
    </form>
  );
};

export default ReviewForm;
