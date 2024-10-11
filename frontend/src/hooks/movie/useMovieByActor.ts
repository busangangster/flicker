// hooks/useMoviesByActor.ts
import { useQuery, UseQueryOptions } from "@tanstack/react-query";
import { fetchMovieBasedOnActor } from "../../apis/axios";
import { ActorMoviesResponse, Movie, UseMoviesByActorResult } from "../../type";

const useMoviesByActor = (userSeq: number | undefined) => {
  return useQuery<UseMoviesByActorResult, Error>({
    queryKey: ["moviesByActor", userSeq],
    queryFn: async () => {
      const data: ActorMoviesResponse = await fetchMovieBasedOnActor(userSeq!);
      const movies: Movie[] = data.movieListResponses.map((movie) => ({
        movieSeq: movie.movieSeq,
        moviePosterUrl: movie.moviePosterUrl,
        movieTitle: movie.movieTitle,
        movieYear: movie.movieYear,
        movieRating: movie.movieRating,
        runningTime: movie.runningTime,
        audienceRating: movie.audienceRating,
      }));

      return {
        movies,
        actorName: data.actorName,
        movieTitle: data.movieTitle,
      };
    },
    enabled: !!userSeq, // userSeq가 존재할 때만 쿼리 실행
    staleTime: 1000 * 60 * 60, // 1시간 동안 데이터 신선하게 유지
  } as UseQueryOptions<UseMoviesByActorResult, Error>);
};

export default useMoviesByActor;
