// Mypage.tsx
import React from "react";
import { Outlet } from "react-router-dom"; // Outlet을 가져옵니다.

const Mypage: React.FC = () => {
  return (
    <div>
      <h1>Mypage</h1>
      {/* 자식 경로가 렌더링될 위치 */}
      <Outlet />
    </div>
  );
};

export default Mypage;
