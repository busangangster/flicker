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

const AppRoutes: React.FC = () => {
  return (
    <Routes>
      <Route path="/home" element={<Home />} />
      <Route path="/error" element={<ErrorPage />} />
      <Route path="/landing" element={<LandingPage />} />{" "}
      {/*렌딩페이지, 메인페이지 하나로 합쳐서 보여줄까 ? */}
      <Route path="/survey" element={<SurveyPage />} />
      <Route path="/passwordreset" element={<PasswordResetPage />} />
      <Route path="/passwordreset/:token" element={<PasswordChangePage />} />
      <Route path="/signup" element={<SignUpPage />} />
      <Route path="/signin" element={<SignInPage />} />
      <Route path="/contact" element={<ContactPage />} />
      <Route path="/servicedetail" element={<ServiceDetailPage />} />
      <Route path="/moviedetail" element={<MovieDetailPage />} />
      <Route path="/recommendlist" element={<RecommendListPage />} />
      <Route path="/recommend" element={<RecommendPage />} />
      <Route path="/review" element={<ReviewPage />} />
      <Route path="/search" element={<SearchPage />} />
      <Route path="/movies" element={<MoviesPage />} />
      {/* 경로 검색어 수정 해야함!*/}
      <Route path="/mypage" element={<Mypage />}>
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
