import { Sprite } from "pixi.js";
import { IconType } from "react-icons";

export interface CircleCarouselProps {
  onCardClick: (videoUrl: string) => void;
  className?: string;
}

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

export interface FavoriteMovie {
  movieSeq: number;
  moviePosterUrl: string;
}

export interface MemberProps {
  name: string;
  role: string;
  description: string;
  githubUrl: string;
  emailUrl: string;
  imgSrc: string;
}

export interface UseMoviesByActorResult {
  movies: Movie[];
  actorName: string;
  movieTitle: string;
}

export interface ActorMoviesResponse {
  actorName: string;
  movieTitle: string;
  movieListResponses: Movie[];
}

export interface ReviewProps {
  review: ReviewType;
  onDelete?: (reviewSeq: number) => Promise<void>;
  userSeq: number;
  onShowMore?: (reviewSeq: number) => void; 
  isDetailPage?: boolean; 
}

export interface FilterOptions {
  value: string; 
  label: string; 
}

export interface RatingData {
  stars: number; 
  count: number; 
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

export interface MovieImageDto {
  moviePosterUrl: string;
  movieTitle: string;
  movieYear: number;
  backgroundUrl: string;
}

export interface PhotocardDataItem {
  reviewDto: PhotoCardReviewDto;
  movieImageDto: MovieImageDto;
}

export interface PhotocardData {
  data: PhotocardDataItem[];
}

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
  }) => void; 
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
}

export interface MoviesListProps {
  category: string;
  movies: Movie[]; 
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

export interface ReviewCheckResponse {
  alreadyReview: boolean;
  reviewDto: CheckReview | null; 
}

export interface ModalProps {
  onClose: () => void;
  title: string;
  description?: string;
  icon?: IconType; 
  buttonText: string;
  iconColor?: string;
}