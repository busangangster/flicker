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
    const response = await axios.get(
      "/api/bff/movie/list/top10"
    );
    return response.data;
  } catch (error) {
    console.error("영화 목록을 가져오는데 실패했습니다.", error);
    throw error;
  }
};
