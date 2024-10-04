import React, { useRef } from "react";
import { Outlet, useLocation } from "react-router-dom";
import Navbar from "../../components/common/Navbar";
import SideBar from "../../components/SideBar";

const MyPage: React.FC = () => {
  const location = useLocation();
  const sidebarRef = useRef<HTMLDivElement>(null); // 사이드바에 대한 ref 설정

  // photocard 페이지와 photocarddetail 페이지인지 확인하는 조건
  const isPhotoCardPage = location.pathname === "/mypage/photocard";
  const isPhotoCardDetailPage = /^\/mypage\/photocarddetail\/\d+$/.test(
    location.pathname
  );

  return (
    <div className="min-h-screen flex flex-col">
      <header className="sticky top-0 bg-black z-10">
        <Navbar />
      </header>

      <div className="flex h-screen bg-black text-white">
        {!isPhotoCardPage && !isPhotoCardDetailPage && (
          <div
            ref={sidebarRef}
            className="SideBar transition-opacity duration-500 opacity-100"
          >
            <SideBar />
          </div>
        )}
        <div className="items-center justify-center flex h-[900px] flex-grow mt-[90px]">
          <Outlet />
        </div>
      </div>
    </div>
  );
};

export default MyPage;