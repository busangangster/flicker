// src/components/ProtectedRoute.tsx
import React from "react";
import { Navigate } from "react-router-dom";
import { useUserQuery } from "../hooks/useUserQuery";
import { ProtectedRouteProps } from "../type";

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children }) => {
  const { data, isLoading } = useUserQuery();

  if (isLoading) {
    return <div>로딩 중...</div>; 
  }

  if (!data) {
    return <Navigate to="/signin" />; 
  }

  return <>{children}</>; 
};

export default ProtectedRoute;