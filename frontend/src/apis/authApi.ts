/* eslint-disable @typescript-eslint/no-explicit-any */
import axios from "./axios";
import { SignUpParams, SignInParams, SignInResponse } from "../type";
import { handleApiError } from "../utils/ErrorHandling";

export const signUp = async (params: SignUpParams) => {
  try {
    const response = await axios.post("/api/bff/user", params);
    return response.data;
  } catch (error) {
    handleApiError(error as any);
    throw error;
  }
};

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

    const accessToken = response.headers["authorization"].replace(
      "Bearer ",
      ""
    );
    localStorage.setItem("accessToken", accessToken);

    return response.data;
  } catch (error) {
    handleApiError(error as any);
    throw error;
  }
};

export const verifyToken = async () => {
  try {
    const response = await axios.get("/api/users/auth-test");
    return response.data;
  } catch (error) {
    handleApiError(error as any);
    throw error;
  }
};

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