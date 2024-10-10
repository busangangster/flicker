import axios from "axios";
import Cookies from "js-cookie";
import axiosRetry from "axios-retry";
import { ActorMoviesResponse } from "../type";

const instance = axios.create({
  baseURL: import.meta.env.VITE_SERVER_URL,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true, // for cross-origin requests
  // timeout: 40000,
});

axiosRetry(instance, {
  retries: 3, // 최대 3번 재시도
  retryDelay: () => {
    return 500; // 각 재시도마다 3초씩 지연
  },
  retryCondition: (error) => {
    console.error("Error occurred:", error.message);
    // 재시도할 조건을 설정 (예: 네트워크 오류 또는 5xx 오류 시)
    return (
      (error.response &&
        (error.response.status >= 500 || error.response.status === 404)) ||
      error.code === "ECONNABORTED"
    );
  },
  onRetry: (retryCount, error, requestConfig) => {
    console.log(`Retrying... Attempt #${retryCount}`);
    console.log("Request Config:", requestConfig);
    console.error("Error Details:", error);
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

// 영화 상세정보 조회
const movieDetailApi = axios.create({
  baseURL: `${import.meta.env.VITE_BFF_MOVIE_URL}`, // Base URL 설정
  headers: {
    "Content-Type": "application/json", // 모든 요청에 공통적으로 사용할 헤더
  },
  // timeout: 40000,
});

//
axiosRetry(movieDetailApi, {
  retries: 3, // 최대 3번 재시도
  retryDelay: () => {
    return 500; // 각 재시도마다 3초씩 지연
  },
  retryCondition: (error) => {
    console.error("Error occurred:", error.message);
    // 재시도할 조건을 설정 (예: 네트워크 오류 또는 5xx 오류 시)
    return (
      (error.response &&
        (error.response.status >= 500 || error.response.status === 404)) ||
      error.code === "ECONNABORTED"
    );
  },
  onRetry: (retryCount, error, requestConfig) => {
    console.log(`Retrying... Attempt #${retryCount}`);
    console.log("Request Config:", requestConfig);
    console.error("Error Details:", error);
  },
});

// 리뷰 데이터 조회
const reviewApiClient = axios.create({
  baseURL: `${import.meta.env.VITE_BFF_USER_URL}`, // 리뷰 API에 맞는 Base URL 설정
  headers: {
    "Content-Type": "application/json", // 모든 요청에 공통적으로 사용할 헤더
  },
  // timeout: 40000,
});

// 리뷰 데이터 retry
axiosRetry(reviewApiClient, {
  retries: 3, // 최대 3번 재시도
  retryDelay: () => {
    return 500; // 각 재시도마다 3초씩 지연
  },
  retryCondition: (error) => {
    console.error("Error occurred:", error.message);
    // 재시도할 조건을 설정 (예: 네트워크 오류 또는 5xx 오류 시)
    return (
      (error.response &&
        (error.response.status >= 500 || error.response.status === 404)) ||
      error.code === "ECONNABORTED"
    );
  },
  onRetry: (retryCount, error, requestConfig) => {
    console.log(`Retrying... Attempt #${retryCount}`);
    console.log("Request Config:", requestConfig);
    console.error("Error Details:", error);
  },
});

// 영화 리스트 조회
const movieListApi = axios.create({
  baseURL: `${import.meta.env.VITE_BFF_MOVIE_URL}/list`,
  headers: {
    "Content-Type": "application/json",
  },
  // timeout: 40000,
});

// 영화 리스트 retry
axiosRetry(movieListApi, {
  retries: 3, // 최대 3번 재시도
  retryDelay: () => {
    return 500; // 각 재시도마다 3초씩 지연
  },
  retryCondition: (error) => {
    console.error("Error occurred:", error.message);
    // 재시도할 조건을 설정 (예: 네트워크 오류 또는 5xx 오류 시)
    return (
      (error.response &&
        (error.response.status >= 500 || error.response.status === 404)) ||
      error.code === "ECONNABORTED"
    );
  },
  onRetry: (retryCount, error, requestConfig) => {
    console.log(`Retrying... Attempt #${retryCount}`);
    console.log("Request Config:", requestConfig);
    console.error("Error Details:", error);
  },
});

// 영화 리뷰 가져오기
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

// 영화 전체 리뷰 가져오기 
export const fetchMovieReviewsAll = async (
  movieSeq: number,
  userSeq: number,
  option: string,
) => {
  try {
    const response = await reviewApiClient.get(
      `/all/movies/${movieSeq}?userSeq=${userSeq}`,
      {
        params: {
          userSeq,
          movieSeq,
          option,
        },
      }
    );
    return response?.data?.data ?? [];
  } catch (error) {
    console.error("Error fetching movie reviews:", error);
    throw error;
  }
};


// 영화 상세정보 조회
export const fetchMovieDetail = async (movieSeq: number, userSeq: number) => {
  try {
    const response = await movieDetailApi.get(`/detail/${movieSeq}/${userSeq}`);
    return response.data.data; // 반환된 데이터가 올바른지 확인
  } catch (error) {
    console.error("Error fetching movie detail:", error);
    throw error;
  }
};

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

// 행동기반 추천 영화 조회
export const fetchMovieUserActing = async (userSeq: number) => {
  try {
    const response = await movieListApi.get(
      `/recommendation/action/${userSeq}`
    );
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

// 리뷰 기반 추천 영화 목록 조회
export const fetchMovieUserReview = async (userSeq: number) => {
  try {
    const response = await movieListApi.get(
      `/recommendation/review/${userSeq}`
    );
    if (response?.data.data && Array.isArray(response.data.data)) {
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

// 최근 작성한 영화 리뷰에 출연한 배우 기반 연관 영화 추천
export const fetchMovieBasedOnActor = async (
  userSeq: number
): Promise<ActorMoviesResponse> => {
  try {
    const url = `/recommendActor/${userSeq}`;
    const response = await movieListApi.get(url);
    if (response?.data.data) {
      return response.data.data as ActorMoviesResponse;
    } else {
      console.error("Unexpected response structure", response);
      throw new Error("Invalid response structure");
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
  const url = `${import.meta.env.VITE_BFF_USER_URL}/${userSeq}/favorite-movie`;

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
    return response.data;
  } catch (error) {
    console.error("Error adding favorite movies:", error);
    throw error; // 에러 처리
  }
};

export const addfavoriteMovies = async (userSeq: number, movieSeq: number) => {
  const url = `${
    import.meta.env.VITE_BFF_USER_URL
  }/${userSeq}/bookmark-movie/${movieSeq}`;
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
  const url = `${
    import.meta.env.VITE_BFF_USER_URL
  }/${userSeq}/bookmark-movie/${movieSeq}`;
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
  const url = `${import.meta.env.VITE_BFF_USER_URL}/${userSeq}/bookmark-movie`;

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
  const url = `${import.meta.env.VITE_BFF_USER_URL}/${userSeq}/dislike-movie`;

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
  const url = `${
    import.meta.env.VITE_BFF_USER_URL
  }/${userSeq}/dislike-movie/${movieSeq}`;
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
  const url = `${
    import.meta.env.VITE_BFF_USER_URL
  }/${userSeq}/dislike-movie/${movieSeq}`;
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
  const url = `${import.meta.env.VITE_BFF_USER_URL}/${userSeq}`;

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
  const url = `${import.meta.env.VITE_BFF_USER_URL}/${userSeq}/myPage`;

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

export const fetchMoviesBySearch = async (
  keyword: string,
  userSeq: number,
  page: number,
  size: number
) => {
  try {
    const response = await movieListApi.get(
      `/search/${keyword}/${userSeq}/${page}/${size}`
    );
    if (response?.data?.data && Array.isArray(response.data?.data)) {
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
