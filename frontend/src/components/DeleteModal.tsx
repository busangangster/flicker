import React, { useEffect, useState } from "react";
import "../css/Modal.css"; // 기존 Modal 스타일을 재사용


interface DeleteModalProps {
  onClose: () => void;
  onDeleteConfirm: () => void;
  title: string;
  description: string;
}

const DeleteModal: React.FC<DeleteModalProps> = ({
    onClose,
    onDeleteConfirm,
    title,
    description,
}) => {
    const [isVisible, setIsVisible] = useState(false);
    
    useEffect(() => {
      setTimeout(() => {
        setIsVisible(true);
      }, 50); // 모달이 뜰 때 약간의 딜레이를 줌
    }, []);

    
  return (
    <div
      className={`fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 z-50 transition-opacity duration-300 ${
        isVisible ? "opacity-100" : "opacity-0"
      }`}
    >
      <div className="bg-[#FFFDF6] p-12 rounded-xl text-center shadow-lg max-w-96 transform transition-transform duration-300 scale-100">
        <div className="mb-6 flex flex-col items-center justify-center">
          <span className="text-[#4D4D4D] text-3xl font-bold">{title}</span>
          <p className="mt-4 text-xl text-[#4D4D4D]">{description}</p>
        </div>
        <div className="flex justify-center space-x-4 mt-6">
          <button
            className="bg-[#4D7FFF] hover:bg-[#3E63C3] text-white font-medium py-2 px-4 rounded-3xl transition-colors duration-300"
            onClick={onDeleteConfirm}
          >
            확인
          </button>
          <button
            className="bg-gray-400 hover:bg-gray-500 text-white font-medium py-2 px-4 rounded-3xl transition-colors duration-300"
            onClick={onClose}
          >
            취소
          </button>
        </div>
      </div>
    </div>
  );
};

export default DeleteModal;
