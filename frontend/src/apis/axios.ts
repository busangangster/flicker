import axios from "axios";
import Cookies from "js-cookie";

const instance = axios.create({
  baseURL: import.meta.env.VITE_SERVER_URL,


const apiClient = axios.create({
  baseURL: "http://j11e206.p.ssafy.io/api/bff/client", // Base URL 설정
  headers: {
    "Content-Type": "application/json", // 모든 요청에 공통적으로 사용할 헤더
  },
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

export const fetchMovieDetail = async (movieId: any) => {
  const response = await apiClient.get(`/boarding/${movieId}`);
  return response.data.data; // data 안의 data를 반환 (필요한 데이터만)
};

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

