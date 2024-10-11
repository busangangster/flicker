// src/components/ProtectedRoute.tsx
import React from "react";
import { Navigate } from "react-router-dom";
import { useUserQuery } from "../hooks/useUserQuery";
import { ProtectedRouteProps } from "../type";

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children }) => {
  const { data, isLoading } = useUserQuery();

  if (isLoading) {
    return <div>로딩 중...</div>; // 로딩 상태
  }

  if (!data) {
    return <Navigate to="/signin" />; // 로그인하지 않은 경우 로그인 페이지로 리다이렉트
  }

  return <>{children}</>; // 로그인된 경우 자식 컴포넌트를 렌더링
};

export default ProtectedRoute;