import React, { useState } from "react";
import starOutline from "../assets/review/star_outline.png";
import starHalf from "../assets/review/star_half.png";
import starFull from "../assets/review/star.png";
import { ReviewType, ReviewForm as ReviewFormData } from "../type";
import { IoMdCheckboxOutline, IoMdSquareOutline } from "react-icons/io";
import { createReview } from "../apis/movieApi";
import { useUserQuery } from "../hooks/useUserQuery";
import Modal from "./common/Modal"; 
import { FaCheckCircle, FaExclamationCircle } from "react-icons/fa"; 

const ReviewForm: React.FC<{
  onSubmit: (review: ReviewType) => void;
  movieSeq: number;
}> = ({ onSubmit, movieSeq }) => {
  const [rating, setRating] = useState(0);
  const [content, setContent] = useState("");
  const [isSpoiler, setIsSpoiler] = useState(false);
  const [isFormSubmitted, setIsFormSubmitted] = useState(false);
  const [isDragging, setIsDragging] = useState(false);
  const [showModal, setShowModal] = useState<{
    isOpen: boolean;
    message: string;
    type: "success" | "error";
  } | null>(null); // 모달 상태
  const { data } = useUserQuery();

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

  // 리뷰 작성 API 호출
  const submitReviewToApi = async (reviewData: ReviewFormData) => {
    try {
      await createReview(reviewData);
      setShowModal({
        isOpen: true,
        message: "리뷰 작성이 성공적으로 완료되었습니다.",
        type: "success",
      });
    } catch (error) {
      setShowModal({
        isOpen: true,
        message: "리뷰 작성에 실패했습니다.",
        type: "error",
      });
      throw error;
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // userSeq가 undefined인 경우 폼 제출을 막음
    if (!data?.userSeq) {
      setShowModal({
        isOpen: true,
        message: "유저 정보가 없습니다.",
        type: "error",
      });
      return;
    }

    if (content.trim() === "" || rating === 0) {
      setShowModal({
        isOpen: true,
        message: "별점과 내용을 모두 입력해주세요.",
        type: "error",
      });
      return;
    }

    const newReview = {
      reviewSeq: Date.now(),
      reviewRating: rating,
      content: content,
      createdAt: new Date().toISOString(),
      spoiler: isSpoiler,
      likes: 0,
      liked: false,
      nickname: data?.nickname,
      top: false,
    };

    // API로 보낼 데이터 구조
    const reviewData: ReviewFormData = {
      userSeq: data?.userSeq || 0,
      movieSeq: movieSeq,
      reviewRating: rating,
      content: content,
      isSpoiler: isSpoiler,
    };
    try {
      await submitReviewToApi(reviewData);
      onSubmit(newReview); // 화면에 리뷰 추가
      setIsFormSubmitted(true); // 폼이 제출되면 폼을 숨김

      // 새로고침 추가
      window.location.reload(); // 페이지 새로고침
    } catch (error) {
      console.error("리뷰 제출 중 오류 발생:", error);
    }
  };

  if (isFormSubmitted) {
    return null;
  }

  return (
    <form onSubmit={handleSubmit} className="border-b border-gray-700 mb-4">
      <div className="flex items-center mb-2">
        <div className="rounded-full bg-gray-500 w-8 h-8 flex items-center justify-center text-white font-bold">
          {data?.nickname.charAt(0)}
        </div>
        <span className="ml-4 font-semibold ">{data?.nickname}</span>
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
                onDragStart={(e) => e.preventDefault()}
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
        <label
          className="text-gray-200 flex cursor-pointer"
          onClick={() => setIsSpoiler(!isSpoiler)}
        >
          <div className="mr-2">
            {isSpoiler ? (
              <IoMdCheckboxOutline className="text-gray-200" size={22} />
            ) : (
              <IoMdSquareOutline className="text-gray-200" size={22} />
            )}
          </div>
          스포일러 내용이 포함되어 있어요!
        </label>
        <button
          type="submit"
          className="ml-auto bg-[#4D7FFF] text-white px-2 py-1 rounded hover:bg-blue-700 "
        >
          작성하기
        </button>
      </div>

      {showModal?.isOpen && (
        <Modal
          onClose={() => setShowModal(null)}
          title={showModal.type === "success" ? "성공" : "실패"}
          description={showModal.message}
          icon={
            showModal.type === "success" ? FaCheckCircle : FaExclamationCircle
          }
          buttonText="확인"
        />
      )}
    </form>
  );
};

export default ReviewForm;
