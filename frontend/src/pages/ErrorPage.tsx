// ErrorPage.tsx
import React from "react";
import { useNavigate, useLocation } from "react-router-dom";

const ErrorPage: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();

  // 에러 코드 받아오기 (기본 값은 404로 설정)
  const errorCode = location.state?.errorCode || 404;

  const handleHomeClick = () => {
    navigate("/home"); // HOME 버튼 클릭 시 이동할 경로 설정
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-white text-center w-screen">
      <p className="text-[200px] font-bold italic text-[#1D2F5E] -mt-20">
        Oops!
      </p>
      <p className="text-2xl font-semibold mt-12">{errorCode} ERROR</p>
      <p className="mt-6">찾을 수 없는 페이지입니다.</p>
      <p>요청하신 페이지가 사라졌거나, 잘못된 경로를 이용하셨어요!</p>
      <button
        onClick={handleHomeClick}
        className="mt-8 bg-[#4D7FFF] text-white px-8 py-1.5 rounded-lg hover:bg-blue-700 font-semibold"
      >
        HOME
      </button>
    </div>
  );
};

export default ErrorPage;
