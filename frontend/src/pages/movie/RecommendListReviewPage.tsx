import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { SwiperSlide, Swiper } from "swiper/react";
import { Navigation, Pagination, Autoplay } from "swiper/modules"; // Autoplay 모듈 추가
import { IoIosArrowRoundBack } from "react-icons/io";
import { useUserQuery } from "../../hooks/useUserQuery";
import { Movie } from "../../type";
import { fetchMovieUserReview } from "../../apis/axios";

const RecommandListReviewPage: React.FC = () => {
  const navigate = useNavigate();
  const [movies, setMovies] = useState<Movie[]>([]); // 영화 목록 상태
  const [isLoading, setIsLoading] = useState(true); // 로딩 상태

  // 페이지 진입 애니메이션 상태
  const [isLoaded, setIsLoaded] = useState(false);
  const { data: userData } = useUserQuery();
  const userSeq = userData?.userSeq;

  useEffect(() => {
    // 페이지 로드 시 애니메이션을 시작
    setTimeout(() => {
      setIsLoaded(true);
    }, 0); // 약간의 지연을 두고 애니메이션 시작
    const fetchMovies = async () => {
      try {
        const moviesData = await fetchMovieUserReview(userSeq!);
        console.log(moviesData);
        setMovies(moviesData); // 영화 데이터를 상태로 설정
        setIsLoading(false); // 로딩 완료
      } catch (error) {
        console.error("Error fetching movie data:", error);
        setIsLoading(false);
      }
    };

    fetchMovies();
  }, [userSeq]);

  const goToDetail = (movieSeq: number) => {
    navigate(`/moviedetail/${movieSeq}`);
  };

  const goToRecommend = () => {
    navigate("/recommend");
  };

  return (
    <div
      className={`relative bg-black text-white min-h-screen flex items-center justify-center transition-all duration-700 ${
        isLoaded
          ? "scale-100 opacity-100 bg-transparent "
          : "scale-110 opacity-50 bg-black"
      }`}
      style={{
        backgroundColor: "black",
        backgroundImage: isLoaded ? `url(/assets/movie/theater3.jpg)` : "none",
        backgroundSize: "cover",
        backgroundPosition: "center",
      }}
    >
      <header className="sticky top-0 bg-transparent z-20">
        <IoIosArrowRoundBack
          onClick={goToRecommend} // 뒤로가기 기능
          className="text-gray-200 cursor-pointer fixed left-4 top-5 w-10 h-10 hover:opacity-60" // 크기 및 위치 설정
        />
      </header>
      {/* 영화관 스타일 화면 */}
      <div className="relative w-[73%] h-[75vh] rounded-md overflow-hidden">
        <h2 className="text-center text-3xl font-bold text-black mb-[80px] mt-24 ">
          My own movie theater
        </h2>
        {isLoading ? (
          <p className="text-center text-white">Loading movies...</p>
        ) : (
          <Swiper
            slidesPerView={6}
            spaceBetween={10}
            autoplay={{
              delay: 0,
              disableOnInteraction: false,
            }}
            speed={10000}
            loop={true}
            modules={[Navigation, Pagination, Autoplay]}
          >
            {movies.map((movie) => (
              <SwiperSlide
                key={movie.movieSeq}
                className="flex justify-center items-center transition-transform duration-300 transform hover:-translate-y-2 mt-4"
              >
                <img
                  src={movie.moviePosterUrl}
                  alt={`Movie ${movie.movieSeq + 1}`}
                  onClick={() => goToDetail(movie.movieSeq)}
                  className="rounded-lg shadow-md object-cover w-full h-[306px] cursor-pointer"
                />
              </SwiperSlide>
            ))}
          </Swiper>
        )}
      </div>
    </div>
  );
};

export default RecommandListReviewPage;


