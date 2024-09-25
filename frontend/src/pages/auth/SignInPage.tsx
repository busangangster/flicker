import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { signin } from "../../apis/authApi";
import UsAndThem from "../../assets/background/UsAndThem.png";
import { isAxiosError } from "axios"; // axios 모듈에서 isAxiosError 가져오기

const SignInPage: React.FC = () => {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    rememberMe: false,
  });

  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === "checkbox" ? checked : value,
    });
    setError(""); // 입력 시 오류 메시지 초기화
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      // API 호출: 서버에서 id와 password를 사용하도록 수정
      const response = await signin(formData.username, formData.password);

      // JWT 토큰을 로컬 스토리지에 저장
      const { token, accessToken } = response.data; // 'accessToken' 또는 다른 키 이름으로 확인
      const authToken = token || accessToken; // 'token'과 'accessToken' 중 존재하는 값 선택

      if (authToken) {
        localStorage.setItem("token", authToken);
        alert("로그인 성공! \nFlicker에서 반짝이는 순간을 기록하세요.");
        navigate("/"); // 메인 페이지로 이동
      } else {
        throw new Error("토큰이 반환되지 않았습니다."); // 토큰이 없는 경우 예외 처리
      }
    } catch (error: unknown) {
      console.error("로그인 오류:", error);

      // error가 AxiosError 타입인지 확인
      if (isAxiosError(error)) {
        // AxiosError 타입일 경우, error.response에 안전하게 접근
        if (error.response && error.response.status === 400) {
          setError("로그인 실패: 아이디나 비밀번호가 올바르지 않습니다.");
        } else {
          setError(
            "로그인 실패: 서버에 문제가 발생했습니다. 나중에 다시 시도하세요."
          );
        }
      } else {
        // 일반적인 오류 처리
        setError("로그인 중 알 수 없는 오류가 발생했습니다.");
      }
    }
  };

  // 비밀번호 재설정 클릭 핸들러
  const handlePasswordResetClick = () => {
    navigate("/passwordreset"); // '/passwordreset' 경로로 이동
  };

  return (
    <div
      className="min-h-screen w-screen bg-black flex items-center justify-center bg-cover bg-center"
      style={{ backgroundImage: `url(${UsAndThem})` }}
    >
      <form onSubmit={handleSubmit} className="w-full max-w-xl p-8">
        <h2 className="text-3xl font-bold mb-6 text-white text-center">
          로그인
        </h2>

        <input
          type="text"
          name="id"
          placeholder="아이디"
          value={formData.username}
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
          <label className="flex items-center text-gray-100 text-sm">
            <input
              type="checkbox"
              name="rememberMe"
              checked={formData.rememberMe}
              onChange={handleChange}
              className="mr-2 ml-2"
            />
            자동 로그인
          </label>
          <a
            type="button"
            className="text-sm text-gray-100 underline hover:underline mr-2"
            onClick={handlePasswordResetClick}
          >
            비밀번호 재설정
          </a>
        </div>

        <button
          type="submit"
          className="w-full py-2 mb-4 text-white bg-[#4D7FFF] rounded hover:bg-blue-600 focus:outline-none"
        >
          로그인
        </button>

        <p className="text-sm text-center text-gray-200">
          Flicker와 함께 반짝이는 순간을 기록하시겠어요?&nbsp;&nbsp;&nbsp;{" "}
          <a href="/signup" className="text-gray-100 underline hover:underline">
            회원가입
          </a>
        </p>
      </form>
    </div>
  );
};

export default SignInPage;
