import {
  ReviewForm,
  WordCloud,
  RatingData,
  ReviewCheckResponse,
} from "../type";
import axios from "./axios";
// import { handleApiError } from "../utils/errorHandling";
// import { GenreMovie } from "../type";

// 장르별 영화 목록 조회
// export const getMovieGenre = async (genre: string) => {
//     try {
//         const response = await axios.get(`/api/bff/movie/list/genre/${genre}/`);
//         return response.data;
//   } catch (error) {
//     console.error("영화 목록을 가져오는데 실패했습니다.", error);
//     throw error;
//   }
// };

// top10 영화 목록 조회
export const getTopTenMovies = async () => {
  try {
    const response = await axios.get("/api/bff/movie/list/top10");
    return response.data;
  } catch (error) {
    console.error("영화 목록을 가져오는데 실패했습니다.", error);
    throw error;
  }
};

// 리뷰 작성
export const createReview = async (reviewData: ReviewForm) => {
  try {
    const response = await axios.post("/api/bff/user/review", reviewData);
    return response.data;
  } catch (error) {
    console.error("리뷰 작성에 실패했습니다.", error);
    throw error;
  }
};

// 리뷰 삭제
export const deleteReview = async (reviewSeq: number, userSeq: number) => {
  try {
    const response = await axios.delete("/api/bff/user/review", {
      data: {
        reviewSeq: reviewSeq,
        userSeq: userSeq,
      },
    });
    return response.data;
  } catch (error) {
    console.error("리뷰 삭제에 실패했습니다.", error);
    throw error;
  }
};

// 워드클라우드
export const getWordCloud = async (movieSeq: number): Promise<WordCloud> => {
  try {
    const response = await axios.get<WordCloud>(
      `/api/bff/movie/wordCloud/${movieSeq}`
    );
    return response.data;
  } catch (error) {
    console.error("워드 클라우드를 불러오는데 실패했습니다.", error);
    throw error;
  }
};

// 영화 분포표
export const getReviewRating = async (
  movieSeq: number
): Promise<RatingData> => {
  try {
    const response = await axios.get<RatingData>(
      `/api/bff/user/review/movies/${movieSeq}/distribute`
    );
    return response.data;
  } catch (error) {
    console.error("별점 분포를 불러오는데 실패했습니다.", error);
    throw error;
  }
};

// 이미 작성된 리뷰 확인 API 호출 함수
export const checkAlreadyReview = async (
  userSeq: number,
  movieSeq: number
): Promise<ReviewCheckResponse> => {
  try {
    const response = await axios.get<{ data: ReviewCheckResponse }>(
      `/api/bff/user/review/check-already-review`,
      {
        params: {
          userSeq: userSeq,
          movieSeq: movieSeq,
        },
      }
    );
    return response.data.data; // 실제 필요한 데이터만 반환
  } catch (error) {
    console.error("리뷰 확인 중 오류가 발생했습니다.", error);
    throw error;
  }
};
