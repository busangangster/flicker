// Navbar.js
import React, { useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { useUserQuery } from "../../hooks/useUserQuery";

const Navbar: React.FC = () => {
  const [menuOpen, setMenuOpen] = useState(false);
  const [animateMenu, setAnimateMenu] = useState(false);
  const [buttonText, setButtonText] = useState("MENU");
  const { data } = useUserQuery();
  const navigate = useNavigate();

  const toggleMenu = () => {
    if (menuOpen) {
      setAnimateMenu(false);
      setTimeout(() => {
        setMenuOpen(false);
        setButtonText("MENU");
      }, 300);
    } else {
      setMenuOpen(true);
      setTimeout(() => setAnimateMenu(true), 10);
      setButtonText("CLOSE");
    }
  };

  const handleForMeClick = () => {
    if (data) {
      // 로그인된 경우 /recommend 페이지로 이동
      navigate("/recommend");
    } else {
      // 로그인되지 않은 경우 /signin 페이지로 이동
      alert("로그인한 사용자만 접근 가능합니다 !");
      navigate("/signin");
    }
  };

  const goToSignin = () => {
    navigate("/signin");
  };

  const goToMyPage = () => {
    navigate("/mypage/myinformation");
  };

  return (
    <div className={`w-full bg-black absolute z-20`}>
      <header className="flex items-center mx-auto rounded-md mt-[25px] border bg-black border-white w-[1800px] h-[57px]">
        <div className="flex items-center justify-between w-full">
          {/* 로고 */}
          <div className="flex-none ml-[25px]">
            <NavLink
              to="/home"
              className="text-2xl font-bold text-white italic"
            >
              Flicker
            </NavLink>
          </div>

          {/* 라이트 모드 & 다크 모드 버튼 */}
          {/* <div className="flex space-x-4 justify-center items-center">
            <div className="relative flex items-center">
              <button
                onClick={setLightMode}
                className={`w-5 h-5 bg-white border border-white rounded-full absolute left-3 ${
                  !isDarkMode ? "bg-white text-black" : "text-white"
                }`}
              ></button>
              <button
                onClick={setDarkMode}
                className={`w-5 h-5 border border-white rounded-full ${
                  isDarkMode ? "bg-black text-white" : "text-black"
                }`}
              ></button>
            </div>
          </div> */}

          {/* 오른쪽 컨테이너 */}
          <div className="flex-none h-[57px] flex items-center relative text-white">
            {/* 메뉴 항목들 */}
            {(menuOpen || animateMenu) && (
              <div
                className={`absolute right-[180px] flex items-center space-x-4 mr-4 transition-transform duration-300 transform text-white ${
                  animateMenu
                    ? "translate-x-0 opacity-100" // 열릴 때 애니메이션
                    : "translate-x-[calc(100%-180px)] opacity-0" // 닫힐 때 애니메이션
                }`}
                style={{
                  transition: "transform 0.3s ease, opacity 0.3s ease",
                  display: menuOpen || animateMenu ? "flex" : "none",
                }}
              >
                <NavLink
                  to="/servicedetail"
                  className="text-white font-semibold whitespace-nowrap"
                >
                  About
                </NavLink>
                <button
                  onClick={handleForMeClick}
                  className="text-white font-semibold whitespace-nowrap"
                >
                  For Me
                </button>
                <NavLink
                  to="/movies"
                  className="text-white font-semibold whitespace-nowrap"
                >
                  Movies
                </NavLink>
                <NavLink
                  to="/contact"
                  className="text-white font-semibold whitespace-nowrap"
                >
                  Contact
                </NavLink>
                <div>
                  {data ? (
                    <button
                      onClick={goToMyPage}
                      className="text-white font-semibold whitespace-nowrap"
                    >
                      Mypage
                    </button>
                  ) : (
                    <button
                      onClick={goToSignin}
                      className="text-white font-semibold whitespace-nowrap"
                    >
                      Login
                    </button>
                  )}
                </div>
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
    </div>
  );
};

export default Navbar;
