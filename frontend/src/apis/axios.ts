import axios from "axios";
import Cookies from "js-cookie";

const instance = axios.create({
  baseURL: import.meta.env.VITE_SERVER_URL,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true, // for cross-origin requests
});

// 요청 인터셉터 : JWT 토큰을 요청 헤더에 포함
instance.interceptors.request.use((config) => {
  const accessToken = localStorage.getItem("accessToken");

  if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }

  return config;
});

// 응답 인터센터 : 토큰 만료 시 처리
instance.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config;

    // 토큰 만료 시 처리
    if (error.response && error.response.status === 401) {
      try {
        const refreshToken = Cookies.get("refreshToken");

        if (refreshToken) {
          // 토큰 갱신 API 호출
          const response = await axios.post(
            `${originalRequest.baseURL}/auth/refresh-token`,
            {
              refreshToken,
            }
          );

          const { accessToken } = response.data;
          localStorage.setItem("accessToken", accessToken);
          originalRequest.headers.Authorization = `Bearer ${accessToken}`;

          return axios(originalRequest);
        } else {
          console.error("Refresh token not found.");
        }
      } catch (err) {
        console.error("Token refresh failed", err);
      }
    }

    return Promise.reject(error);
  }
);

export default instance;

// 영화 상세정보 조회
const movieDetailApi = axios.create({
  baseURL: "http://j11e206.p.ssafy.io/api/bff/movie/", // Base URL 설정
  headers: {
    "Content-Type": "application/json", // 모든 요청에 공통적으로 사용할 헤더
  },
});

export const fetchMovieDetail = async (movieSeq: number, userSeq: number) => {
  try {
    const response = await movieDetailApi.get(`/detail/${movieSeq}/${userSeq}`);
    console.log("API Response:", response); // API 응답을 확인
    return response.data.data; // 반환된 데이터가 올바른지 확인
  } catch (error) {
    console.error("Error fetching movie detail:", error);
    throw error; // 에러 발생 시 처리
  }
};

// 더미 리뷰 데이터 조회
const reviewApiClient = axios.create({
  baseURL: "http://j11e206.p.ssafy.io/api/bff/user", // 리뷰 API에 맞는 Base URL 설정
  headers: {
    "Content-Type": "application/json", // 모든 요청에 공통적으로 사용할 헤더
  },
});

export const fetchMovieReviews = async (
  movieId: number,
  userSeq: number,
  option: string,
  page: number,
  size: number
) => {
  try {
    const response = await reviewApiClient.get(`/movies/${movieId}`, {
      params: {
        userSeq,
        option,
        page,
        size,
      },
    });
    return response?.data?.data ?? []; // 필요한 데이터만 반환
  } catch (error) {
    console.error("Error fetching movie reviews:", error);
    throw error;
  }
};

// 영화 리스트 조회
const movieListApi = axios.create({
  baseURL: "http://j11e206.p.ssafy.io/api/bff/movie/list",
  headers: {
    "Content-Type": "application/json",
  },
});

export const fetchMovieGenre = async (
  genre: string,
  page: number,
  size: number
) => {
  try {
    // API 호출
    const response = await movieListApi.get(`/genre/${genre}/${page}/${size}`);

    // 응답 구조가 올바른지 확인 후 처리
    if (response?.data?.data && Array.isArray(response.data.data)) {
      const movies = response.data.data;
      // console.log(movies);
      // return movies.map((movie: any) => movie.moviePosterUrl); // moviePosterUrl 추출
      return movies;
    } else {
      console.error("Unexpected response structure", response);
      return []; // 응답 구조가 예상과 다르다면 빈 배열 반환
    }
  } catch (error) {
    console.error("Error fetching movies:", error);
    throw error;
  }
};
