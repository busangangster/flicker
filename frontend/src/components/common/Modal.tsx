import React, { useEffect, useState } from "react";
import "../../css/Modal.css";
import { ModalProps } from "../../type";

const Modal: React.FC<ModalProps> = ({
  onClose,
  title,
  description,
  icon: Icon,
  buttonText,
  iconColor = "#848484", 
}) => {
  const [isVisible, setIsVisible] = useState(false);

  useEffect(() => {
    setTimeout(() => {
      setIsVisible(true);
    }, 50); // 모달이 뜰 때 약간의 딜레이를 줌
  }, []);

  const handleClose = () => {
    setIsVisible(false);
    setTimeout(() => {
      onClose();
    }, 300); // 애니메이션 후 모달을 닫음
  };

  return (
    <div
      className={`fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50 transition-opacity duration-300 ${
        isVisible ? "opacity-100" : "opacity-0"
      }`}
    >
      <div
        className={`bg-[#FFFDF6] p-12 rounded-xl text-center shadow-lg min-w-80 transform transition-transform duration-300 ${
          isVisible ? "scale-100" : "scale-75"
        }`}
      >
        <div className="mb-6 flex items-center justify-center space-x-2">
          {Icon && (
            <Icon
              className="mr-2"
              style={{ color: iconColor, fontSize: "35px" }}
            />
          )}
          <span className="text-3xl font-bold text-[#4D4D4D]">{title}</span>
        </div>
        {description && (
          <p className="mt-4 text-lg text-[#4D4D4D] mb-2">{description}</p>
        )}
        <button
          className="bg-neutral-400 hover:bg-neutral-500 text-white font-medium py-2 px-4 rounded-3xl transition-colors duration-300 mt-4"
          lang="ko"
          onClick={handleClose}
        >
          {buttonText}
        </button>
      </div>
    </div>
  );
};

export default Modal;
