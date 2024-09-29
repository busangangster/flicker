import axios from "./axios";
import { handleApiError } from "../utils/errorHandling";
import { GenreMovie } from "../type";

// 장르별 영화 목록 조회
export const genreMovie = async (genre: string) => {
    try {
        const response = await axios.get(`/api/bff/movie/list/genre/${genre}/`);
        return response.data;
    } catch (error) {
        handleApiError(error);
        throw error;
    };
};
