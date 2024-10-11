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
import { IoIosArrowRoundBack } from "react-icons/io";
import Modal from "../../components/common/Modal"

const MovieDetailPage: React.FC = () => {
  const navigate = useNavigate();
  const { movieSeq } = useParams<{ movieSeq: string }>(); 
  const [isLiked, setIsLiked] = useState(false);
  const [disLiked, setDisLiked] = useState(false);
  const [isFavoriteModalOpen, setIsFavoriteModalOpen] = useState(false); 
  const [isDislikeModalOpen, setIsDislikeModalOpen] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedCategory, setSelectedCategory] = useState("배우");
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
    enabled: !!userSeq && !!movieSeq, 
    // staleTime: 1000 * 60 * 5, // 5분 동안 데이터 신선하게 유지
  });

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [location.pathname]);

  useEffect(() => {
    if (movieData) {
      setIsLiked(bookMarkedMovie); 
    }
  }, [movieData]);

  useEffect(() => {
    if (movieData) {
      setDisLiked(unlikedMovie); 
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
      moviePlot,
      audienceRating,
      movieYear,
      runningTime,
      moviePosterUrl,
      trailerUrl,
      backgroundUrl,
      movieRating,
      actors,
    } = {}, 
    bookMarkedMovie = false,
    unlikedMovie = false,
    reviews = [],
    similarMovies = [],
  } = movieData;

  const MAX_LENGTH = 250;
  const isLongText = moviePlot && moviePlot.length > MAX_LENGTH;
  const displayedText = moviePlot
    ? moviePlot.slice(0, MAX_LENGTH)
    : "줄거리를 준비 중입니다."; 

  const posterUrl = moviePosterUrl
    ? moviePosterUrl
    : "/assets/movie/noImage.png";

  const extractVideoId = (url: string) => {
    const videoIdMatch = url.match(
      /(?:\?v=|\/embed\/|\.be\/|\/v\/|\/e\/|watch\?v=|watch\?.+&v=)([^&\n?#]+)/
    );
    return videoIdMatch ? videoIdMatch[1] : null;
  };

  const videoUrl = trailerUrl
    ? `https://www.youtube.com/embed/${extractVideoId(
        trailerUrl
      )}?autoplay=1&mute=1&loop=1&playlist=${extractVideoId(trailerUrl)}`
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
      console.error("User sequence is not available."); 
      return;
    }

    setIsLiked((prev) => !prev);

    try {
      if (isLiked) {
        await deletefavoriteMovies(userSeq, Number(movieSeq));
      } else {
        await addfavoriteMovies(userSeq, Number(movieSeq));
        setIsFavoriteModalOpen(true); 
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
      setDisLiked((prevDisLiked) => {
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
              setIsDislikeModalOpen(true); 
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

  const renderCategoryContent = () => {
    switch (selectedCategory) {
      case "배우":
        return (
          <div className="flex flex-wrap gap-2 mt-6">
            {(actors || [])
              .slice(0, 15) 
              .map((actor, index) => (
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
      <div className="relative h-auto">
        <div
          className="absolute inset-0 h-[650px] w-full bg-cover bg-center"
          style={{
            backgroundImage: `url(${backgroundUrl})`,
          }}
        >
          <div className="absolute inset-0 bg-black opacity-70"></div>
        </div>

        <header className="sticky top-0 bg-transparent z-20">
          <Navbar />
          <IoIosArrowRoundBack
            onClick={() => navigate(-1)} 
            className="text-gray-200 cursor-pointer fixed left-4 top-16 w-10 h-10 hover:opacity-60" 
          />
        </header>

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
                review={{ ...review, top: false }} 
                userSeq={userData.userSeq}
                onShowMore={handleShowMore} 
                isDetailPage={true} 
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
          iconColor="#EF7B7B"
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
