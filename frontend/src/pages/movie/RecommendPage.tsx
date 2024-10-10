  import React, { useState } from "react";
  import { useNavigate } from "react-router-dom";
  import Navbar from "../../components/common/Navbar";
  import theaterDoorImage from "/assets/movie/theater7.jpg";
  import left_left_door from "/assets/movie/left_left_door.jpg";
  import left_right_door from "/assets/movie/left_right_door.jpg";
  import right_left_door from "/assets/movie/right_left_door.jpg";
  import right_right_door from "/assets/movie/right_right_door.jpg";
  import "../../css/RecommendPage.css"; 

  const RecommendPage: React.FC = () => {
    const navigate = useNavigate();
    const [isLeftOpen, setIsLeftOpen] = useState(false);
    const [isRightOpen, setIsRightOpen] = useState(false);
    const [isZoomed, setIsZoomed] = useState(false); 

    // 왼쪽 문 클릭 시 애니메이션 및 이동
    const gotoLeftRecommend = () => {
      setIsLeftOpen(true);
      setIsZoomed(true); 
      setTimeout(() => {
        navigate("/recommendlist/action");
      }, 1000); // 1초 후 이동
    };

    // 오른쪽 문 클릭 시 애니메이션 및 이동
    const gotoRightRecommend = () => {
      setIsRightOpen(true);
      setIsZoomed(true); 
      setTimeout(() => {
        navigate("/recommendlist/review");
      }, 1000); // 1초 후 이동
    };

    return (
      <div className="flex flex-col h-screen bg-black">
        <header className="sticky top-0 bg-transparent z-10">
          <Navbar />
        </header>

        {/* 반짝이는 동그라미 추가 */}
        {/* <div>
          {Array.from({ length: 26 }).map((_, index) => (
            <div
              key={index}
              className="twinkling-circle z-10"
              style={{
                position: "absolute",
                top: "8.5%",
                left: `${27.3 + index * 1.815}%`, // left 값을 1.8씩 증가
              }}
            ></div>
          ))}
        </div>
        <div>
          {Array.from({ length: 10 }).map((_, index) => (
            <div
              key={index}
              className="twinkling-circle z-10"
              style={{
                position: "absolute",
                top: "26.5%",
                left: `${27.3 + index * 1.815}%`, // left 값을 1.8씩 증가
              }}
            ></div>
          ))}
        </div>
        <div>
          {Array.from({ length: 15 }).map((_, index) => (
            <div
              key={index}
              className="twinkling-circle z-10"
              style={{
                position: "absolute",
                top: "26.5%",
                left: `${46.5 + index * 1.88}%`, // left 값을 1.8씩 증가
              }}
            ></div>
          ))}
        </div> */}

        <div
          className={`relative flex items-center justify-center w-full h-full door-container ${
            isZoomed ? "zoom-door-bottom" : ""
          }`}
          style={{
            backgroundImage: `url(${theaterDoorImage})`,
            backgroundSize: "cover",
            backgroundPosition: "center",
          }}
        >
          {/* 왼쪽 문 */}
          {/* <img
            src={left_left_door}
            alt="left_left_door"
            className={`absolute h-[54.5vh] w-[8.5vw] door left-door-hover ${
              isLeftOpen ? "open-left" : ""
            }`}
            style={{
              left: "31.5vw",
              top: "40vh",
            }}
            onClick={gotoLeftRecommend}
          />
          <img
            src={left_right_door}
            alt="left_right_door"
            className={`absolute h-[54.5vh] w-[8.5vw] door ${
              isLeftOpen ? "open-right" : ""
            }`}
            style={{
              left: "39.8vw",
              top: "40vh",
            }}
            onClick={gotoLeftRecommend}
          /> */}

          {/* 오른쪽 문 */}
          {/* <img
            src={right_left_door}
            alt="right_left_door"
            className={`absolute h-[54.5vh] w-[8.3vw] door left-door-hover ${
              isRightOpen ? "open-left" : ""
            }`}
            style={{
              left: "55vw",
              top: "40vh",
            }}
            onClick={gotoRightRecommend}
          />
          <img
            src={right_right_door}
            alt="right_right_door"
            className={`absolute h-[54.5vh] w-[8.3vw] door ${
              isRightOpen ? "open-right" : ""
            }`}
            style={{
              left: "63vw",
              top: "40vh",
            }}
            onClick={gotoRightRecommend}
          /> */}

          {/* f11 확대할 때의 문 */}
          {/* 왼쪽 문 */}
          <img
            src={left_left_door}
            alt="left_left_door"
            className={`absolute h-[52vh] w-[10vw] door ${
              isLeftOpen ? "open-left" : ""
            }`}
            style={{
              left: "29vw",
              top: "40vh",
            }}
            onClick={gotoLeftRecommend}
          />
          <img
            src={left_right_door}
            alt="left_right_door"
            className={`absolute h-[52vh] w-[10vw] door ${
              isLeftOpen ? "open-right" : ""
            }`}
            style={{
              left: "38.5vw",
              top: "40vh",
            }}
            onClick={gotoLeftRecommend}
          />

          {/* 오른쪽 문 */}
          <img
            src={right_left_door}
            alt="right_left_door"
            className={`absolute h-[52vh] w-[9.5vw] door ${
              isRightOpen ? "open-left" : ""
            }`}
            style={{
              left: "55.5vw",
              top: "40vh",
            }}
            onClick={gotoRightRecommend}
          />
          <img
            src={right_right_door}
            alt="right_right_door"
            className={`absolute h-[52vh] w-[9.5vw] door ${
              isRightOpen ? "open-right" : ""
            }`}
            style={{
              left: "65vw",
              top: "40vh",
            }}
            onClick={gotoRightRecommend}
          />
        </div>
      </div>
    );
  };

  export default RecommendPage;
