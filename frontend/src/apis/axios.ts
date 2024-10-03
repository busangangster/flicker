// import { favorite } from "/assets/service/favorite2.png";
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
    return response.data.data; // 반환된 데이터가 올바른지 확인
  } catch (error) {
    console.error("Error fetching movie detail:", error);
    throw error; // 에러 발생 시 처리
  }
};

// 리뷰 데이터 조회
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

// 장르별 조회
export const fetchMovieGenre = async (
  genre: string,
  page: number,
  size: number
) => {
  try {
    const response = await movieListApi.get(`/genre/${genre}/${page}/${size}`);

    if (response?.data?.data && Array.isArray(response.data.data)) {
      const movies = response.data.data;
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

// 연도별 조회
export const fetchMovieYear = async (
  year: number,
  page: number,
  size: number
) => {
  try {
    const response = await movieListApi.get(`/year/${year}/${page}/${size}`);

    if (response?.data?.data && Array.isArray(response.data.data)) {
      const movies = response.data.data;
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

// 국가별 조회
export const fetchMovieCountry = async (
  country: string,
  page: number,
  size: number
) => {
  try {
    const response = await movieListApi.get(
      `/country/${country}/${page}/${size}`
    );
    if (response?.data?.data && Array.isArray(response.data.data)) {
      const movies = response.data.data;
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

// 별점 높은 영화 조회
export const fetchMovieRating = async () => {
  try {
    const response = await movieListApi.get(`/topRating`);
    if (response?.data?.data && Array.isArray(response.data.data)) {
      const movies = response.data.data;
      return movies;
    } else {
      console.error("Unexpected response structure", response);
      return [];
    }
  } catch (error) {
    console.error("Error fetching movies:", error);
    throw error;
  }
};

// 이번 달 개봉 영화 조회
export const fetchMovieNew = async () => {
  try {
    const response = await movieListApi.get(`/newMovie`);
    if (response?.data?.data && Array.isArray(response.data.data)) {
      const movies = response.data.data;
      return movies;
    } else {
      console.error("Unexpected response structure", response);
      return [];
    }
  } catch (error) {
    console.error("Error fetching movies:", error);
    throw error;
  }
};

// coldStart issue 처리
export const addFavoriteMovies = async (
  userSeq: number,
  movieSeqList: number[]
) => {
  const url = `http://j11e206.p.ssafy.io/api/bff/user/${userSeq}/favorite-movie`;

  try {
    const response = await axios.post(
      url,
      {
        movieSeqList: movieSeqList.map(String),
      },
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    // 요청 성공 시 결과 출력
    console.log("Response:", response.data);
    return response.data;
  } catch (error) {
    console.error("Error adding favorite movies:", error);
    throw error; // 에러 처리
  }
};

export const addfavoriteMovies = async (userSeq: number, movieSeq: number) => {
  const url = `http://j11e206.p.ssafy.io/api/bff/user/${userSeq}/bookmark-movie/${movieSeq}`;
  try {
    const response = await axios.post(url, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    console.log("Response:", response.data);
  } catch (error) {
    console.error("Error adding favorite movies:", error);
    throw error; // 에러 처리
  }
};

export const deletefavoriteMovies = async (
  userSeq: number,
  movieSeq: number
) => {
  const url = `http://j11e206.p.ssafy.io/api/bff/user/${userSeq}/bookmark-movie/${movieSeq}`;
  try {
    const response = await axios.delete(url, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    console.log("Response:", response.data);
  } catch (error) {
    console.error("Error adding favorite movies:", error);
    throw error; // 에러 처리
  }
};

export const fetchFavoriteMovies = async (userSeq: number) => {
  console.log("userSeq", userSeq);
  const url = `http://j11e206.p.ssafy.io/api/bff/user/${userSeq}/bookmark-movie`;

  try {
    const response = await axios.get(url, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    console.log("Favorite movies response:", response.data.data); // API 응답 로그 출력

    return response.data.data; // 서버에서 영화 데이터를 반환
  } catch (error) {
    console.error("Error fetching favorite movies:", error);
    throw error;
  }
};

export const fetchDislikeMovies = async (userSeq: number) => {
  const url = `http://j11e206.p.ssafy.io/api/bff/user/${userSeq}/dislike-movie`;

  try {
    const response = await axios.get(url, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    return response.data.data; // 서버에서 영화 데이터를 반환
  } catch (error) {
    console.error("Error fetching favorite movies:", error);
    throw error;
  }
};

export const addDislikeMovies = async (userSeq: number, movieSeq: number) => {
  const url = `http://j11e206.p.ssafy.io/api/bff/user/${userSeq}/dislike-movie/${movieSeq}`;
  try {
    const response = await axios.post(url, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    console.log("Response:", response.data);
  } catch (error) {
    console.error("Error adding favorite movies:", error);
    throw error; // 에러 처리
  }
};

export const deleteDislikeMovies = async (
  userSeq: number,
  movieSeq: number
) => {
  const url = `http://j11e206.p.ssafy.io/api/bff/user/${userSeq}/dislike-movie/${movieSeq}`;
  try {
    const response = await axios.delete(url, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    console.log("Response:", response.data);
  } catch (error) {
    console.error("Error adding favorite movies:", error);
    throw error; // 에러 처리
  }
};

export const updateUserInfo = async (
  userSeq: number,
  data: { email: string; password: string; nickname: string }
) => {
  const url = `http://j11e206.p.ssafy.io/api/bff/user/${userSeq}`;

  try {
    const response = await axios.put(
      url,
      {
        email: data.email,
        password: data.password,
        nickname: data.nickname,
      }, // JSON 형식으로 body 전달
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );

    return response.data; // 응답 데이터 반환
  } catch (error) {
    console.error("Error updating user info:", error);
    throw error;
  }
};

export const fetchSideBarUserInfo = async (userSeq: number) => {
  const url = `http://j11e206.p.ssafy.io/api/bff/user/${userSeq}/myPage`;

  try {
    const response = await axios.get(url, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    return response.data; // API 응답 데이터 반환
  } catch (error) {
    console.error("Error fetching user page info:", error);
    throw error;
  }
};
