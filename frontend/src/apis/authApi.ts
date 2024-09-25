import axios from "./axios"; // isAxiosError를 axios 모듈에서 직접 가져오기
import { AxiosError } from "axios";

export const signin = async (username: string, password: string) => {
  try {
    console.log("로그인 요청 데이터:", { username, password });

    const response = await axios.post("/auth/login", { username, password });
    return response;
  } catch (error) {
    const err = error as AxiosError; // 타입 단언으로 error를 AxiosError로 간주

    // error가 AxiosError 타입인지 확인
    if (err.response) {
      // axios.isAxiosError가 아닌, isAxiosError 사용
      // AxiosError 타입으로 간주하고 에러 처리
      console.error("API 로그인 오류 상태:", err.response?.status);
      console.error("API 로그인 오류 데이터:", err.response?.data);
    } else {
      // AxiosError가 아닌 일반 오류 처리
      console.error("API 로그인 오류:", err.message);
    }
    throw error; // 원래 에러를 다시 던짐
  }
};
