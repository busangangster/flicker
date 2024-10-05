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
import { fetchMovieDetail, fetchMovieReviews } from "../../apis/axios";
import { checkAlreadyReview, deleteReview } from "../../apis/movieApi"; // API 호출 함수 추가
import { throttle } from "lodash";
import { useParams, useNavigate } from "react-router-dom";
import { IoIosArrowRoundBack } from "react-icons/io";

gsap.registerPlugin(ScrollToPlugin);

const ReviewPage: React.FC = () => {
  const { movieSeq } = useParams<{ movieSeq: string }>(); // URL에서 movieSeq 받아오기
  const [reviews, setReviews] = useState<ReviewType[]>([]); // 서버에서 가져온 리뷰 데이터
  const [sortOption, setSortOption] = useState("최신순"); // 기본 정렬 옵션
  const { data: userData } = useUserQuery(); // 유저 정보 가져오기
  const [page, setPage] = useState(0); // 페이지 번호
  const [isLoading, setIsLoading] = useState(false); // 데이터 로딩 상태
  const [hasMore, setHasMore] = useState(true); // 더 불러올 데이터가 있는지 여부
  const [moviePosterUrl, setMoviePosterUrl] = useState<string>(""); // 영화 포스터 URL 상태 추가
  const [alreadyReview, setAlreadyReview] = useState<boolean | null>(null); // 이미 리뷰 작성 여부 상태 (null일 때는 로딩 중)
  const [userReview, setUserReview] = useState<ReviewType | null>(null); // 유저의 리뷰 저장 상태
  const navigate = useNavigate();

  const userSeq = userData?.userSeq || 0;

  useEffect(() => {
    const fetchMovieData = async () => {
      try {
        const movieDetail = await fetchMovieDetail(Number(movieSeq), userSeq); // 영화 상세 정보 API 호출
        if (movieDetail?.movieDetailResponse?.moviePosterUrl) {
          setMoviePosterUrl(movieDetail.movieDetailResponse.moviePosterUrl); // 포스터 URL 설정
        }
      } catch (error) {
        console.error("Error fetching movie details:", error);
      }
    };

    fetchMovieData(); // 영화 상세 정보를 호출해서 포스터 URL 설정
  }, [movieSeq, userSeq]);

  // 이미 작성한 리뷰가 있는지 확인하는 함수
  useEffect(() => {
    const fetchReviewStatus = async () => {
      try {
        const response = await checkAlreadyReview(userSeq, Number(movieSeq)); // API 호출
        setAlreadyReview(response.alreadyReview); // 리뷰 여부 상태 업데이트
        if (response.alreadyReview && response.reviewDto) {
          setUserReview(response.reviewDto); // 유저 리뷰 저장
        }
      } catch (error) {
        console.error("리뷰 확인 중 오류가 발생했습니다.", error);
      }
    };

    if (userSeq && movieSeq) {
      fetchReviewStatus(); // API 호출
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
  

  // 데이터를 가져오는 함수
  const loadReviews = useCallback(async () => {
    if (isLoading || !hasMore) return; // 로딩 중이거나 더 불러올 데이터가 없으면 중단

    setIsLoading(true);

    try {
      const newReviews = await fetchMovieReviews(
        Number(movieSeq),
        userSeq || 0,
        "like",
        page,
        10
      );

      // 중복된 reviewSeq가 없도록 필터링
      const uniqueNewReviews = newReviews.filter(
        (newReview: { reviewSeq: number }) =>
          !reviews.some((review) => review.reviewSeq === newReview.reviewSeq)
      );

      if (uniqueNewReviews.length > 0) {
        setReviews((prevReviews) => {
          const updatedReviews = [...prevReviews, ...uniqueNewReviews];
          return updatedReviews;
        });
      }

      // 만약 필터링된 리뷰가 10개 미만이면 더 이상 불러올 데이터가 없다고 판단
      if (uniqueNewReviews.length < 10) {
        setHasMore(false); // 불러온 데이터가 10개 미만이면 더 이상 데이터 없음
      }
    } catch (error) {
      console.error("Error fetching reviews:", error);
    } finally {
      setIsLoading(false); // 로딩 완료 후
    }
  }, [movieSeq, userSeq, page, hasMore, isLoading, reviews]);

  // 페이지 변경 시 새로운 데이터를 가져옴
  useEffect(() => {
    loadReviews(); // 초기 데이터 및 페이지 변경 시 데이터 로딩
  }, [page]);

  // 스크롤 이벤트를 감지하여 하단에 도달하면 페이지를 증가시킴
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
          setPage((prevPage) => prevPage + 1); // 페이지 증가
        }
      }
    }, 1000),
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
            onClick={() => navigate(-1)} // 뒤로가기 기능
            className="text-gray-200 cursor-pointer fixed left-4 top-16 w-10 h-10 hover:opacity-60" // 크기 및 위치 설정
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
                  defaultValue="최신순"
                />
              </div>
            </div>

            {/* API 응답 전까지는 아무것도 렌더링하지 않도록 */}
            {alreadyReview !== null && (
              <>
                {/* 이미 리뷰를 작성한 경우, 리뷰 작성 폼을 숨기고 본인 리뷰를 최상단에 배치 */}
                {!alreadyReview && userData && (
                  <ReviewForm
                    onSubmit={handleAddReview}
                    movieSeq={Number(movieSeq)}
                  />
                )}
                {userReview && (
                  <Review
                    key={userReview.reviewSeq}
                    review={userReview} // 본인 리뷰 최상단에 표시
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
                  review={{ ...review, top: false }} // review 객체로 모든 데이터를 전달
                  userSeq={userSeq}
                />
              ))
            ) : (
              <div>리뷰가 없습니다</div> // 리뷰가 없는 경우 처리
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
