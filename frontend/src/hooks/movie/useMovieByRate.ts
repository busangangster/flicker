import { useQuery } from "@tanstack/react-query";
import { fetchMovieRating } from "../../apis/axios";

const useMoviesByRate = () => {
  return useQuery({
    queryKey: ["moviesByRate"],
    queryFn: () => fetchMovieRating(),
    staleTime: 1000 * 60 * 60, // 1시간 동안 데이터 신선하게 유지
    // cacheTime: 1000 * 60 * 60 * 24, // 24시간 동안 캐시 보관
  });
};

export default useMoviesByRate;
