import React from "react";
import { Outlet } from "react-router-dom";
import Navbar from "../../components/common/Navbar";
import SideBar from "../../components/SideBar";

const MyPage: React.FC = () => {
  return (
    <div className="min-h-screen flex flex-col">
      <header className="sticky top-0 bg-black z-10">
        <Navbar />
      </header>

      <div className="flex h-screen bg-black text-white">
        <SideBar />
        <div className=" items-center justify-center flex h-[900px] flex-grow">
          <Outlet />
        </div>
      </div>
    </div>
  );
};

export default MyPage;
