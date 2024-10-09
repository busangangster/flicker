import React, { useState, useEffect, useRef } from "react";
import { useParams, useNavigate, useLocation } from "react-router-dom"; // useParams 사용
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import Navbar from "../../components/common/Navbar";
import { useQuery } from "@tanstack/react-query";
import {
  addDislikeMovies,
  addfavoriteMovies,
  deleteDislikeMovies,
  deletefavoriteMovies,
  fetchMovieDetail,
} from "../../apis/axios";
import PlotModal from "../../components/PlotModal";
import { MovieDetail } from "../../type";
import Review from "../../components/Review";
import MoviesList from "../../components/MoviesList";
import { IoBan } from "react-icons/io5";
import { GoHeartFill } from "react-icons/go";
import { useUserQuery } from "../../hooks/useUserQuery";
// import Swal from "sweetalert2";
import { IoIosArrowRoundBack } from "react-icons/io";
import Modal from "../../components/common/Modal"
// import { getReviewRating } from "../../apis/movieApi"; // API 호출 함수 가져오기

const MovieDetailPage: React.FC = () => {
  const navigate = useNavigate();
  const { movieSeq } = useParams<{ movieSeq: string }>(); // URL에서 movieSeq를 가져옴
  const [isLiked, setIsLiked] = useState(false);
  const [disLiked, setDisLiked] = useState(false);
  const [isFavoriteModalOpen, setIsFavoriteModalOpen] = useState(false); // FavoriteModal 상태 추가
  const [isDislikeModalOpen, setIsDislikeModalOpen] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState("배우");
  // const [totalCnt, setTotalCnt] = useState<number>(0);
  const dropdownRef = useRef<HTMLDivElement | null>(null);
  const location = useLocation();

  const handleShowMore = (reviewSeq: number) => {
    navigate(`/review/${movieSeq}?reviewSeq=${reviewSeq}`);
  };

  const {
    data: userData,
    error: userError,
    isLoading: userIsLoading,
  } = useUserQuery();

  const userSeq = userData?.userSeq;

  const {
    data: movieData,
    error: movieError,
    isLoading: movieIsLoading,
  } = useQuery<MovieDetail, Error>({
    queryKey: ["movieDetail", movieSeq],
    queryFn: async () => {
      if (userSeq) {
        const movieDetail = await fetchMovieDetail(Number(movieSeq), userSeq); 
        return movieDetail;
      }
    },
    enabled: !!userSeq && !!movieSeq, // `userSeq`와 `movieSeq`가 존재할 때만 쿼리 실행
    // staleTime: 1000 * 60 * 5, // 5분 동안 데이터 신선하게 유지
  });

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [location.pathname]);

  // 리뷰 데이터 불러오기 - getReviewRating 호출
  // useEffect(() => {
  //   const fetchReviewRating = async () => {
  //     if (movieSeq) {
  //       try {
  //         const ratingData: RatingData = await getReviewRating(
  //           Number(movieSeq)
  //         );
  //         setTotalCnt(ratingData.data.totalCnt); // 총 리뷰 수를 상태에 저장
  //       } catch (error) {
  //         console.error("리뷰 데이터를 불러오는 데 실패했습니다.", error);
  //       }
  //     }
  //   };

  //   fetchReviewRating();
  // }, [movieSeq]);

  useEffect(() => {
    if (movieData) {
      setIsLiked(bookMarkedMovie); // 초기 상태 설정
    }
  }, [movieData]);

  useEffect(() => {
    if (movieData) {
      setDisLiked(unlikedMovie); // 초기 상태 설정
    }
  }, [movieData]);

  if (movieIsLoading) return <div>Loading...</div>;
  if (movieError) return <div>유저 정보를 불러오는데 실패했습니다.</div>;
  if (!movieData) return null; // data가 undefined일 경우를 처리

  if (!userData) return null;
  if (userIsLoading) return <p>Loading...</p>;
  if (userError) return <p>유저 정보를 불러오는데 실패했습니다.</p>;

  const {
    movieDetailResponse: {
      movieTitle,
      director,
      genre,
      // country,
      moviePlot,
      audienceRating,
      movieYear,
      runningTime,
      moviePosterUrl,
      trailerUrl,
      backgroundUrl,
      movieRating,
      actors,
    } = {}, // movieDetailResponse가 없을 경우를 대비해 기본값으로 빈 객체 설정
    bookMarkedMovie = false,
    unlikedMovie = false,
    reviews = [],
    similarMovies = [],
  } = movieData;

  // 줄거리가 없을 경우 기본 메시지 설정
  const MAX_LENGTH = 250;
  const isLongText = moviePlot && moviePlot.length > MAX_LENGTH;
  const displayedText = moviePlot
    ? moviePlot.slice(0, MAX_LENGTH)
    : "줄거리를 준비 중입니다."; // moviePlot이 없을 경우 기본 메시지

  // moviePosterUrl가 없을 경우 대체 이미지 설정
  const posterUrl = moviePosterUrl
    ? moviePosterUrl
    : "/assets/movie/noImage.png";

  const extractVideoId = (url: string) => {
    const videoIdMatch = url.match(
      /(?:\?v=|\/embed\/|\.be\/|\/v\/|\/e\/|watch\?v=|watch\?.+&v=)([^&\n?#]+)/
    );
    return videoIdMatch ? videoIdMatch[1] : null;
  };

  // trailerUrl이 없을 경우 대체 이미지 설정
  const videoUrl = trailerUrl
    ? `https://www.youtube.com/embed/${extractVideoId(trailerUrl)}`
    : "/assets/movie/noVideo.png";

  const handleCategorySelect = (category: string) => {
    setSelectedCategory(category);
  };

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const toggleHeart = async () => {
    if (!userSeq) {
      console.error("User sequence is not available."); // userSeq가 없을 때 에러 처리
      return;
    }

    setIsLiked((prev) => !prev);

    try {
      if (isLiked) {
        await deletefavoriteMovies(userSeq, Number(movieSeq));
      } else {
        await addfavoriteMovies(userSeq, Number(movieSeq));
        setIsFavoriteModalOpen(true); // FavoriteModal 열기
        // Swal.fire({
        //   title: "찜 완료!",
        //   icon: "success",
        //   confirmButtonText: "확인",
        // });
      }
    } catch (error) {
      console.error("즐겨찾기 API 호출 중 오류 발생:", error);
    }
  };

  const toggleDislike = async () => {
    if (!userSeq) {
      console.error("User sequence is not available.");
      return;
    }

    try {
      // setDisLiked와 동시에 현재 상태값을 이용해서 API 호출을 분기 처리
      setDisLiked((prevDisLiked) => {
        // 현재 상태값을 기준으로 API 호출
        if (prevDisLiked) {
          deleteDislikeMovies(userSeq, Number(movieSeq))
            .then(() => {
              console.log("관심없음 목록에서 삭제");
            })
            .catch((error) => {
              console.error("관심없음 목록 삭제 중 오류 발생:", error);
            });
        } else {
          addDislikeMovies(userSeq, Number(movieSeq))
            .then(() => {
              console.log("관심없음 목록에 추가");
              setIsDislikeModalOpen(true); // DislikeModal 열기
              // Swal.fire({
              //   title: "무관심 추가 완료!",
              //   icon: "success",
              //   confirmButtonText: "확인",
              // });
            })
            .catch((error) => {
              console.error("관심없음 목록 추가 중 오류 발생:", error);
            });
        }

        return !prevDisLiked;
      });
    } catch (error) {
      console.error("API 호출 중 오류 발생:", error);
    }
  };

  const goToReview = () => {
    navigate(`/review/${movieSeq}`);
  };

  // const toggleDropdown = () => {
  //   setIsDropdownOpen((prev) => !prev);
  // };

  // const handleOptionClick = () => {
  //   setInterestOption((prev) =>
  //     prev === "관심 없음" ? "관심 없음 취소" : "관심 없음"
  //   );
  //   setIsDropdownOpen(false);
  // };

  const renderCategoryContent = () => {
    switch (selectedCategory) {
      case "배우":
        return (
          <div className="flex flex-wrap gap-2 mt-6">
            {(actors || []).map((actor, index) => (
              <span
                key={index}
                className="relative px-3 py-1 text-[15px] rounded-[5px] text-white bg-black bg-opacity-70 z-10"
              >
                {actor.actorName}
              </span>
            ))}
          </div>
        );
      case "감독":
        return (
          <div className="flex flex-wrap gap-2 mt-6">
            <span className="relative px-3 py-1 text-[15px] rounded-[5px] text-white bg-black bg-opacity-70 z-10">
              {director}
            </span>
          </div>
        );
      case "장르":
        return (
          <div className="flex flex-wrap gap-2 mt-6">
            <span className="relative px-3 py-1 text-[15px] rounded-[5px] text-white bg-black bg-opacity-70 z-10">
              {genre}
            </span>
          </div>
        );
      default:
        return null;
    }
  };

  return (
    <div className="flex flex-col bg-black h-screen overflow-y-auto">
      {/* 영화 세부 정보 */}
      <div className="relative h-auto">
        <div
          className="absolute inset-0 h-[650px] w-full bg-cover bg-center"
          style={{
            backgroundImage: `url(${backgroundUrl})`,
          }}
        >
          <div className="absolute inset-0 bg-black opacity-70"></div>
        </div>

        {/* Header with Navbar */}
        <header className="sticky top-0 bg-transparent z-20">
          <Navbar />
          <IoIosArrowRoundBack
            onClick={() => navigate(-1)} // 뒤로가기 기능
            className="text-gray-200 cursor-pointer fixed left-4 top-16 w-10 h-10 hover:opacity-60" // 크기 및 위치 설정
          />
        </header>

        {/* Top section */}
        {/* Top section */}
        <div className="relative flex items-end text-white p-3 w-[1100px] h-[480px] bg-transparent ml-[50px] mt-[120px] overflow-hidden">
          {/* Left Section: Movie Poster and Details */}
          <div className="flex flex-col lg:flex-row w-full">
            <img
              src={posterUrl}
              alt="Movie Poster"
              className="w-[270px] h-[410px] shadow-md border"
            />
            <div className="mt-4 ml-[60px] flex-1 w-[500px]">
              {" "}
              {/* 고정된 너비 설정 */}
              <div className="flex items-center justify-between w-full">
                <h2 className="text-4xl font-bold flex-1 flex items-center overflow-hidden">
                  <span
                    className="whitespace-nowrap overflow-hidden text-ellipsis"
                    lang="ko"
                  >
                    {movieTitle}
                  </span>
                  <span className="flex items-end ml-4 flex-shrink-0">
                    <span className="text-[#E3C202] text-2xl mr-1">★</span>
                    <span className="text-2xl" lang="ko">
                      {movieRating}
                    </span>
                  </span>
                </h2>

                {/* Heart and Dislike icons */}
                <div
                  className="flex items-end ml-auto relative flex-shrink-0"
                  ref={dropdownRef}
                >
                  <svg
                    className="w-6 h-6 cursor-pointer"
                    viewBox="0 0 24 24"
                    xmlns="http://www.w3.org/2000/svg"
                    onClick={toggleHeart}
                    fill={isLiked ? "red" : "none"}
                    stroke={isLiked ? "none" : "red"}
                    strokeWidth="2"
                  >
                    <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" />
                  </svg>

                  <div className="relative group">
                    <IoBan
                      className={`w-6 h-6 ml-3 ${
                        disLiked ? "opacity-100" : "opacity-60"
                      } hover:opacity-100`}
                      onClick={toggleDislike}
                    />
                    {/* Tooltip */}
                    <div className="absolute left-1/2 transform -translate-x-3/4 mt-2 bg-gray-700 text-white text-xs rounded-md px-2 py-1 opacity-0 group-hover:opacity-100 transition-opacity duration-300 w-32 text-center">
                      {"관심 없음 목록에 추가하면 추천에서 제외됩니다."}
                    </div>
                  </div>
                </div>
              </div>
              {/* Movie details */}
              <div
                className="flex mt-4 text-white text-[16px] w-full"
                lang="ko"
              >
                <span>{movieYear}&nbsp;&nbsp;&nbsp;</span>
                {runningTime && (
                  <span>
                    | &nbsp;&nbsp;&nbsp;{runningTime}&nbsp;&nbsp;&nbsp;
                  </span>
                )}
                {audienceRating && (
                  <span>|&nbsp;&nbsp;&nbsp;{audienceRating}</span>
                )}
              </div>
              <p className="mt-4 text-lg" lang="ko">
                {displayedText}
                {isLongText && (
                  <button
                    className="text-gray-400 ml-2 hover:text-gray-500"
                    onClick={openModal}
                  >
                    더보기
                  </button>
                )}
              </p>
              <PlotModal
                isopen={isModalOpen}
                onClose={closeModal}
                movieDetail={movieData}
              />
              <div>
                <div className="flex w-full h-[40px] bg-transparent border-b border-opacity-50 border-white text-white justify-start items-center space-x-4 mt-4 cursor-pointer">
                  {["배우", "감독", "장르"].map((category) => (
                    <div
                      key={category}
                      onClick={() => handleCategorySelect(category)}
                      className={`font-bold text-[18px] mt-[10px] cursor-pointer ${
                        selectedCategory === category
                          ? "border-b-2 border-white"
                          : ""
                      }`}
                    >
                      {category}
                    </div>
                  ))}
                </div>
                {renderCategoryContent()}
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Reviews */}
      <div className="flex">
        <div className="p-1 bg-black text-black w-[800px] h-[400px] mt-[100px] ml-[150px] border-white">
          <div className="flex w-full justify-between items-center">
            <div className="text-[38px] font-bold text-white">Reviews</div>
            <button
              className="text-gray-200 flex ml-auto items-center cursor-pointer text-[16px] px-3 justify-center hover:opacity-80 underline"
              lang="ko"
              onClick={goToReview}
            >
              전체보기
            </button>
          </div>
          <div className="mt-4 space-y-4 text-white text-[14px]">
            {reviews.map((review) => (
              <Review
                key={review.reviewSeq}
                review={{ ...review, top: false }} // review 객체로 모든 데이터를 전달
                userSeq={userData.userSeq}
                onShowMore={handleShowMore} // onShowMore prop 추가
                isDetailPage={true} // MovieDetailPage에서 사용하는 경우만 true
              />
            ))}
          </div>
        </div>

        {/* Trailer */}
        <div className="w-[700px] bg-black text-white flex justify-center items-center m-4 p-4 h-[450px] ml-[50px] mt-[100px]">
          {trailerUrl ? (
            <iframe
              src={videoUrl}
              title="YouTube video player"
              className="w-full h-full rounded-lg shadow-md"
              allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
              allowFullScreen
            ></iframe>
          ) : (
            <img
              src="/assets/movie/noVideo.png"
              alt="No video available"
              className="w-full h-full rounded-lg shadow-md"
            />
          )}
        </div>
      </div>

      {/* Recommended movies */}
      <div className="h-[300px] w-full flex-shrink-0 mb-[100px] mt-[20px]">
        <MoviesList
          category={`${movieTitle}과 유사한 장르 작품들`}
          movies={similarMovies}
        />
      </div>
      {/* Favorite and Dislike Modals */}
      {isFavoriteModalOpen && (
        <Modal
          onClose={() => setIsFavoriteModalOpen(false)}
          title="찜 완료!"
          icon={GoHeartFill}
          buttonText="확인"
        />
      )}

      {/* 무관심 모달 */}
      {isDislikeModalOpen && (
        <Modal
          onClose={() => setIsDislikeModalOpen(false)}
          title="무관심 추가 완료!"
          icon={IoBan}
          buttonText="확인"
        />
      )}
    </div>
  );
};

export default MovieDetailPage;
