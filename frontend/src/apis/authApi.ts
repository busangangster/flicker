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
    const response = await axios.post<SignInResponse>(
      "/api/bff/user/login",
      params
    );

    const accessTokenHeader = response.headers["Authorization"];

    if (accessTokenHeader) {
      const accessToken = accessTokenHeader.replace("Bearer ", "");
      localStorage.setItem("accessToken", accessToken);
    } else {
      console.error("Authorization 헤더가 없습니다:", response.headers);
    }

    // 서버 응답에서 Authorization 헤더 추출

    const accessToken = response.headers["authorization"].replace(
      "Bearer ",
      ""
    );
    localStorage.setItem("accessToken", accessToken);

    // if (accessTokenHeader) {
    //   const accessToken = accessTokenHeader.replace("Bearer ", "");
    //   localStorage.setItem("accessToken", accessToken);
    // } else {
    //   console.error("Authorization 헤더가 없습니다:", response.headers);
    // }

    // const accessToken = response.headers["authorization"].replace(
    //   "Bearer ",
    //   ""
    // );
    // localStorage.setItem("accessToken", accessToken);

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

// 최초 로그인 판단
export const checkFirstLogin = async (userSeq: number) => {
  try {
    const response = await axios.get(
      `/api/bff/user/check-first-login/${userSeq}`
    );
    console.log(response.data.data);
    return response.data.data;
  } catch (error) {
    console.error("first login error:", error);
    throw error;
  }
};