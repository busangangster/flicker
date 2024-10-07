import React from "react";
import { NavLink } from "react-router-dom";
import Cookies from "js-cookie";
import { useQuery, useQueryClient } from "@tanstack/react-query"; // react-query 사용
import { fetchSideBarUserInfo } from "../apis/axios";
import { useUserQuery } from "../hooks/useUserQuery"; // useUserQuery 가져오기

const Sidebar: React.FC = () => {
  const queryClient = useQueryClient(); // react-query 캐시 무효화

  // 로그인한 유저의 정보를 가져오는 useUserQuery 사용
  const { data: userData } = useUserQuery();
  const userSeq = userData?.userSeq; // userSeq 추출

  // 유저 정보가 있을 때만 sidebarUserInfo를 가져옴
  const { data, error, isLoading } = useQuery({
    queryKey: ["sidebarUserInfo", userSeq], // queryKey에 userSeq 포함
    queryFn: () => fetchSideBarUserInfo(userSeq!), // userSeq가 있을 때만 fetch 함수 호출
    enabled: !!userSeq, // userSeq가 존재할 때만 쿼리 실행
  });

  if (isLoading) return <p>로딩 중...</p>;
  if (error) return <p>유저 정보를 불러오는데 실패했습니다.</p>;

  // 로그아웃 핸들러
  const handleLogout = () => {
    localStorage.removeItem("accessToken"); // 로컬 스토리지에서 accessToken 삭제
    Cookies.remove("refreshToken"); // 쿠키에서 refreshToken 삭제

    // react-query 캐시 무효화 (유저 정보 초기화)
    queryClient.removeQueries({
      queryKey: ["sidebarUserInfo"], // queryKey를 정확하게 설정
    });

    alert("로그아웃되었습니다.");
    window.location.replace("/home"); // /home으로 이동하면서 새로고침
  };

  return (
    <aside className="w-[300px] h-5/6 rounded-md bg-[#2C3751] bg-opacity-80 flex flex-col items-center py-8 ml-20 mt-[110px]">
      {/* Profile Section */}
      <div>MY PAGE</div>
      <div className="flex flex-col items-center my-8 ">
        <img
          src="/assets/George.jpg"
          alt="Profile"
          className="rounded-full w-[150px] h-[150px] mb-2"
        />
        <span className="text-white font-semibold mt-3 text-[20px] mb-8">
          {userData?.nickname}
        </span>

        <div className="w-[200px] h-[60px] text-white flex justify-between items-center px-4">
          <div className="flex flex-col items-center">
            <span className="text-xl font-bold">{data.data.reviewCnt}</span>
            <span className="text-sm">reviews</span>
          </div>
          <div className="h-8 border-r border-gray-400"></div>
          <div className="flex flex-col items-center">
            <span className="text-xl font-bold">{data.data.likes}</span>
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
