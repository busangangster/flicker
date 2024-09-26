import React, { useState, useEffect } from "react";
import Review from "../../components/Review";
import Ratings from "../../components/Ratings";
import Keyword from "../../components/Keyword";
import Filter from "../../components/Filter";
import ReviewForm from "../../components/ReviewForm";
import Navbar from "../../components/common/Navbar";
import { ReviewType } from "../../type";
import { gsap } from "gsap";
import { ScrollToPlugin } from "gsap/ScrollToPlugin";

gsap.registerPlugin(ScrollToPlugin);

// 목업 데이터
const mockReviews: ReviewType[] = [
  {
    reviewSeq: 1,
    userSeq: 1,
    movieId: 101,
    reviewRating: 4.0,
    content:
      "영화관에서 탑건 보고 집에 가려고 차 댔을 때의 기분을 그대로 느꼈어요.",
    createdAt: "2024-09-18T10:00:00",
    likes: 523,
    liked: false,
    nickname: "HyunJeong",
    isSpoiler: false,
  },
  {
    reviewSeq: 2,
    userSeq: 2,
    movieId: 102,
    reviewRating: 3.5,
    content:
      "탑건1(1986년)의 36년만의 나온 속편. 매우 만족 스러웠고 매우 재밌었다 무조건 특별관에서 봐야되는 영화 2022년 개봉작 영화중에서 범죄도시2 이후 2번째로 엄청 좋았던 영화 톰 크루즈 미모는 여전히 잘생겼다 1편을 보고 가야되는 질문에서 답을 하자면 1편 보고 가는게 더 좋다 감동도 2배 더 느낄 수 있음",
    createdAt: "2024-09-17T11:00:00",
    likes: 320,
    liked: true,
    nickname: "Jaechan",
    isSpoiler: false,
  },
  {
    reviewSeq: 3,
    userSeq: 3,
    movieId: 103,
    reviewRating: 5.0,
    content: "이 영화는 정말 최고입니다! 스토리, 연기, 모든 것이 완벽했습니다.",
    createdAt: "2024-09-16T14:30:00",
    likes: 720,
    liked: true,
    nickname: "MinSu",
    isSpoiler: true,
  },
  {
    reviewSeq: 4,
    userSeq: 4,
    movieId: 104,
    reviewRating: 2.5,
    content:
      "기대보다 아쉬웠어요. 캐릭터들이 조금 더 깊이 있었으면 좋았을 것 같아요.",
    createdAt: "2024-09-15T09:15:00",
    likes: 150,
    liked: false,
    nickname: "EunJi",
    isSpoiler: true,
  },
  {
    reviewSeq: 5,
    userSeq: 5,
    movieId: 105,
    reviewRating: 4.5,
    content: "재밌고 감동적이었어요! 영화 보는 내내 몰입해서 봤습니다.",
    createdAt: "2024-09-14T18:45:00",
    likes: 430,
    liked: true,
    nickname: "DongHoon",
    isSpoiler: false,
  },
  {
    reviewSeq: 6,
    userSeq: 6,
    movieId: 106,
    reviewRating: 3.0,
    content:
      "평범한 영화였어요. 몇몇 장면은 인상적이었지만 전체적으로는 무난했어요.",
    createdAt: "2024-09-13T12:00:00",
    likes: 210,
    liked: false,
    nickname: "HyeJin",
    isSpoiler: false,
  },
  {
    reviewSeq: 7,
    userSeq: 7,
    movieId: 107,
    reviewRating: 2.5,
    content:
      "기대보다 아쉬웠어요. 캐릭터들이 조금 더 깊이 있었으면 좋았을 것 같아요.",
    createdAt: "2024-09-15T09:15:00",
    likes: 158,
    liked: false,
    nickname: "EunJi",
    isSpoiler: false,
  },
  {
    reviewSeq: 8,
    userSeq: 8,
    movieId: 108,
    reviewRating: 4.5,
    content: "재밌고 감동적이었어요! 영화 보는 내내 몰입해서 봤습니다.",
    createdAt: "2024-09-14T18:45:00",
    likes: 350,
    liked: true,
    nickname: "Harry",
    isSpoiler: true,
  },
  {
    reviewSeq: 9,
    userSeq: 9,
    movieId: 109,
    reviewRating: 3.0,
    content:
      "평범한 영화였어요. 몇몇 장면은 인상적이었지만 전체적으로는 무난했어요.",
    createdAt: "2024-09-13T12:00:00",
    likes: 125,
    liked: false,
    nickname: "Poter",
    isSpoiler: false,
  },
];

