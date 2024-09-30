import axios from "axios";

const loginAxios = () => {
  const instance = axios.create({
    baseURL: "http://localhost:8080/api",
    headers: {
      "Content-Type": "application/json",
    },
    withCredentials: true, // for cross-origin requests
  });
  return instance;
};

const apiClient = axios.create({
  baseURL: "http://j11e206.p.ssafy.io/api/bff/client", // Base URL 설정
  headers: {
    "Content-Type": "application/json", // 모든 요청에 공통적으로 사용할 헤더
  },
});

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

export default { loginAxios };
