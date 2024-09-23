// type.ts
export interface ReviewType {
  reviewSeq: number; // 영화리뷰 시퀀스
  userSeq: number; // 작성자 시쿼스
  movieId: number; // 영화 id
  reviewRating: number; // 평점
  content: string; // 리뷰 내용
  createdAt: string; // 리뷰 작성일시
  isSpoiler: boolean; // 스포일러 유무
  likes: number; // 좋아요 개수 (ERD에 추가할 예쩡)
  liked: boolean; // 좋아요 여부 (ERD에 추가할 예정)
  nickname: string; // 사용자 닉네임 (일단 넣음)
}

export interface FilterOptions {
  value: string; // 드롭다운 옵션 값
  label: string; // 드롭다운 옵션 레이블
}

export interface RatingData {
  stars: number; // 평점 데이터
  count: number; // 평점 개수
}
