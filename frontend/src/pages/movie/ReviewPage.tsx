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
import { fetchMovieReviews } from "../../apis/axios";
import {
  checkAlreadyReview,
  deleteReview,
  getMoviePoster,
} from "../../apis/movieApi";
import { throttle } from "lodash";
import { useParams, useNavigate } from "react-router-dom";
import { IoIosArrowRoundBack } from "react-icons/io";
import { useQuery } from "@tanstack/react-query";

gsap.registerPlugin(ScrollToPlugin);

const ReviewPage: React.FC = () => {
  const { movieSeq } = useParams<{ movieSeq: string }>();
  const [reviews, setReviews] = useState<ReviewType[]>([]);
  const [sortOption, setSortOption] = useState("좋아요 많은 순");
  const { data: userData } = useUserQuery();
  const [page, setPage] = useState(0);
  const [isLoading, setIsLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);
  const [alreadyReview, setAlreadyReview] = useState<boolean | null>(null);
  const [userReview, setUserReview] = useState<ReviewType | null>(null);
  const navigate = useNavigate();

  const userSeq = userData?.userSeq || 0;
  console.log(userSeq);

  // react-query로 getMoviePoster 요청 (주어진 형식에 맞춰 수정)
  const { data: moviePosterUrl } = useQuery({
    queryKey: ["moviePoster", movieSeq],
    queryFn: () => getMoviePoster(Number(movieSeq)),
    enabled: !!movieSeq, 
  });

  useEffect(() => {
    const fetchReviewStatus = async () => {
      try {
        const response = await checkAlreadyReview(userSeq, Number(movieSeq));
        setAlreadyReview(response.alreadyReview);
        if (response.alreadyReview && response.reviewDto) {
          setUserReview(response.reviewDto);
        }
      } catch (error) {
        console.error("리뷰 확인 중 오류가 발생했습니다.", error);
      }
    };

    if (userSeq && movieSeq) {
      fetchReviewStatus();
    }
  }, [userSeq, movieSeq]);

  const handleDeleteReview = async (reviewSeq: number) => {
    try {
      await deleteReview(reviewSeq, userSeq);
      setReviews((prevReviews) =>
        prevReviews.filter((review) => review.reviewSeq !== reviewSeq)
      );
    } catch (error) {
      console.error("리뷰 삭제 중 오류 발생:", error);
    }
  };

  const loadReviews = useCallback(async () => {
    if (isLoading || !hasMore) return;

    setIsLoading(true);

    try {
      const newReviews = await fetchMovieReviews(
        Number(movieSeq),
        userSeq || 0,
        "date", // 기본적으로 날짜 순으로 불러옴
        page,
        100 // 더 큰 범위로 한 번에 불러옴
      );

      setReviews((prevReviews) => [...prevReviews, ...newReviews]);

      if (newReviews.length < 100) {
        setHasMore(false);
      }
    } catch (error) {
      console.error("Error fetching reviews:", error);
    } finally {
      setIsLoading(false);
    }
  }, [movieSeq, userSeq, page, hasMore, isLoading]);

  useEffect(() => {
    loadReviews();
  }, [page]);

  const handleScroll = useCallback(
    throttle(() => {
      const scrollableElement = document.querySelector(".scroll-container");
      if (scrollableElement) {
        if (
          scrollableElement.scrollTop + scrollableElement.clientHeight >=
            scrollableElement.scrollHeight - 20 &&
          !isLoading &&
          hasMore
        ) {
          setPage((prevPage) => prevPage + 1);
        }
      }
    }, 1000),
    [isLoading, hasMore]
  );

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

  const getSortedReviews = () => {
    const sortedReviews = [...reviews];

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
    { value: "좋아요 많은 순", label: "좋아요 많은 순" },
    { value: "최신순", label: "최신순" },
    { value: "오래된 순", label: "오래된 순" },
  ];

  const handleFilterChange = async (value: string) => {
    setSortOption(value);
    setPage(0); // 페이지 초기화
    const sortedReviews = getSortedReviews();
    setReviews(sortedReviews);
  };

  const handleAddReview = (newReview: ReviewType) => {
    newReview.isUserReview = true;
    setReviews((prev: ReviewType[]) => [newReview, ...prev]);
  };

  const scrollToTop = () => {
    const scrollableElement = document.querySelector(".scroll-container");
    if (scrollableElement) {
      gsap.to(scrollableElement, { scrollTo: { y: 0 }, duration: 0.7 });
    }
  };

  return (
    <div className="scroll-container flex flex-col bg-black h-screen overflow-y-auto text-white">
      <header className="sticky top-0 bg-transparent z-20">
        <Navbar />
        <IoIosArrowRoundBack
          onClick={() => navigate(-1)}
          className="text-gray-200 cursor-pointer fixed left-4 top-16 w-10 h-10 hover:opacity-60"
        />
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
                  defaultValue="좋아요 많은 순"
                />
              </div>
            </div>

            {alreadyReview !== null && (
              <>
                {!alreadyReview && userData && (
                  <ReviewForm
                    onSubmit={handleAddReview}
                    movieSeq={Number(movieSeq)}
                  />
                )}
                {userReview && (
                  <Review
                    key={userReview.reviewSeq}
                    review={userReview}
                    onDelete={handleDeleteReview}
                    userSeq={userSeq}
                  />
                )}
              </>
            )}

            {reviews.length > 0 ? (
              getSortedReviews().map((review) => (
                <Review
                  key={review.reviewSeq}
                  review={{ ...review, top: false }}
                  userSeq={userSeq}
                />
              ))
            ) : (
              <div>리뷰가 없습니다</div>
            )}
            {isLoading && <div>Loading...</div>}
          </div>
          <div className="w-1/4 pl-4">
            <div className="sticky top-20">
              <div className="mb-6">
                <img
                  src={moviePosterUrl || "default_poster_url"}
                  alt="Movie Poster"
                  className="w-full rounded-sm mb-4"
                />
              </div>
              <Ratings movieSeq={Number(movieSeq)} />
              <Keyword />
            </div>
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
