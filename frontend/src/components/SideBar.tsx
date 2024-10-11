import React, { useState } from "react";
import { NavLink } from "react-router-dom";
import Cookies from "js-cookie";
import { useQuery, useQueryClient } from "@tanstack/react-query"; // react-query 사용
import { fetchSideBarUserInfo } from "../apis/axios";
import { useUserQuery } from "../hooks/useUserQuery"; // useUserQuery 가져오기
import Modal from "../components/common/Modal"; // Modal 컴포넌트 불러오기
import { FaCheckCircle } from "react-icons/fa"; // 모달에 사용할 아이콘 불러오기

const Sidebar: React.FC = () => {
  const queryClient = useQueryClient(); 
  const [isModalOpen, setIsModalOpen] = useState(false); 
  const [modalContent, setModalContent] = useState({
    title: "",
    description: "",
    icon: FaCheckCircle,
    buttonText: "확인",
  }); 

  const { data: userData } = useUserQuery();
  const userSeq = userData?.userSeq; 

  // 유저 정보가 있을 때만 sidebarUserInfo를 가져옴
  const { data, error, isLoading } = useQuery({
    queryKey: ["sidebarUserInfo", userSeq], 
    queryFn: () => fetchSideBarUserInfo(userSeq!), 
    enabled: !!userSeq, 
  });

  if (isLoading) return <p>로딩 중...</p>;
  if (error) return <p>유저 정보를 불러오는데 실패했습니다.</p>;

  const handleLogout = () => {
    localStorage.removeItem("accessToken"); 
    Cookies.remove("refreshToken"); 
    queryClient.removeQueries({
      queryKey: ["sidebarUserInfo"], 
    });

    setModalContent({
      title: "로그아웃 완료",
      description:
        "",
      icon: FaCheckCircle,
      buttonText: "확인",
    });
    setIsModalOpen(true);
    setTimeout(() => {
      window.location.replace("/home");
    }, 1300);
  };

  return (
    <aside className="w-[300px] h-5/6 rounded-md bg-[#2C3751] bg-opacity-80 flex flex-col items-center py-8 ml-20 mt-[110px]">
      {/* Profile Section */}
      <div>MY PAGE</div>
      <div
        className="flex flex-col items-center my-8
       rounded-full bg-gray-500 w-[150px] h-[150px] justify-center text-white text-[80px]"
      >
        {userData?.nickname.charAt(0)}
      </div>
        <span className="text-white font-semibold mt-3 text-[20px] mb-8">
          {userData?.nickname}
        </span>

        <div className="w-[200px] h-[60px] text-white flex justify-between items-center px-4">
          <div className="flex flex-col items-center">
            <span className="text-xl font-bold" lang="ko">{data.data.reviewCnt}</span>
            <span className="text-sm">reviews</span>
          </div>
          <div className="h-8 border-r border-gray-400"></div>
          <div className="flex flex-col items-center">
            <span className="text-xl font-bold" lang="ko">{data.data.likes}</span>
            <span className="text-sm">likes</span>
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
      {isModalOpen && (
        <Modal
          onClose={() => setIsModalOpen(false)}
          title={modalContent.title}
          description={modalContent.description}
          icon={modalContent.icon}
          buttonText={modalContent.buttonText}
          iconColor="#20BD4D"
        />
      )}
    </aside>
  );
};

export default React.memo(Sidebar);
