import React, { useState, useEffect } from "react";
import { NavLink } from "react-router-dom";

const Navbar: React.FC = () => {
  const [menuOpen, setMenuOpen] = useState(false);
  const [animateMenu, setAnimateMenu] = useState(false);
  const [buttonText, setButtonText] = useState("MENU");

  const toggleMenu = () => {
    if (menuOpen) {
      // 메뉴 닫을 때
      setAnimateMenu(false); // 닫힘 애니메이션을 실행
      setTimeout(() => {
        setMenuOpen(false); // 애니메이션이 끝난 후에 메뉴를 숨김
        setButtonText("MENU");
      }, 300); // 애니메이션 지속 시간과 맞춤
    } else {
      // 메뉴를 열 때
      setMenuOpen(true); // 메뉴를 먼저 열고
      setTimeout(() => setAnimateMenu(true), 10); // 애니메이션을 약간의 지연 후에 시작
      setButtonText("CLOSE");
    }
  };

  useEffect(() => {
    if (!animateMenu && menuOpen) {
      // 메뉴가 닫히는 애니메이션이 실행될 때
      const timer = setTimeout(() => {
        setMenuOpen(false); // 애니메이션이 끝난 후 메뉴 숨김
      }, 300); // 애니메이션 지속 시간과 일치
      return () => clearTimeout(timer); // 타이머 정리
    }
  }, [animateMenu, menuOpen]);

  return (
    <header className="flex items-center mx-auto rounded-md mt-[25px] border border-white w-[1800px] h-[57px]">
      <div className="flex items-center justify-between w-full">
        {/* 로고 */}
        <div className="flex-none ml-[25px]">
          <NavLink to="/home" className="text-2xl font-bold text-white">
            Flicker
          </NavLink>
        </div>

        {/* 가운데 버튼 */}
        <div className="flex justify-center items-center">
          <div className="relative flex items-center">
            <div className="w-5 h-5 border border-white rounded-full"></div>
            <div className="w-5 h-5 bg-white border border-white rounded-full absolute left-3"></div>
          </div>
        </div>

        {/* 오른쪽 컨테이너 */}
        <div className="flex-none h-[57px] flex items-center relative text-white ">
          {/* 메뉴 항목들 */}
          {(menuOpen || animateMenu) && (
            <div
              className={`absolute right-[180px] flex items-center space-x-4 mr-4 transition-transform duration-300 transform text-white ${
                animateMenu
                  ? "translate-x-0 opacity-100" // 열릴 때 애니메이션
                  : "translate-x-[calc(100%-180px)] opacity-0" // 닫힐 때 애니메이션
              }`}
              style={{
                transition: "transform 0.3s ease, opacity 0.3s ease", // 애니메이션 속도 설정
                display: menuOpen || animateMenu ? "flex" : "none", // 애니메이션 중이거나 메뉴가 열려 있을 때만 표시
                // overflow: "hidden",
              }}
            >
              <NavLink
                to="/"
                className="text-white font-semibold whitespace-nowrap"
              >
                about
              </NavLink>
              <NavLink
                to="/"
                className="text-white font-semibold whitespace-nowrap"
              >
                for me
              </NavLink>
              <NavLink
                to="/"
                className="text-white font-semibold whitespace-nowrap"
              >
                movies
              </NavLink>
              <NavLink
                to="/contact"
                className="text-white font-semibold whitespace-nowrap"
              >
                contact
              </NavLink>
              <NavLink
                to="/signin"
                className="text-white font-semibold whitespace-nowrap"
              >
                login
              </NavLink>
            </div>
          )}

          {/* MENU 버튼 */}
          <button
            onClick={toggleMenu}
            className="h-full px-[50px] flex items-center justify-center text-white font-semibold hover:bg-white hover:text-black transition-colors"
          >
            {buttonText}
          </button>
        </div>
      </div>
    </header>
  );
};

export default Navbar;
