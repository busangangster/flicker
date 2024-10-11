import React from "react";
import { Routes, Route, useLocation } from "react-router-dom";
import { CSSTransition, TransitionGroup } from "react-transition-group";
import LandingPage from "../pages/LandingPage";
import SurveyPage from "../pages/SurveyPage";
import SignUpPage from "../pages/auth/SignUpPage";
import SignInPage from "../pages/auth/SignInPage";
import ContactPage from "../pages/info/ContactPage";
import ServiceDetailPage from "../pages/info/ServiceDetailPage";
import MovieDetailPage from "../pages/movie/MovieDetailPage";
import RecommendPage from "../pages/movie/RecommendPage";
import ReviewPage from "../pages/movie/ReviewPage";
import SearchPage from "../pages/movie/SearchPage";
import FavoritePage from "../pages/mypage/FavoritePage";
import MyInformaitonPage from "../pages/mypage/MyInformaitonPage";
import PhotoBookPage from "../pages/mypage/PhotoBookPage";
import PhotoCardPage from "../pages/mypage/PhotoCardPage";
import Mypage from "../pages/mypage/Mypage";
import UserInfoEditPage from "../pages/mypage/UserInfoEditPage";
import VerificationPage from "../pages/mypage/VerificationPage";
import Home from "../pages/Home";
import MoviesPage from "../pages/movie/MoviesPage";
import ProtectedRoute from "../components/ProtectedRoute";
import MovieGenrePage from "../pages/movie/MovieGenrePage";
import DisLikePage from "../pages/mypage/DisLikePage";
import RecommendListReviewPage from "../pages/movie/RecommendListReviewPage";
import RecommendListActionPage from "../pages/movie/RecommendListActionPage";

const AppRoutes: React.FC = () => {
  const location = useLocation();

  return (
    <TransitionGroup>
      <CSSTransition
        key={location.key}
        classNames="fade" 
        timeout={200} 
      >
        <Routes location={location}>
          <Route path="/" element={<Home />} />
          <Route path="/home" element={<Home />} />
          <Route path="/landing" element={<LandingPage />} />
          <Route path="/signup" element={<SignUpPage />} />
          <Route path="/signin" element={<SignInPage />} />
          <Route path="/contact" element={<ContactPage />} />
          <Route path="/servicedetail" element={<ServiceDetailPage />} />
          <Route path="/moviedetail/:movieSeq" element={<MovieDetailPage />} />
          <Route
            path="/recommendlist/action"
            element={
              <ProtectedRoute>
                <RecommendListActionPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/recommendlist/review"
            element={
              <ProtectedRoute>
                <RecommendListReviewPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/recommend"
            element={
              <ProtectedRoute>
                <RecommendPage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/survey"
            element={
              <ProtectedRoute>
                <SurveyPage />
              </ProtectedRoute>
            }
          />
          <Route path="/review/:movieSeq" element={<ReviewPage />} />
          <Route path="/search" element={<SearchPage />} />
          <Route path="/movies" element={<MoviesPage />} />
          <Route path="/movies/genre/:genre" element={<MovieGenrePage />} />
          <Route path="/photobook" element={<PhotoBookPage />} />
          <Route path="/photocard" element={<PhotoCardPage />} />
          <Route
            path="/mypage/*"
            element={
              <ProtectedRoute>
                <Mypage />
              </ProtectedRoute>
            }
          >
            <Route path="favorite" element={<FavoritePage />} />
            <Route path="dislike" element={<DisLikePage />} />
            <Route path="myinformation" element={<MyInformaitonPage />} />
            <Route path="userinfoedit" element={<UserInfoEditPage />} />
            <Route path="verification" element={<VerificationPage />} />
          </Route>
        </Routes>
      </CSSTransition>
    </TransitionGroup>
  );
};

export default AppRoutes;
