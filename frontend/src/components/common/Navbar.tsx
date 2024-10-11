import React, { useState, useEffect } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { useUserQuery } from "../../hooks/useUserQuery";
import { IoBan } from "react-icons/io5"; 
import Modal from "./Modal"; 


const Navbar: React.FC = () => {
  const [menuOpen, setMenuOpen] = useState(false);
  const [animateMenu, setAnimateMenu] = useState(false);
  const [buttonText, setButtonText] = useState("MENU");
  const { data } = useUserQuery();
  const navigate = useNavigate();

  const [isVisible, setIsVisible] = useState(true); 
  const [scrollPosition, setScrollPosition] = useState(0); 
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalTitle, setModalTitle] = useState("");
  const [modalDescription, setModalDescription] = useState("");


  useEffect(() => {
    const handleScroll = () => {
      const currentScrollPos = window.pageYOffset;

      if (currentScrollPos > scrollPosition && currentScrollPos > 50) {
        setIsVisible(false);
      } else {
        setIsVisible(true);
      }

      setScrollPosition(currentScrollPos);
    };

    window.addEventListener("scroll", handleScroll);

    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, [scrollPosition]);

  const handleMouseEnter = () => {
    if (!isVisible && window.pageYOffset > 50) {
      setIsVisible(true);
    }
  };

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
      navigate("/recommend");
    } else {
      setModalTitle("접근 불가");
      setModalDescription("로그인한 사용자만 접근 가능합니다!");
      setIsModalOpen(true); 
    }
  };

  const handlePhotoBookClick = () => {
    if (data) {
      navigate("/photobook");
    } else {
      setModalTitle("접근 불가");
      setModalDescription("로그인한 사용자만 접근 가능합니다!");
      setIsModalOpen(true); 
    }
  };

  const handleModalClose = () => {
    setIsModalOpen(false); 
    navigate("/signin"); 
  };

  const goToSignin = () => {
    navigate("/signin");
  };

  const goToMyPage = () => {
    navigate("/mypage/myinformation");
  };

  return (
    <div
      className={`w-full bg-black fixed z-20 transition-all duration-300 ${
        isVisible ? "top-0" : "-top-16"
      }`} 
      onMouseEnter={handleMouseEnter} 
    >
      <header className="flex items-center mx-auto border-b bg-black border-gray-700 w-full">
        <div className="flex items-center justify-between w-full">
          <div className="flex-none ml-[25px]">
            <NavLink
              to="/home"
              className="text-2xl font-bold text-white italic"
            >
              Flicker
            </NavLink>
          </div>

          {/* 오른쪽 컨테이너 */}
          <div className="flex-none h-[57px] flex items-center relative text-white">
            {/* 메뉴 항목들 */}
            {(menuOpen || animateMenu) && (
              <div
                className={`absolute right-[180px] flex items-center space-x-4 mr-4 transition-transform duration-300 transform text-white ${
                  animateMenu
                    ? "translate-x-0 opacity-100"
                    : "translate-x-[calc(100%-180px)] opacity-0"
                }`}
                style={{
                  transition: "transform 0.3s ease, opacity 0.3s ease",
                  display: menuOpen || animateMenu ? "flex" : "none",
                }}
              >
                <NavLink
                  to="/servicedetail"
                  className="text-white font-semibold whitespace-nowrap hover:opacity-70"
                >
                  About
                </NavLink>
                <button
                  onClick={handleForMeClick}
                  className="text-white font-semibold whitespace-nowrap hover:opacity-70"
                >
                  For You
                </button>
                <NavLink
                  to="/movies"
                  className="text-white font-semibold whitespace-nowrap hover:opacity-70"
                >
                  Movies
                </NavLink>
                <NavLink
                  to="/contact"
                  className="text-white font-semibold whitespace-nowrap hover:opacity-70"
                >
                  Contact
                </NavLink>
                <button
                  onClick={handlePhotoBookClick}
                  className="text-white font-semibold whitespace-nowrap hover:opacity-70"
                >
                  PhotoBook
                </button>
                <div>
                  {data ? (
                    <button
                      onClick={goToMyPage}
                      className="text-white font-semibold whitespace-nowrap hover:opacity-70"
                    >
                      Mypage
                    </button>
                  ) : (
                    <button
                      onClick={goToSignin}
                      className="text-white font-semibold whitespace-nowrap hover:opacity-70"
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
      {isModalOpen && (
        <Modal
          onClose={handleModalClose}
          title={modalTitle}
          description={modalDescription}
          icon={IoBan} 
          buttonText="확인"
        />
      )}
    </div>
  );
};

export default Navbar;
