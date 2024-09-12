// frontend/src/pages/auth/PasswordChangePage.tsx
import React, { useState } from "react";
import { useParams } from "react-router-dom";
import axios from "../../apis/axios";
import UsAndThem from "../../assets/background/UsAndThem.png";
// 타입 지정!

const PasswordChangePage: React.FC = () => {
  const { token } = useParams<{ token: string }>(); // URL의 토큰을 가져옴
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState(""); // 비밀번호 일치 오류 메시지 상태 추가

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    if (name === "password") {
      setPassword(value);
    } else if (name === "confirmPassword") {
      setConfirmPassword(value);
    }

    // 비밀번호 일치 확인
    if (name === "confirmPassword" && password !== value) {
      setError("비밀번호가 일치하지 않습니다.");
    } else if (
      name === "password" &&
      confirmPassword &&
      confirmPassword !== value
    ) {
      setError("비밀번호가 일치하지 않습니다.");
    } else {
      setError("");
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    // 최종 제출 시에도 비밀번호 일치 여부 확인
    if (password !== confirmPassword) {
      setError("비밀번호가 일치하지 않습니다.");
      return;
    }

    try {
      await axios.post("/auth/reset-password", {
        token,
        newPassword: password,
      });
      alert("비밀번호가 성공적으로 변경되었습니다.");
    } catch (error) {
      alert("비밀번호 변경에 실패했습니다.");
    }
  };

  return (
    <div
      className="min-h-screen w-full bg-black flex items-center justify-center bg-cover bg-center"
      style={{ backgroundImage: `url(${UsAndThem})` }}
    >
      <form onSubmit={handleSubmit} className="w-full max-w-xl p-8">
        <h2 className="text-3xl font-bold mb-6 text-white text-center">
          비밀번호 재설정
        </h2>

        <input
          type="password"
          name="password"
          placeholder="새로운 비밀번호"
          value={password}
          onChange={handleChange}
          className="w-full py-2 px-4 mb-4 border border-gray-500 rounded bg-black text-white placeholder-gray-400 focus:outline-none"
        />

        <input
          type="password"
          name="confirmPassword"
          placeholder="비밀번호 확인"
          value={confirmPassword}
          onChange={handleChange}
          className="w-full py-2 px-4 mb-4 border border-gray-500 rounded bg-black text-white placeholder-gray-400 focus:outline-none"
        />

        {/* 비밀번호 일치 여부에 따른 오류 메시지 표시, 고정 높이 유지 */}
        <div className="h-4 mb-4">
          {error && <p className="text-red-500 text-xs">{error}</p>}
        </div>

        <button
          type="submit"
          className="w-full py-2 mb-4 text-white bg-[#4D7FFF] rounded hover:bg-blue-600 focus:outline-none"
          disabled={!!error} // 오류가 있을 경우 버튼 비활성화
        >
          비밀번호 재설정하기
        </button>
      </form>
    </div>
  );
};

export default PasswordChangePage;
