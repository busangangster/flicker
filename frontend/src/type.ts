import { Sprite } from "pixi.js";

export interface FilterOptions {
  value: string; // 드롭다운 옵션 값
  label: string; // 드롭다운 옵션 레이블
}

export interface RatingData {
  stars: number; // 평점 데이터
  count: number; // 평점 개수
}

export interface ExtendedSprite extends Sprite {
  userData: {
    angle: number;
    rotationOffset: number;
  };
}

export interface ReviewType {
  review_seq: number;
  member_seq: number;
  movie_id: number;
  review_rating: number;
  content: string;
  created_at: string;
  likes: number;
  liked: boolean;
  nickname: string;
  username: string;
  rating: number;
  date: string;
}
