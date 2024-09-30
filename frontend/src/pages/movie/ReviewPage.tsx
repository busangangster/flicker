import React, { useState, useEffect, useCallback } from "react";
import Review from "../../components/Review";
import Ratings from "../../components/Ratings";
import Keyword from "../../components/Keyword";
import Filter from "../../components/Filter";
import ReviewForm from "../../components/ReviewForm";
import Navbar from "../../components/common/Navbar";
import { ReviewType } from "../../type";
import { useUserQuery } from "../../hooks/useUserQuery";
import { gsap } from "gsap";
import { ScrollToPlugin } from "gsap/ScrollToPlugin";
import { fetchMovieReviews } from "../../apis/axios"; // 서버 API 호출 함수
import { throttle } from "lodash"; // lodash에서 throttle 가져오기

gsap.registerPlugin(ScrollToPlugin);

const ReviewPage: React.FC = () => {
  const [reviews, setReviews] = useState<ReviewType[]>([]); // 서버에서 가져온 리뷰 데이터
  const [sortOption, setSortOption] = useState("최신순"); // 기본 정렬 옵션
  const { data } = useUserQuery(); // 유저 정보 가져오기
  const [page, setPage] = useState(0); // 페이지 번호
  const [isLoading, setIsLoading] = useState(false); // 데이터 로딩 상태
  const [hasMore, setHasMore] = useState(true); // 더 불러올 데이터가 있는지 여부

  // isLoading 상태가 변경될 때마다 로그 출력
  useEffect(() => {
    console.log("isLoading 상태 변경:", isLoading);
  }, [isLoading]);

  // 데이터를 가져오는 함수
  const loadReviews = useCallback(async () => {
    if (isLoading || !hasMore) return; // 로딩 중이거나 더 불러올 데이터가 없으면 중단

    setIsLoading(true); // 로딩 시작

    try {
      console.log("Fetching reviews for page:", page);
      const newReviews = await fetchMovieReviews(
        23428,
        181368,
        "like",
        page,
        10
      );
      console.log("Fetched reviews:", newReviews);

      // 중복된 reviewSeq가 없도록 필터링
      const uniqueNewReviews = newReviews.filter(
        (newReview) =>
          !reviews.some((review) => review.reviewSeq === newReview.reviewSeq)
      );

      if (uniqueNewReviews.length > 0) {
        setReviews((prevReviews) => {
          const updatedReviews = [...prevReviews, ...uniqueNewReviews];
          console.log("Updated reviews:", updatedReviews);
          return updatedReviews;
        });
      }

      if (uniqueNewReviews.length < 10) {
        setHasMore(false); // 불러온 데이터가 10개 미만이면 더 이상 데이터 없음
      }
    } catch (error) {
      console.error("Error fetching reviews:", error);
    } finally {
      setIsLoading(false); // 로딩 완료 후
    }
  }, [page, hasMore, isLoading, reviews]);

  // 페이지 변경 시 새로운 데이터를 가져옴
  useEffect(() => {
    loadReviews(); // 초기 데이터 및 페이지 변경 시 데이터 로딩
  }, [page]);

  // 스크롤 이벤트를 감지하여 하단에 도달하면 페이지를 증가시킴
  const handleScroll = useCallback(
    throttle(() => {
      // throttle 적용
      const scrollableElement = document.querySelector(".scroll-container");
      if (scrollableElement) {
        if (
          scrollableElement.scrollTop + scrollableElement.clientHeight >=
            scrollableElement.scrollHeight - 100 &&
          !isLoading &&
          hasMore
        ) {
          console.log("Bottom of container reached, loading next page");
          setPage((prevPage) => prevPage + 1); // 페이지 증가
        }
      }
    }, 1000), // 1초에 한 번만 실행되도록 제한
    [isLoading, hasMore]
  );

  // 스크롤 이벤트 추가 및 제거
  useEffect(() => {
    const scrollableElement = document.querySelector(".scroll-container");
    if (scrollableElement) {
      scrollableElement.addEventListener("scroll", handleScroll);
    }

    return () => {
      if (scrollableElement) {
        scrollableElement.removeEventListener("scroll", handleScroll);
      }
    };
  }, [handleScroll]);

  // 리뷰 데이터를 정렬하는 함수
  const getSortedReviews = () => {
    const sortedReviews = [...reviews];
    console.log("Sorted reviews before rendering:", sortedReviews); // 디버깅 로그 추가

    if (sortOption === "최신순") {
      return sortedReviews.sort(
        (a, b) =>
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
      );
    } else if (sortOption === "오래된 순") {
      return sortedReviews.sort(
        (a, b) =>
          new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime()
      );
    } else if (sortOption === "좋아요 많은 순") {
      return sortedReviews.sort((a, b) => b.likes - a.likes);
    }

    return sortedReviews;
  };

  const filterOptions = [
    { value: "최신순", label: "최신순" },
    { value: "좋아요 많은 순", label: "좋아요 많은 순" },
    { value: "오래된 순", label: "오래된 순" },
  ];

  const handleFilterChange = (value: string) => {
    setSortOption(value);
  };

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
    const scrollableElement = document.querySelector(".scroll-container");
    if (scrollableElement) {
      gsap.to(scrollableElement, { scrollTo: { y: 0 }, duration: 0.7 });
    }
  };

  return (
    <div className="scroll-container flex flex-col bg-black h-screen overflow-y-auto text-white">
      <header className="sticky top-0 bg-transparent z-10">
        <Navbar />
      </header>
      <div className="flex justify-center mt-[120px] ">
        <div className="w-1/4"></div>
        <div className="flex w-3/5 p-4">
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
            {data ? <ReviewForm onSubmit={handleAddReview} /> : ""}
            {reviews.length > 0 ? (
              getSortedReviews().map((review) => (
                <Review
                  key={review.reviewSeq}
                  review={review}
                  liked={review.liked}
                  likes={review.likes}
                  nickname={review.nickname}
                  onLikeToggle={handleLikeToggle}
                />
              ))
            ) : (
              <div>리뷰가 없습니다</div> // 리뷰가 없는 경우 처리
            )}
            {isLoading && <div>Loading...</div>} {/* 로딩 중일 때 표시 */}
          </div>
          <div className="w-1/4 pl-4">
            <div className="mb-6">
              <img
                src="assets/survey/image20.jpg"
                alt="Movie Poster"
                className="w-full rounded-sm mb-4"
              />
            </div>
            <Ratings />
            <Keyword />
          </div>
        </div>
        <div className="w-1/5"></div>
      </div>

      <div
        className="material-symbols-outlined bg-gray-200 text-black w-[42px] h-[42px] border rounded-[10px] cursor-pointer justify-center flex items-center fixed right-[85px] bottom-[30px] z-10"
        onClick={scrollToTop}
      >
        <img src="/assets/review/arrow_upward.png" alt="arrow_upward" />
      </div>
    </div>
  );
};

export default ReviewPage;
