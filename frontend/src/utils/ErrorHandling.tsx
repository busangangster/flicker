import { AxiosError } from "axios";
import { ApiErrorResponse } from "../type";

// 에러 처리 함수
export const handleApiError = (error: AxiosError<ApiErrorResponse>) => {
  if (error.response) {
    // HTTP 응답이 있는 경우
    console.error(`API 에러 상태: ${error.response.status}`);
    console.error(`API 에러 메시지: ${error.response.data.message}`);
    return { error: error.response.data.message };
  } else if (error.request) {
    // 요청이 이루어졌으나 응답을 받지 못한 경우
    console.error("서버로부터 응답이 없습니다.");
    return { error: "No response from the server" };
  } else {
    // 요청을 설정하는 중에 에러가 발생한 경우
    console.error("API 요청 설정 중 에러:", error.message);
    return { error: error.message };
  }
};
