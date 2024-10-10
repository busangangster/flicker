// src/Pages/ContactPage.tsx
import React from "react";
import Navbar from "../../components/common/Navbar";
import Member from "../../components/Member";
import team from "/assets/service/team.jpg";

const ContactPage: React.FC = () => {
  return (
    <div className="flex flex-col h-screen bg-black text-white pb-10 overflow-y-auto">
      {/* NavBar 고정 */}
      <header className="sticky top-0 bg-transparent z-10">
        <Navbar />
      </header>

      {/* 팀 소개 영역 */}
      <section className="flex flex-col items-center justify-center mt-[250px] space-y-10 px-80 mb-12">
        <div className="flex items-center justify-center ">
          {/* 팀 이미지 */}
          <img
            src={team}
            alt="Team"
            className="w-[1000px] h-[400px] mr-10 object-cover rounded-md "
          />
          {/* 팀 설명 */}
          <div className="text-left">
            <h1 className="text-[45px] font-bold">
              Wassup! <br /> We are "
              <span className="text-[#4D7FFF]">6 CAN DO IT !</span>"
            </h1>
            <p className="mt-6 text-2xl leading-relaxed">
              저희는 빅데이터를 활용하여 영화를 추천해주는 "Flicker"라는
              서비스를 개발했습니다. 든든한 팀장을 필두로하여, 뛰어난 팀원들과
              끈끈한 팀워크로 협업을 진행했습니다.
            </p>
          </div>
        </div>
      </section>

      {/* Meet Expert Team 영역 */}
      <section className="mt-16 text-center px-80">
        <p className="text-[25px] text-[#4D7FFF]">our team</p>
        <h2 className="text-[50px] font-bold">
          Meet Expert <span className="text-[#4D7FFF]">Team.</span>
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-10 mt-10">
          <Member
            name="HaHyul Kim"
            role="Frontend"
            description={`UX / UI 개발. \n 데이터 캐싱 및 서버 비동기 통신`}
            githubUrl="https://github.com/busangangster"
            emailUrl="gkgbf1034@gmail.com"
            imgSrc="/assets/team/hyulKim.jpg"
          />
          <Member
            name="HyunJeong Cho"
            role="Frontend"
            description={`UX / UI 디자인 및 개발. \n JWT 및 서버 비동기 통신`}
            githubUrl="https://github.com/hyunjeongg11"
            emailUrl="guswjd4585@gmail.com"
            imgSrc="/assets/team/jeong.jpg"
          />
          <Member
            name="JiHwan Gong"
            role="Backend"
            description={`데이터 수집. \n 영화 도메인 서버/BFF 서버 설계 및 구현`}
            githubUrl="https://github.com/izgnok"
            emailUrl="rinch12332@gmail.com"
            imgSrc="/assets/team/jihwan.jpg"
          />
          <Member
            name="DongGyu Oh"
            role="Backend"
            description={`유저/리뷰 도메인 개발. \n 시스템 아키텍처 설계, 부하 테스트`}
            githubUrl="https://github.com/Eastplanet"
            emailUrl="ehdrb1645@gmail.com"
            imgSrc="/assets/team/dong.png"
          />
          <Member
            name="JaeChan Lee"
            role="Infra"
            description={`인프라 아키텍처, CI/CD 구축. \n 추천 모델 훈련 및 서버 구현`}
            githubUrl="https://github.com/jaechanjj"
            emailUrl="jaechanjj@gmail.com"
            imgSrc="/assets/team/chan.jpg"
          />
          <Member
            name="JaeYoung Choi"
            role="Backend"
            description={`배치 서버 구현 및 ERD 설계. \n 추천 모델 훈련 및 서버 구현`}
            githubUrl="https://github.com/wodyddldl333"
            emailUrl="wodyddldl333@naver.com"
            imgSrc="/assets/team/jae.jpg"
          />
        </div>
      </section>
    </div>
  );
};

export default ContactPage;
