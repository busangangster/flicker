// Filter.tsx
import React from "react";

interface FilterProps {
  options: { value: string; label: string }[]; // 옵션을 외부에서 받도록 설정
  onChange?: (value: string) => void; // 선택 변경 시 호출할 함수
}

const Filter: React.FC<FilterProps> = ({ options, onChange }) => {
  return (
    <select
      className="p-1 rounded bg-gray-700 text-white mb-4 text-sm"
      onChange={(e) => onChange && onChange(e.target.value)}
    >
      {options.map((option) => (
        <option key={option.value} value={option.value}>
          {option.label}
        </option>
      ))}
    </select>
  );
};

export default Filter;
