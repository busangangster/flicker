import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { signin } from "../../apis/authApi";
import { AxiosError } from "axios";
import { IoMdCheckboxOutline, IoMdSquareOutline } from "react-icons/io";
import { IoIosArrowRoundBack } from "react-icons/io";

const backgrounds = [
  "/assets/background/background1.png",
  "/assets/background/background2.png",
  "/assets/background/background3.png",
  "/assets/background/background4.png",
  "/assets/background/background5.png",
  "/assets/background/background6.png",
];

const SignInPage: React.FC = () => {
  const [formData, setFormData] = useState({
    userId: "",
    password: "",
    rememberMe: false,
  });
  const [error, setError] = useState("");
  const [backgroundImage, setBackgroundImage] = useState(""); // 랜덤 배경 이미지 상태
  const navigate = useNavigate();

  useEffect(() => {
    const randomIndex = Math.floor(Math.random() * backgrounds.length);
    setBackgroundImage(backgrounds[randomIndex]);
  }, []);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    setError(""); // 입력 시 오류 메시지 초기화
  };

  const toggleRememberMe = () => {
    setFormData((prevData) => ({
      ...prevData,
      rememberMe: !prevData.rememberMe,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await signin({
        userId: formData.userId,
        password: formData.password,
      });

      if (response) {
        // 로그인 성공 시 홈으로 이동
        navigate("/home");
      } else {
        throw new Error("로그인 응답이 없습니다.");
      }
    } catch (error: unknown) {
      const err = error as AxiosError;
      console.error("로그인 오류:", err);

      if (err.response && err.response.status === 400) {
        setError("로그인 실패: 아이디나 비밀번호가 올바르지 않습니다.");
      } else {
        setError(
          "로그인 실패: 서버에 문제가 발생했습니다. 나중에 다시 시도하세요."
        );
      }
    }
  };

  return (
    <div
      className="min-h-screen w-screen bg-black flex items-center justify-center bg-cover bg-center"
      style={{ backgroundImage: `url(${backgroundImage})` }}
    >
      <header className="sticky top-0 bg-transparent z-20">
        <IoIosArrowRoundBack
          onClick={() => navigate(-1)}
          className="text-gray-200 cursor-pointer fixed left-4 top-5 w-10 h-10 hover:opacity-60"
        />
      </header>
      <form onSubmit={handleSubmit} className="w-full max-w-xl p-8">
        <h2 className="text-3xl font-bold mb-6 text-white text-center">
          로그인
        </h2>

        <input
          type="text"
          name="userId"
          placeholder="아이디"
          value={formData.userId}
          onChange={handleChange}
          className="w-full py-2 px-4 mb-4 border border-gray-500 rounded bg-black text-white placeholder-gray-400 focus:outline-none"
        />

        <input
          type="password"
          name="password"
          placeholder="비밀번호"
          value={formData.password}
          onChange={handleChange}
          className="w-full py-2 px-4 mb-4 border border-gray-500 rounded bg-black text-white placeholder-gray-400 focus:outline-none"
        />

        {error && <p className="text-red-500 text-xs mb-2">{error}</p>}

        <div className="flex items-center justify-between w-full mb-10">
          <label
            className="flex items-center text-gray-100 text-sm cursor-pointer"
            onClick={toggleRememberMe}
          >
            {formData.rememberMe ? (
              <IoMdCheckboxOutline className="mr-2 ml-2 text-xl text-gray-100" />
            ) : (
              <IoMdSquareOutline className="mr-2 ml-2 text-xl text-gray-100" />
            )}
            자동 로그인
          </label>
        </div>

        <button
          type="submit"
          className="w-full py-2 mb-4 text-white bg-[#4D7FFF] rounded hover:bg-blue-600 focus:outline-none"
        >
          로그인
        </button>
        <p className="text-sm text-center text-gray-200">
          Flicker와 함께 반짝이는 순간을 기록하시겠어요?&nbsp;&nbsp;&nbsp;
          <a href="/signup" className="text-gray-100 underline hover:text-gray-400">
            회원가입
          </a>
        </p>
      </form>
    </div>
  );
};

export default SignInPage;
