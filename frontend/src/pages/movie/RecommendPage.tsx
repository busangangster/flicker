import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../../components/common/Navbar";
import theaterDoorImage from "/assets/movie/theater7.jpg";
import left_left_door from "/assets/movie/left_left_door.jpg";
import left_right_door from "/assets/movie/left_right_door.jpg";
import right_left_door from "/assets/movie/right_left_door.jpg";
import right_right_door from "/assets/movie/right_right_door.jpg";
import "../../css/RecommendPage.css"; // CSS 파일에서 애니메이션 정의

const RecommendPage: React.FC = () => {
  const navigate = useNavigate();
  const [isLeftOpen, setIsLeftOpen] = useState(false);
  const [isRightOpen, setIsRightOpen] = useState(false);
  const [isZoomed, setIsZoomed] = useState(false); // 배경 확대 상태
  const [tooltipPosition, setTooltipPosition] = useState({ x: 0, y: 0 });
  const [tooltipVisible, setTooltipVisible] = useState(false);
  const [isLargeScreen, setIsLargeScreen] = useState<boolean>(
    window.innerHeight >= 1028
  ); // 기본적으로 현재 창 크기 확인

  // 윈도우 크기 변경 감지
  useEffect(() => {
    const handleResize = () => {
      setIsLargeScreen(window.innerHeight >= 1028); // 1028px 이상일 때 true
    };

    window.addEventListener("resize", handleResize);

    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

  const handleMouseMove = (event: React.MouseEvent) => {
    setTooltipPosition({
      x: event.clientX + 10, // 마우스 커서 우측에 10px 떨어지도록 설정
      y: event.clientY + 10, // 마우스 커서 아래에 10px 떨어지도록 설정
    });
  };

  const showTooltip = () => {
    setTooltipVisible(true);
  };

  const hideTooltip = () => {
    setTooltipVisible(false);
  };

  // 왼쪽 문 클릭 시 애니메이션 및 이동
  const gotoLeftRecommend = () => {
    setIsLeftOpen(true);
    setIsZoomed(true); // 문 아래쪽 부분 확대 시작
    setTimeout(() => {
      navigate("/recommendlist/action");
    }, 1000); // 1초 후 이동
  };

  // 오른쪽 문 클릭 시 애니메이션 및 이동
  const gotoRightRecommend = () => {
    setIsRightOpen(true);
    setIsZoomed(true); // 문 아래쪽 부분 확대 시작
    setTimeout(() => {
      navigate("/recommendlist/review");
    }, 1000); // 1초 후 이동
  };

  return (
    <div className="flex flex-col h-screen bg-black overflow-hidden">
      <header className="sticky top-0 bg-transparent z-10">
        <Navbar />
      </header>

      <div
        className={`relative flex items-center justify-center w-full h-full door-container ${
          isZoomed ? "zoom-door-bottom" : ""
        }`}
        style={{
          backgroundImage: `url(${theaterDoorImage})`,
          backgroundSize: "cover",
          backgroundPosition: "center",
        }}
        onMouseMove={handleMouseMove}
      >
        {isLargeScreen ? (
          <>
            {/* 왼쪽 문 */}
            <img
              src={left_left_door}
              alt="left_left_door"
              className={`absolute h-[52vh] w-[10vw] door cursor-pointer ${
                isLeftOpen ? "open-left" : ""
              }`}
              style={{
                left: "29vw",
                top: "40vh",
              }}
              onClick={gotoLeftRecommend}
              onMouseEnter={showTooltip}
              onMouseLeave={hideTooltip}
            />
            <img
              src={left_right_door}
              alt="left_right_door"
              className={`absolute h-[52vh] w-[10vw] door cursor-pointer ${
                isLeftOpen ? "open-right" : ""
              }`}
              style={{
                left: "38.5vw",
                top: "40vh",
              }}
              onClick={gotoLeftRecommend}
              onMouseEnter={showTooltip}
              onMouseLeave={hideTooltip}
            />

            {/* 오른쪽 문 */}
            <img
              src={right_left_door}
              alt="right_left_door"
              className={`absolute h-[52vh] w-[9.5vw] door cursor-pointer ${
                isRightOpen ? "open-left" : ""
              }`}
              style={{
                left: "55.5vw",
                top: "40vh",
              }}
              onClick={gotoRightRecommend}
              onMouseEnter={showTooltip}
              onMouseLeave={hideTooltip}
            />
            <img
              src={right_right_door}
              alt="right_right_door"
              className={`absolute h-[52vh] w-[9.5vw] door cursor-pointer ${
                isRightOpen ? "open-right" : ""
              }`}
              style={{
                left: "65vw",
                top: "40vh",
              }}
              onClick={gotoRightRecommend}
              onMouseEnter={showTooltip}
              onMouseLeave={hideTooltip}
            />
          </>
        ) : (
          <>
            {/* 작은 화면일 때 사용하는 문 */}
            <img
              src={left_left_door}
              alt="left_left_door"
              className={`absolute h-[54.5vh] w-[8.5vw] door cursor-pointer ${
                isLeftOpen ? "open-left" : ""
              }`}
              style={{
                left: "31.5vw",
                top: "40vh",
              }}
              onClick={gotoLeftRecommend}
              onMouseEnter={showTooltip}
              onMouseLeave={hideTooltip}
            />
            <img
              src={left_right_door}
              alt="left_right_door"
              className={`absolute h-[54.5vh] w-[8.5vw] door cursor-pointer ${
                isLeftOpen ? "open-right" : ""
              }`}
              style={{
                left: "39.8vw",
                top: "40vh",
              }}
              onClick={gotoLeftRecommend}
              onMouseEnter={showTooltip}
              onMouseLeave={hideTooltip}
            />

            {/* 오른쪽 문 */}
            <img
              src={right_left_door}
              alt="right_left_door"
              className={`absolute h-[54.5vh] w-[8.3vw] door cursor-pointer ${
                isRightOpen ? "open-left" : ""
              }`}
              style={{
                left: "55vw",
                top: "40vh",
              }}
              onClick={gotoRightRecommend}
              onMouseEnter={showTooltip}
              onMouseLeave={hideTooltip}
            />
            <img
              src={right_right_door}
              alt="right_right_door"
              className={`absolute h-[54.5vh] w-[8.3vw] door cursor-pointer ${
                isRightOpen ? "open-right" : ""
              }`}
              style={{
                left: "63vw",
                top: "40vh",
              }}
              onClick={gotoRightRecommend}
              onMouseEnter={showTooltip}
              onMouseLeave={hideTooltip}
            />
          </>
        )}

        {/* 툴팁 추가 */}
        <div
          className={`custom-tooltip ${tooltipVisible ? "visible" : ""}`}
          style={{
            top: `${tooltipPosition.y}px`,
            left: `${tooltipPosition.x}px`,
          }}
        >
          문을 클릭하여, 영화를 추천받으세요!
        </div>
      </div>
    </div>
  );
};

export default RecommendPage;
