import { jwtDecode } from "jwt-decode";
import { JwtPayload } from "../type";

// 유저 정보를 호출하는 API (JWT 토큰을 로컬 스토리지에서 가져옴)
export const getUserInfoFromToken = () => {
  const accessToken = localStorage.getItem("accessToken");

  if (accessToken) {
    const decodedToken: JwtPayload = jwtDecode<JwtPayload>(accessToken);

    console.log("Decoded JWT Token:", decodedToken);

    return {
      userId: decodedToken.userId, 
      email: decodedToken.email,
      nickname: decodedToken.nickname,
      birthDate: decodedToken.birthDate,
      gender: decodedToken.gender,
      userSeq: decodedToken.userSeq,
    };
  }

  throw new Error(
    "유저 정보가 없습니다. 토큰이 유효하지 않거나 로그인 상태가 아닙니다."
  );
};
