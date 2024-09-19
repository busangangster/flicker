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

const PasswordChangePage = () => {
  return (
    <div>PasswordChangePage</div>
  )
}

export default PasswordChangePage
