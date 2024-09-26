// frontend/src/pages/auth/PasswordResetPage.tsx
import React, { useState, useEffect } from "react";
import axios from "../../apis/axios";

const backgrounds = [
  "/assets/background/background1.png",
  "/assets/background/background2.png",
  "/assets/background/background3.png",
  "/assets/background/background4.png",
  "/assets/background/background5.png",
  "/assets/background/background6.png",
];

const PasswordResetPage: React.FC = () => {
  const [email, setEmail] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [backgroundImage, setBackgroundImage] = useState(""); // 랜덤 배경 이미지 상태

  useEffect(() => {
    // 랜덤한 배경 이미지 선택
    const randomIndex = Math.floor(Math.random() * backgrounds.length);
    setBackgroundImage(backgrounds[randomIndex]);
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setEmail(e.target.value);
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      await axios.post("/auth/request-password-reset", { email });
      setIsModalOpen(true);
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
    } catch (error) {
      alert("이메일 전송에 실패했습니다. 다시 시도해주세요.");
    }
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  return (
    <div
      className="min-h-screen w-screen bg-black flex items-center justify-center bg-cover bg-center"
      style={{ backgroundImage: `url(${backgroundImage})` }}
    >
      <form onSubmit={handleSubmit} className="w-full max-w-xl p-8">
        <h2 className="text-3xl font-bold mb-6 text-white text-center">
          비밀번호 재설정
        </h2>

        <input
          type="email"
          name="email"
          placeholder="이메일"
          value={email}
          onChange={handleChange}
          className="w-full py-2 px-4 mb-6 border border-gray-500 rounded bg-black text-white placeholder-gray-400 focus:outline-none"
        />

        <button
          type="submit"
          className="w-full py-2 mb-4 text-white bg-[#4D7FFF] rounded hover:bg-blue-600 focus:outline-none"
        >
          인증 메일 전송하기
        </button>
      </form>

      {isModalOpen && (
        <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
          <div className="bg-gray-600 p-6 rounded-2xl shadow-lg text-center">
            <h3 className="text-lg text-white font-bold mb-4">알림</h3>
            <p className="mb-6 text-white">
              인증 메일이 전송되었습니다. 메일을 확인해주세요.
            </p>
            <div className="flex justify-center">
              <button
                onClick={closeModal}
                className="py-2 px-4 bg-[#4D7FFF] text-white rounded-xl hover:bg-blue-600 focus:outline-none"
              >
                확인
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default PasswordResetPage;
