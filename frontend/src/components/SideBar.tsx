import React from "react";
import { NavLink } from "react-router-dom";

const Sidebar: React.FC = () => {
  return (
    <aside className="w-[300px] h-[830px] rounded-md bg-[#2C3751] bg-opacity-80 flex flex-col items-center py-8 ml-20 my-[20px]">
      {/* Profile Section */}
      <div>MY PAGE</div>
      <div className="flex flex-col items-center my-8 ">
        <img
          src="/assets/George.jpg"
          alt="Profile"
          className="rounded-[90px] w-[220px] h-[170px] mb-2"
        />
        <span className="text-white font-semibold mt-3 text-[20px] mb-8">
          busangangster
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
          to="/mypage/photobook"
          className={({ isActive }) =>
            isActive ? "font-semibold underline" : "hover:underline"
          }
        >
          PhotoBook
        </NavLink>
      </nav>

      {/* Logout Button */}
      <button className="mt-auto mb-4 px-4 h-[40px] w-[140px] bg-gray-600 rounded-md text-white text-[20px]">
        logout
      </button>
    </aside>
  );
};

// Sidebar를 React.memo로 감싸 리렌더링 방지
export default React.memo(Sidebar);
