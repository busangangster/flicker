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
  top?: boolean;
  isUserReview?: boolean;
  
}

export interface ReviewProps {
  review: ReviewType;
  onDelete?: (reviewSeq: number) => Promise<void>;
  userSeq: number;
  onShowMore?: (reviewSeq: number) => void; // 추가된 prop
  isDetailPage?: boolean; // 추가된 prop
}

export interface FilterOptions {
  value: string; // 드롭다운 옵션 값
  label: string; // 드롭다운 옵션 레이블
}

export interface RatingData {
  stars: number; // 평점 데이터
  count: number; // 평점 개수
}

export interface SignUpParams {
  userId: string;
  email: string;
  password: string;
  passCheck: string;
  nickname: string;
  birthDate: string;
  gender: "M" | "F" | "";
}

export interface SignInParams {
  userId: string;
  password: string;
}

export interface SignInResponse {
  accessToken: string;
  refreshToken: string;
}

export interface JwtPayload {
  userId: string;
  email: string;
  nickname: string;
  birthDate: string;
  gender: "M" | "F" | "";
  userSeq: number;
}

export interface ApiErrorResponse {
  status: number;
  message: string;
  //   data?: any; // 만약 응답 데이터 구조가 복잡하다면, 여기서 구체적인 타입을 추가할 수 있음
}

export interface ExtendedSprite extends Sprite {
  userData: {
    angle: number;
    rotationOffset: number;
    yOffset: number;
  };
}

export interface MovieDetail {
  movieDetailResponse: {
    // movieSeq: number;
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
    movieRating: number;
    actors: {
      actorSeq: number;
      actorName: string;
      role: string;
    }[];
  };
  bookMarkedMovie: boolean;
  unlikedMovie: boolean;
  reviews: {
    reviewSeq: number;
    userSeq: number;
    nickname: string;
    movieSeq: number;
    reviewRating: number;
    content: string;
    createdAt: string;
    spoiler: boolean;
    likes: number;
    liked: boolean;
  }[];
  similarMovies: {
    movieSeq: number;
    movieTitle: string;
    moviePosterUrl: string;
  }[];
}

export interface ProtectedRouteProps {
  children: React.ReactNode;
}

export interface SearchBarProps {
  initialSearchQuery?: string;
  isExpanded: boolean;
  setIsExpanded: React.Dispatch<React.SetStateAction<boolean>>;
}

export interface ServiceContentProps {
  title: string;
  description: string;
  tags: string[];
  imageUrl: string;
}

export interface TopTenMovie {
  movieSeq: number;
  movieTitle: string;
  moviePosterUrl: string;
}

// Photocard Review 데이터 타입
export interface PhotoCardReviewDto {
  userSeq: number;
  movieSeq: number;
  reviewSeq: number;
  nickname: string;
  reviewRating: number;
  content: string;
  spoiler: boolean;
  likes: number;
  liked: boolean;
  createdAt: string;
  top: null | string;
}

// Movie Image DTO 데이터 타입
export interface MovieImageDto {
  moviePosterUrl: string;
  movieTitle: string;
  movieYear: number;
  backgroundUrl: string;
}

// Photocard 데이터 타입
export interface PhotocardDataItem {
  reviewDto: PhotoCardReviewDto;
  movieImageDto: MovieImageDto;
}

// PhotocardData 내에 data라는 배열을 가진 타입
export interface PhotocardData {
  data: PhotocardDataItem[];
}

// IFlipBook = 포토북
export interface IFlipBook {
  flipNext: () => void;
  flipPrev: () => void;
  pageFlip: () => { flipNext: () => void; flipPrev: () => void };
}

export interface PhotoCardFrontProps {
  images: Array<{
    src: string;
    alt: string;
    movieSeq: number;
    movieTitle: string;
    movieYear: number;
    reviewRating: number;
    createdAt: string;
    content: string;
    likes: number;
    backgroundUrl: string;
  }>;
  pageIndex: number;
  onCardClick: (card: {
    src: string;
    alt: string;
    movieSeq: number;
    movieTitle: string;
    movieYear: number;
    reviewRating: number;
    createdAt: string;
    content: string;
    likes: number;
    backgroundUrl: string;
  }) => void; // onCardClick 함수 타입 정의
};


export interface Movie {
  movieSeq: number;
  moviePosterUrl: string;
  movieTitle: string;
  movieYear?: number;
  movieRating?: number;
  runningTime?: string;
  audienceRating?: string;
}

export interface SelectionListProps {
  movies: Movie[];
  // loadMoreMovies: () => void; // loadMoreMovies 함수 추가
  // hasMore: boolean; // hasMore 값 추가
}

export interface MoviesListProps {
  category: string;
  movies: Movie[]; // movieSeq와 moviePosterUrl을 포함하는 배열
}

export interface Page {
  id: number;
  content: JSX.Element;
  className?: string; 
}

export interface ReviewForm {
  userSeq: number;
  movieSeq: number;
  reviewRating: number;
  content: string;
  isSpoiler: boolean;
}

export interface WordCloud {
  data: {
    keyword: string;
    count: number;
  }[];
}

export interface ReviewRatingCount {
  reviewRating: number;
  count: number; 
}

export interface RatingData {
  data: {
    reviewRatingCount: ReviewRatingCount[];
    totalCnt: number;
  };
}

// 리뷰 데이터 타입 정의
export interface CheckReview {
  reviewSeq: number;
  userSeq: number;
  nickname: string;
  movieSeq: number;
  reviewRating: number;
  content: string;
  createdAt: string;
  spoiler: boolean;
  likes: number;
  liked: boolean;
}

// 리뷰 확인 응답 데이터 타입 정의
export interface ReviewCheckResponse {
  alreadyReview: boolean;
  reviewDto: CheckReview | null; // 리뷰가 없는 경우 null
}
