// src/hooks/useUserQuery.ts
import { useQuery } from 'react-query';
import { getUserInfoFromToken } from "../apis/userApi";

export const useUserQuery = () => {
    return useQuery("user", getUserInfoFromToken, {
      staleTime: 1000 * 60 * 5, // 5분 동안 데이터 캐싱
      cacheTime: 1000 * 60 * 10, // 10분 후 캐시 삭제
      refetchOnWindowFocus: false, // 윈도우 포커스 시 리패치 하지 않음
      retry: 1,
      onError: (error) => {
        console.error("유저 정보를 불러오는데 실패했습니다.", error);
      },
    });
}