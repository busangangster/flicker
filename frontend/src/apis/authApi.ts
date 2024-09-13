/* eslint-disable @typescript-eslint/no-explicit-any */
import axios from "./axios";

export const signin = async (username: string, password: string) => {
  try {
    console.log("로그인 요청 데이터:", { username, password });

    const response = await axios.post("/auth/login", { username, password });
    return response;
  } catch (error) {
    // error를 any로 캐스팅하여 사용
    const err = error as any;

    if (err.response) {
      console.error("API 로그인 오류 상태:", err.response.status);
      console.error("API 로그인 오류 데이터:", err.response.data);
    } else {
      console.error("API 로그인 오류:", err.message);
    }
    throw err;
  }
};
