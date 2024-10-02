// Sidebar.tsx
import React from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { useUserQuery } from "../hooks/useUserQuery";
import Cookies from "js-cookie";
import { useQueryClient } from "@tanstack/react-query"; // react-query 캐시 무효화용

const Sidebar: React.FC = () => {
  const { data, error, isLoading } = useUserQuery();
  const navigate = useNavigate();
  const queryClient = useQueryClient(); // react-query 캐시 무효화

  if (!data) return <p>유저 정보가 없습니다.</p>;
  if (isLoading) return <p>로딩 중...</p>;
  if (error) return <p>유저 정보를 불러오는데 실패했습니다.</p>;

  // 로그아웃 핸들러
  const handleLogout = () => {
    // 로컬 스토리지에서 accessToken 삭제
    localStorage.removeItem("accessToken");

    // 쿠키에서 refreshToken 삭제
    Cookies.remove("refreshToken");

    // react-query 캐시 무효화 (유저 정보 초기화)
    queryClient.removeQueries({
      queryKey: ["user"], // queryKey는 필터 객체로 전달해야 함
    });

    alert("로그아웃되었습니다.");

    // 로그아웃 후 로그인 페이지로 리다이렉트
    navigate("/home");
  };

  return (
    <aside className="w-[300px] h-[830px] rounded-md bg-[#2C3751] bg-opacity-80 flex flex-col items-center py-8 ml-20 mt-[110px]">
      {/* Profile Section */}
      <div>MY PAGE</div>
      <div className="flex flex-col items-center my-8 ">
        <img
          src="/assets/George.jpg"
          alt="Profile"
          className="rounded-[90px] w-[220px] h-[170px] mb-2"
        />
        <span className="text-white font-semibold mt-3 text-[20px] mb-8">
          {data.nickname}
        </span>

        <div className="w-[200px] h-[60px] text-white flex justify-between items-center px-4">
          <div className="flex flex-col items-center">
            <span className="text-xl font-bold">45</span>
            <span className="text-sm">reviews</span>
          </div>
          <div className="h-8 border-r border-gray-400"></div>
          <div className="flex flex-col items-center">
            <span className="text-xl font-bold">3,225</span>
            <span className="text-sm">likes</span>
          </div>
        </div>
      </div>

      {/* Navigation Menu */}
      <nav className="flex flex-col space-y-8 text-white text-[20px] italic mt-[20px]">
        <NavLink
          to="/mypage/myinformation"
          className={({ isActive }) =>
            isActive ? "font-semibold underline" : "hover:underline"
          }
        >
          Information
        </NavLink>

        <NavLink
          to="/mypage/favorite"
          className={({ isActive }) =>
            isActive ? "font-semibold underline" : "hover:underline"
          }
        >
          Favorites
        </NavLink>
        <NavLink
          to="/mypage/dislike"
          className={({ isActive }) =>
            isActive ? "font-semibold underline" : "hover:underline"
          }
        >
          Dislike
        </NavLink>
      </nav>

      {/* Logout Button */}
      <button
        onClick={handleLogout}
        className="mt-auto mb-4 px-4 h-[40px] w-[140px] bg-gray-600 rounded-md text-white text-[20px]"
      >
        logout
      </button>
    </aside>
  );
};

// Sidebar를 React.memo로 감싸 리렌더링 방지
export default React.memo(Sidebar);
