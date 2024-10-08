import { useQuery } from "@tanstack/react-query";
import { fetchMovieGenre } from "../../apis/axios";

const useMoviesByGenre = (
  genre: string,
  page: number = 1,
  size: number = 15
) => {
  return useQuery({
    queryKey: ["moviesByGenre", genre, page, size],
    queryFn: () => fetchMovieGenre(genre, page, size),
    staleTime: 1000 * 60 * 60, // 1시간 동안 데이터 신선하게 유지
    // cacheTime: 1000 * 60 * 60 * 24, // 24시간 동안 캐시 보관
  });
};

export default useMoviesByGenre;
