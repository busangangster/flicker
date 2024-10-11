import React, { useState } from "react";
import { NavLink } from "react-router-dom";
import Cookies from "js-cookie";
import { useQuery, useQueryClient } from "@tanstack/react-query"; // react-query 사용
import { fetchSideBarUserInfo } from "../apis/axios";
import { useUserQuery } from "../hooks/useUserQuery"; // useUserQuery 가져오기
import Modal from "../components/common/Modal"; // Modal 컴포넌트 불러오기
import { FaCheckCircle } from "react-icons/fa"; // 모달에 사용할 아이콘 불러오기
// import { useNavigate } from "react-router-dom";

const Sidebar: React.FC = () => {
  const queryClient = useQueryClient(); // react-query 캐시 무효화
  // const navigate = useNavigate();
  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 상태 관리
  const [modalContent, setModalContent] = useState({
    title: "",
    description: "",
    icon: FaCheckCircle,
    buttonText: "확인",
  }); // 모달의 내용 관리

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

    setModalContent({
      title: "로그아웃 완료",
      description:
        "",
      icon: FaCheckCircle,
      buttonText: "확인",
    });
    setIsModalOpen(true);

    // 모달 닫은 후 로그인 페이지로 이동
    setTimeout(() => {
      window.location.replace("/home");
    }, 1300);
    // alert("로그아웃되었습니다.");
    // window.location.replace("/home"); // /home으로 이동하면서 새로고침
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

// Sidebar를 React.memo로 감싸 리렌더링 방지
export default React.memo(Sidebar);
