import { Sprite } from "pixi.js";

export interface ReviewType {
  reviewSeq: number;
  createdAt: string;
  nickname: string;
  reviewRating: number;
  content: string;
  spoiler: boolean;
  likes: number;
  liked: boolean;
  top: boolean;
}

export interface ReviewProps {
  review: ReviewType;
  onLikeToggle: (reviewSeq: number) => void; // 좋아요 토글 함수
}

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
    yOffset: number;
  };
}

export interface MovieDetail {
  bookMarkedMovie: boolean;
  movie: {
    movieSeq: number;
    movieDetail: {
      movieTitle: string;
      director: string;
      genre: string;
      country: string;
      moviePlot: string;
      audienceRating: string;
      movieYear: number;
      runningTime: string;
      moviePosterUrl: string;
      trailerUrl: string;
      backgroundUrl: string;
    };
    movieRating: number;
    actors: {
      actorName: string;
      role: string;
    }[];
  };
  reviewList: {
    reviewSeq: number;
    createdAt: string;
    nickname: string;
    reviewRating: number;
    content: string;
    spoiler: boolean;
    likes: number;
    liked: boolean;
    top: boolean;
  }[];
  recommendedMovieList: {
    movieSeq: number;
    moviePosterUrl: string;
  }[];
  likeMovie: boolean;
}
