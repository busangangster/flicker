import React, { useEffect, useState } from "react";
import "../css/Modal.css";

interface FirstLoginModalProps {
  onClose: () => void;
}

const FirstLoginModal: React.FC<FirstLoginModalProps> = ({ onClose }) => {
  const [isVisible, setIsVisible] = useState(false);

  useEffect(() => {
    setTimeout(() => {
      setIsVisible(true);
    }, 50); 
  }, []);

  const handleClose = () => {
    setIsVisible(false);
    setTimeout(() => {
      onClose();
    }, 400); 
  };

  return (
    <div
      className={`fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50 transition-opacity duration-300 ${
        isVisible ? "opacity-100" : "opacity-0"
      }`}
    >
      <div
        className={`bg-[#FFFDF6] p-12 rounded-xl text-center shadow-lg transform transition-transform duration-300 ${
          isVisible ? "scale-100" : "scale-75"
        }`}
      >
        <div className="mb-6">
          <h2 className="text-3xl font-bold">첫 로그인을 축하합니다!</h2>
          <p className="mt-4 text-lg">
            맞춤 추천을 통해 설문조사로 이동합니다.
          </p>
        </div>
        <button
          className="bg-neutral-400 hover:bg-neutral-500 text-white font-medium py-2 px-4 rounded-3xl transition-colors duration-300 mt-4"
          lang="ko"
          onClick={handleClose}
        >
          확인
        </button>
      </div>
    </div>
  );
};

export default FirstLoginModal;