const ReviewPage: React.FC = () => {
  const [reviews, setReviews] = useState<ReviewType[]>(mockReviews); // 초기 리뷰 데이터
  const [sortOption, setSortOption] = useState("최신순"); // 기본 정렬 옵션

  // 정렬 조건이 변경될 때마다 리뷰를 정렬
  useEffect(() => {
    const sortedReviews = [...reviews];

    // 정렬 옵션에 따른 정렬 로직
    if (sortOption === "최신순") {
      sortedReviews.sort(
        (a, b) =>
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
      );
    } else if (sortOption === "오래된 순") {
      sortedReviews.sort(
        (a, b) =>
          new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime()
      );
    } else if (sortOption === "좋아요 많은 순") {
      sortedReviews.sort((a, b) => b.likes - a.likes);
    }

    setReviews(sortedReviews); // 정렬된 리뷰 설정
  }, [sortOption]); // sortOption이 변경될 때마다 실행

  const filterOptions = [
    { value: "최신순", label: "최신순" },
    { value: "좋아요 많은 순", label: "좋아요 많은 순" },
    { value: "오래된 순", label: "오래된 순" },
  ];

  // 필터 변경 시 정렬 옵션 업데이트
  const handleFilterChange = (value: string) => {
    setSortOption(value);
  };

  // 새 리뷰 추가 핸들러
  const handleAddReview = (newReview: ReviewType) => {
    setReviews((prev: ReviewType[]) => [newReview, ...prev]);
  };

  const handleLikeToggle = (reviewSeq: number) => {
    setReviews((prevReviews) =>
      prevReviews.map((review) =>
        review.reviewSeq === reviewSeq
          ? {
              ...review,
              liked: !review.liked,
              likes: review.liked ? review.likes - 1 : review.likes + 1,
            }
          : review
      )
    );
  };

  const scrollToTop = () => {
    const scrollableElement = document.querySelector(".scroll-container"); // 스크롤 가능한 요소를 지정
    if (scrollableElement) {
      gsap.to(scrollableElement, { scrollTo: { y: 0 }, duration: 0.7 });
    }
  };

  return (
    <div className="scroll-container flex flex-col bg-black h-screen overflow-y-auto">
      <header className="sticky top-0 bg-transparent z-10">
        <Navbar />
      </header>
      <div className="flex justify-center bg-black text-white mt-[120px] ">
        {/* 양쪽 패딩을 위한 빈 공간 (1/5로 조정하여 여백을 줄임) */}
        <div className="w-1/4"></div>

        {/* 메인 콘텐츠 영역 */}
        <div className="flex w-3/5 p-4">
          {/* 리뷰 섹션 */}
          <div className="w-3/4 pr-4 border-r border-gray-700">
            <div className="flex items-center justify-between mb-4">
              <h1 className="text-3xl font-bold">Reviews</h1>
              <div className="w-40">
                <Filter
                  options={filterOptions}
                  onChange={handleFilterChange}
                  defaultValue="최신순"
                />
              </div>
            </div>

            {/* 리뷰 작성 폼 추가 */}
            <ReviewForm onSubmit={handleAddReview} />
            {/* 여러 개의 리뷰를 불러오는 부분 */}
            {reviews.map((review) => (
              <Review
                key={review.reviewSeq}
                review={review}
                liked={review.liked}
                likes={review.likes}
                nickname={review.nickname}
                onLikeToggle={handleLikeToggle}
              />
            ))}
          </div>

          {/* 오른쪽 사이드 섹션 */}
          <div className="w-1/4 pl-4">
            {/* 포스터 */}
            <div className="mb-6">
              <img
                src="assets/survey/image20.jpg"
                alt="Movie Poster"
                className="w-full rounded-sm mb-4"
              />
            </div>
            {/* Ratings */}
            <Ratings />
            {/* Word Cloud */}
            <Keyword />
          </div>
        </div>

        {/* 양쪽 패딩을 위한 빈 공간 (1/5로 조정하여 여백을 줄임) */}
        <div className="w-1/5"></div>
      </div>

      <div
        className=" material-icons bg-gray-200 text-black w-[42px] h-[42px] border rounded-[10px] cursor-pointer justify-center flex items-center fixed right-[85px] bottom-[30px] z-10"
        onClick={scrollToTop}
      >
        arrow_upward
      </div>
    </div>
  );
};

export default ReviewPage;
