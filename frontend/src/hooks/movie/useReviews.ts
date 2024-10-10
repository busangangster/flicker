import { useQuery, UseQueryOptions } from "@tanstack/react-query";
import { fetchMovieReviewsAll } from "../../apis/axios";
import { ReviewType } from "../../type";

const useReviews = (
  movieSeq: number,
  userSeq: number,
  sortOption: string,
  page: number
) => {
  return useQuery<ReviewType[], Error>({
    queryKey: ["reviews", movieSeq, userSeq, sortOption, page],
    queryFn: async () => {
      const data: ReviewType[] = await fetchMovieReviewsAll(
        movieSeq,
        userSeq,
        sortOption === "좋아요 많은 순"
          ? "like"
          : sortOption === "최신순"
          ? "date"
          : "old"
      );
      return data;
    },
    enabled: !!movieSeq && !!userSeq,
    keepPreviousData: true,
    staleTime: 1000 * 60 * 60, // 1시간 동안 데이터 신선하게 유지
  } as UseQueryOptions<ReviewType[], Error>);
};

export default useReviews;
