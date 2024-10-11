import React, { useState } from "react";

interface FilterProps {
  options: { value: string; label: string }[]; 
  onChange?: (value: string) => void; 
  defaultValue?: string; 
  customClass?: string; 
}

const Filter: React.FC<FilterProps> = ({
  options,
  onChange,
  defaultValue,
  customClass,
}) => {
  const [isOpen, setIsOpen] = useState(false); 
  const [selectedOption, setSelectedOption] = useState(
    defaultValue || options[0].value
  );

  const handleToggle = () => {
    setIsOpen(!isOpen); 
  };

  const handleSelect = (value: string) => {
    setSelectedOption(value); 
    setIsOpen(false); 
    if (onChange) {
      onChange(value);
    }
  };

  return (
    <div className="relative inline-block text-left w-full">
      <button
        type="button"
        className="inline-flex justify-between w-full rounded-md shadow-sm px-4 py-2 bg-gray-700 text-sm font-medium text-white hover:bg-gray-600 focus:outline-none"
        onClick={handleToggle}
      >
        {selectedOption} 
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

      {isOpen && (
        <div
          className={`absolute z-50 mt-2 rounded-md shadow-lg bg-gray-700 ${
            customClass || "w-40"
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
