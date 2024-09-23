/* eslint-disable @typescript-eslint/no-explicit-any */
import axios from "./axios";
import Cookies from "js-cookie";
import { handleApiError } from "../utils/errorHandling";
import { SignUpParams, SignInParams, SignInResponse } from "../type";

export const signUp = async (params: SignUpParams) => {
  try {
    const response = await axios.post("/users", params);
    return response.data;
  } catch (error) {
    handleApiError(error as any);
    throw error;
  }
};

export const signin = async (
  params: SignInParams
): Promise<SignInResponse | undefined> => {
  try {
    console.log("로그인 요청 데이터:", params);

    const response = await axios.post<SignInResponse>("/users/login", params);
    const { accessToken, refreshToken } = response.data;

    // JWT 토큰을 로컬 스토리지와 쿠키에 저장
    localStorage.setItem("accessToken", accessToken);
    Cookies.set("refreshToken", refreshToken, { expires: 1 }); // 1일간 유지
    return response.data;
  } catch (error) {
    handleApiError(error as any);
    throw error;
  }
};

export const verifyToken = async () => {
  try {
    const response = await axios.get("/auth-test");
    return response.data;
  } catch (error) {
    handleApiError(error as any);
    throw error;
  }
};
