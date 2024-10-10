import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { signUp } from "../../apis/authApi";
import calendar_white from "../../assets/icons/calendar_white.png";
import { SignUpParams } from "../../type";
import { IoIosArrowRoundBack } from "react-icons/io";
import Modal from "../../components/common/Modal"; // Modal 컴포넌트 불러오기
import { FaCheckCircle, FaExclamationCircle } from "react-icons/fa"; // 모달에 사용할 아이콘 불러오기

// background 이미지 목록
const backgrounds = [
  "/assets/background/background1.png",
  "/assets/background/background2.png",
  "/assets/background/background3.png",
  "/assets/background/background4.png",
  "/assets/background/background5.png",
  "/assets/background/background6.png",
];

const SignUpPage: React.FC = () => {
  const [formData, setFormData] = useState<SignUpParams>({
    userId: "",
    email: "",
    password: "",
    passCheck: "",
    nickname: "",
    birthDate: "",
    gender: "",
  });

  const [errors, setErrors] = useState({
    userId: "",
    password: "",
    passCheck: "",
    email: "",
    nickname: "",
    gender: "",
  });

  const navigate = useNavigate();
  const [isIdChecked, setIsIdChecked] = useState(false);
  const [backgroundImage, setBackgroundImage] = useState(""); // 랜덤 배경 이미지 상태

  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 상태 관리
  const [modalContent, setModalContent] = useState({
    title: "",
    description: "",
    icon: FaCheckCircle,
    buttonText: "확인",
  }); // 모달의 내용 관리

  useEffect(() => {
    // 랜덤한 배경 이미지 선택
    const randomIndex = Math.floor(Math.random() * backgrounds.length);
    setBackgroundImage(backgrounds[randomIndex]);
  }, []);

  const validateField = (name: string, value: string) => {
    let error = "";

    switch (name) {
      case "email":
        if (value && !value.includes("@")) {
          error = "올바른 이메일 형식이 아니에요.";
        }
        break;
      case "passCheck":
        if (value !== formData.password) {
          error = "비밀번호가 일치하지 않아요.";
        }
        break;
      case "userId":
        if (!/^[a-zA-Z0-9]*$/.test(value)) {
          error = "영어와 숫자만 입력가능해요.";
        }
        break;
      default:
        break;
    }

    setErrors((prevErrors) => ({
      ...prevErrors,
      [name]: error,
    }));
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
    validateField(name, value);
  };

  const handleBlur = (e: React.FocusEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    validateField(name, value);
  };

  const handleGenderChange = (gender: "M" | "F") => {
    setFormData({ ...formData, gender });
    setErrors({ ...errors, gender: "" });
  };

  const handleIdCheck = () => {
    setIsIdChecked(true);
    setErrors({ ...errors, userId: "" });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    const isValid =
      Object.values(errors).every((error) => error === "") && validateForm();
    if (!isValid) return;

    try {
      await signUp(formData);

      // 회원가입 성공 시 모달 띄우기
      setModalContent({
        title: "회원가입 완료",
        description:
          "회원가입이 완료되었어요. \n앞으로 Flicker에서 소중한 기록을 남겨보세요!",
        icon: FaCheckCircle,
        buttonText: "확인",
      });
      setIsModalOpen(true);

      // 모달 닫은 후 로그인 페이지로 이동
      setTimeout(() => {
        navigate("/signin");
      },3000);
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    } catch (error) {
      // 회원가입 실패 시 모달 띄우기
      setModalContent({
        title: "회원가입 실패",
        description: "회원가입에 실패했습니다. 다시 시도해주세요.",
        icon: FaExclamationCircle,
        buttonText: "확인",
      });
      setIsModalOpen(true);
    }
  };

  const validateForm = () => {
    let valid = true;
    const newErrors = { ...errors };

    if (!formData.email.includes("@")) {
      newErrors.email = "올바른 이메일 형식이 아니에요.";
      valid = false;
    }

    if (formData.password !== formData.passCheck) {
      newErrors.passCheck = "비밀번호가 일치하지 않아요.";
      valid = false;
    }

    if (!/^[a-zA-Z0-9]*$/.test(formData.userId)) {
      newErrors.userId = "영어와 숫자만 입력가능해요.";
      valid = false;
    }

    setErrors(newErrors);
    return valid;
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
      <form onSubmit={handleSubmit} className="w-full max-w-2xl p-8">
        <h2 className="text-3xl font-bold mb-8 text-white text-center">
          회원가입
        </h2>

        <div className="mb-4">
          <div className="flex w-full">
            <input
              type="text"
              name="userId"
              placeholder="아이디"
              value={formData.userId}
              onChange={handleChange}
              onBlur={handleBlur}
              className="flex-grow py-2 px-4 border border-gray-400 rounded bg-black text-white placeholder-gray-400 focus:outline-none"
            />
            <button
              type="button"
              onClick={handleIdCheck}
              className={`ml-1 px-4 py-2 border rounded font-bold text-sm ${
                isIdChecked
                  ? "bg-[#4D7FFF] text-white"
                  : "bg-black border-[#4D7FFF] text-[#4D7FFF]"
              }`}
            >
              {isIdChecked ? "확인완료" : "중복확인"}
            </button>
          </div>
          {errors.userId && (
            <p className="text-red-500 text-xs mt-1">{errors.userId}</p>
          )}
        </div>

        <div className="mb-4">
          <input
            type="password"
            name="password"
            placeholder="비밀번호"
            value={formData.password}
            onChange={handleChange}
            className="w-full py-2 px-4 border border-gray-400 rounded bg-black text-white placeholder-gray-400 focus:outline-none"
          />
          {errors.password && (
            <p className="text-red-500 text-xs mt-1">{errors.password}</p>
          )}
        </div>

        <div className="mb-4">
          <input
            type="password"
            name="passCheck"
            placeholder="비밀번호 확인"
            value={formData.passCheck}
            onChange={handleChange}
            onBlur={handleBlur}
            className="w-full py-2 px-4 border border-gray-400 rounded bg-black text-white placeholder-gray-400 focus:outline-none"
          />
          {errors.passCheck && (
            <p className="text-red-500 text-xs mt-1">{errors.passCheck}</p>
          )}
        </div>

        <div className="mb-4">
          <input
            type="email"
            name="email"
            placeholder="이메일"
            value={formData.email}
            onChange={handleChange}
            onBlur={handleBlur}
            className="w-full py-2 px-4 border border-gray-400 rounded bg-black text-white placeholder-gray-400 focus:outline-none"
          />
          {errors.email && (
            <p className="text-red-500 text-xs mt-1">{errors.email}</p>
          )}
        </div>

        <div className="mb-4">
          <div className="flex w-full space-x-2">
            <div className="relative flex-grow">
              <input
                type="text"
                name="birthDate"
                placeholder="생년월일"
                value={formData.birthDate}
                onChange={handleChange}
                onFocus={(e) => (e.target.type = "date")}
                onBlur={(e) => (e.target.type = "text")}
                className="w-full py-2 px-4 border border-gray-400 rounded bg-black text-white placeholder-gray-400 focus:outline-none"
              />
              <span className="absolute inset-y-0 right-3 flex items-center pointer-events-none text-white">
                <img
                  src={calendar_white}
                  alt="캘린더 아이콘"
                  className="w-5 h-5"
                />
              </span>
            </div>

            <button
              type="button"
              className={`px-4 py-2 rounded ${
                formData.gender === "M" ? "bg-[#4D7FFF]" : "bg-gray-700"
              } text-white`}
              onClick={() => handleGenderChange("M")}
            >
              남성
            </button>
            <button
              type="button"
              className={`px-4 py-2 rounded ${
                formData.gender === "F" ? "bg-[#4D7FFF]" : "bg-gray-700"
              } text-white`}
              onClick={() => handleGenderChange("F")}
            >
              여성
            </button>
          </div>
          {errors.gender && (
            <p className="text-red-500 text-xs mt-1">{errors.gender}</p>
          )}
        </div>

        <div className="mb-4">
          <input
            type="text"
            name="nickname"
            placeholder="닉네임"
            value={formData.nickname}
            onChange={handleChange}
            onBlur={handleBlur}
            className="w-full py-2 px-4 border border-gray-400 rounded bg-black text-white placeholder-gray-400 focus:outline-none"
          />
          {errors.nickname && (
            <p className="text-red-500 text-xs mt-1">{errors.nickname}</p>
          )}
        </div>

        <button
          type="submit"
          className="w-full py-2 mt-4 bg-[#4D7FFF] text-white rounded hover:bg-blue-600 focus:outline-none"
        >
          회원가입
        </button>
      </form>

      {/* Modal */}
      {isModalOpen && (
        <Modal
          onClose={() => setIsModalOpen(false)}
          title={modalContent.title}
          description={modalContent.description}
          icon={modalContent.icon}
          buttonText={modalContent.buttonText}
          iconColor="#20BD4D"
        />
      )}
    </div>
  );
};

export default SignUpPage;
