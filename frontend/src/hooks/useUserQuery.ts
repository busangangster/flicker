import { useQuery } from "@tanstack/react-query"; // @tanstack/react-query로부터 가져오기
import { getUserInfoFromToken } from "../apis/userApi";

export const useUserQuery = () => {
  return useQuery({
    queryKey: ["user"], // 첫 번째 인자는 배열 형태의 queryKey
    queryFn: getUserInfoFromToken, // 두 번째는 fetch 함수
    staleTime: 1000 * 60 * 5, // 5분 동안 데이터 캐싱
    cacheTime: 1000 * 60 * 10, // 10분 후 캐시 삭제
    refetchOnWindowFocus: false, // 윈도우 포커스 시 리패치 하지 않음
    retry: 1, // 실패 시 재시도 횟수
    onError: (error: any) => {
      console.error("유저 정보를 불러오는데 실패했습니다.", error);
    },
  });
};
