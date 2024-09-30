/* eslint-disable @typescript-eslint/no-explicit-any */
import axios from "./axios";
// import Cookies from "js-cookie";
import { handleApiError } from "../utils/errorHandling";
import { SignUpParams, SignInParams, SignInResponse } from "../type";

// 회원가입 API
export const signUp = async (params: SignUpParams) => {
  try {
    const response = await axios.post("/api/bff/user", params);
    return response.data;
  } catch (error) {
    handleApiError(error as any);
    throw error;
  }
};

// 로그인 API
export const signin = async (
  params: SignInParams
): Promise<SignInResponse | ""> => {
  try {
    console.log("로그인 요청 데이터:", params);

    const response = await axios.post<SignInResponse>(
      "/api/bff/user/login",
      params
    );

    // 서버 응답에서 Authorization 헤더 추출

    const accessToken = response.headers["authorization"].replace(
      "Bearer ",
      ""
    );
    // const { accessToken, refreshToken } = response.data || {};
    localStorage.setItem("accessToken", accessToken);
    console.log("accessToken 저장 완료");

    // JWT 토큰을 로컬 스토리지와 쿠키에 저장
    // Cookies.set("refreshToken", refreshToken, { expires: 1 }); // 1일간 유지

    return response.data;
  } catch (error) {
    handleApiError(error as any);
    throw error;
  }
};

// 토큰 검증 API
export const verifyToken = async () => {
  try {
    const response = await axios.get("/api/users/auth-test");
    return response.data;
  } catch (error) {
    handleApiError(error as any);
    throw error;
  }
};
