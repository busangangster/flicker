import React, { useState } from "react";

interface FilterProps {
  options: { value: string; label: string }[]; // 옵션을 외부에서 받도록 설정
  onChange?: (value: string) => void; // 선택 변경 시 호출할 함수
  defaultValue?: string; // 기본값을 받을 수 있도록 설정
  customClass?: string; // 커스텀 클래스를 받을 수 있도록 설정
}

const Filter: React.FC<FilterProps> = ({
  options,
  onChange,
  defaultValue,
  customClass,
}) => {
  const [isOpen, setIsOpen] = useState(false); // 드롭다운 열림/닫힘 상태 관리
  const [selectedOption, setSelectedOption] = useState(
    defaultValue || options[0].value
  ); // 선택된 옵션 관리

  const handleToggle = () => {
    setIsOpen(!isOpen); // 드롭다운 열고 닫는 동작
  };

  const handleSelect = (value: string) => {
    setSelectedOption(value); // 선택된 옵션 설정
    setIsOpen(false); // 선택 후 드롭다운 닫기
    onChange && onChange(value); // 부모 컴포넌트에 선택된 값 전달
  };

  return (
    <div className="relative inline-block text-left w-full">
      {/* 드롭다운 버튼 */}
      <button
        type="button"
        className="inline-flex justify-between w-full rounded-md shadow-sm px-4 py-2 bg-gray-700 text-sm font-medium text-white hover:bg-gray-600 focus:outline-none"
        onClick={handleToggle}
      >
        {selectedOption} {/* 선택된 옵션 표시 */}
        <svg
          className={`ml-2 h-5 w-5 transform ${
            isOpen ? "rotate-180" : "rotate-0"
          }`}
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 20 20"
          fill="currentColor"
          aria-hidden="true"
        >
          <path
            fillRule="evenodd"
            d="M5.23 7.21a.75.75 0 011.06-.02L10 10.44l3.71-3.25a.75.75 0 111.04 1.08l-4 3.5a.75.75 0 01-1.04 0l-4-3.5a.75.75 0 01-.02-1.06z"
            clipRule="evenodd"
          />
        </svg>
      </button>

      {/* 드롭다운 메뉴 (isOpen이 true일 때만 보임) */}
      {isOpen && (
        <div
          className={`absolute z-50 mt-2 rounded-md shadow-lg bg-gray-700 ${
            customClass || "w-36"
          }`}
        >
          <div className={`py-1 ${customClass || ""}`}>
            {options.map((option) => (
              <div
                key={option.value}
                className="cursor-pointer px-4 py-2 text-sm text-white hover:bg-gray-600"
                onClick={() => handleSelect(option.value)}
              >
                {option.label}
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default Filter;
