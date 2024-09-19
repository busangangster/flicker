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
