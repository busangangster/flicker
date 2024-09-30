import React from "react";
import { Routes, Route } from "react-router-dom";
import ErrorPage from "../pages/ErrorPage";
import LandingPage from "../pages/LandingPage";
import SurveyPage from "../pages/SurveyPage";
import PasswordResetPage from "../pages/auth/PasswordResetPage";
import SignUpPage from "../pages/auth/SignUpPage";
import SignInPage from "../pages/auth/SignInPage";
import ContactPage from "../pages/info/ContactPage";
import ServiceDetailPage from "../pages/info/ServiceDetailPage";
import MovieDetailPage from "../pages/movie/MovieDetailPage";
import RecommendListPage from "../pages/movie/RecommendListPage";
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
import PhotoCardDetailPage from "../pages/mypage/PhotoCardDetailPage";
import PasswordChangePage from "../pages/auth/PasswordChangePage";
import Home from "../pages/Home";
import MoviesPage from "../pages/movie/MoviesPage";
import ProtectedRoute from "../components/ProtectedRoute";
import MovieGenrePage from "../pages/movie/MovieGenrePage";

const AppRoutes: React.FC = () => {
  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/home" element={<Home />} />
      <Route path="/error" element={<ErrorPage />} />
      <Route path="/landing" element={<LandingPage />} />
      <Route path="/survey" element={<SurveyPage />} />
      <Route path="/passwordreset" element={<PasswordResetPage />} />
      <Route path="/passwordreset/:token" element={<PasswordChangePage />} />
      <Route path="/signup" element={<SignUpPage />} />
      <Route path="/signin" element={<SignInPage />} />
      <Route path="/contact" element={<ContactPage />} />
      <Route path="/servicedetail" element={<ServiceDetailPage />} />
      <Route path="/moviedetail" element={<MovieDetailPage />} />
      <Route
        path="/recommendlist"
        element={
          <ProtectedRoute>
            <RecommendListPage />
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
      <Route path="/review" element={<ReviewPage />} />
      <Route path="/search" element={<SearchPage />} />
      <Route path="/movies" element={<MoviesPage />} />{" "}
      <Route path="/movies/genre/:genre" element={<MovieGenrePage />} />{" "}
      {/* 장르별 페이지 추가 */}
      <Route
        path="/mypage/*"
        element={
          <ProtectedRoute>
            <Mypage />
          </ProtectedRoute>
        }
      >
        <Route path="favorite" element={<FavoritePage />} />
        <Route path="myinformation" element={<MyInformaitonPage />} />
        <Route path="photobook" element={<PhotoBookPage />} />
        <Route path="photocard" element={<PhotoCardPage />} />
        <Route path="photocarddetail" element={<PhotoCardDetailPage />} />
        <Route path="userinfoedit" element={<UserInfoEditPage />} />
        <Route path="verification" element={<VerificationPage />} />
      </Route>
    </Routes>
  );
};

export default AppRoutes;
