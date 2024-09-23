// type.ts
export interface ReviewType {
  username: string; // 유저 이름
  content: string;  // 리뷰 내용
  rating: number;   // 평점
  date: string;     // 리뷰 작성 날짜
}

export interface FilterOptions {
  value: string;    // 드롭다운 옵션 값
  label: string;    // 드롭다운 옵션 레이블
}

export interface RatingData {
  stars: number;    // 평점 데이터
  count: number;    // 평점 개수
}

export interface SignUpParams {
  userId: string; 
  email: string;
  password: string;
  passCheck: string;
  nickname: string;
  birthDate: string;
  gender: "M" | "F";
}

export interface SignInParams {
  userId: string;
  password: string;
}

export interface SignInResponse {
  accessToken: string;
  refreshToken: string;
}

export interface ApiErrorResponse {
  status: number;
  message: string;
  //   data?: any; // 만약 응답 데이터 구조가 복잡하다면, 여기서 구체적인 타입을 추가할 수 있음
}