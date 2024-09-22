import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { AiOutlineClose } from "react-icons/ai";

interface SearchBarProps {
  initialSearchQuery?: string;
  isExpanded: boolean;
  setIsExpanded: React.Dispatch<React.SetStateAction<boolean>>;
}

const SearchBar: React.FC<SearchBarProps> = ({
  initialSearchQuery = "",
  isExpanded,
  setIsExpanded,
}) => {
  const [searchQuery, setSearchQuery] = useState(initialSearchQuery);
  const searchRef = useRef<HTMLDivElement | null>(null);
  const navigate = useNavigate();

  const handleClick = () => {
    setIsExpanded((prev) => !prev);
  };

  const handleSearchChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setSearchQuery(event.target.value);
  };

  const handleSearchSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (searchQuery.trim() !== "") {
      navigate(`/search?query=${searchQuery}`, {
        state: { searchQuery, isExpanded },
      });
    }
  };

  const clearSearch = () => {
    setSearchQuery("");
  };

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (
        searchRef.current &&
        !searchRef.current.contains(event.target as Node)
      ) {
        setIsExpanded(false);
      }
    };

    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, [setIsExpanded]);

  return (
    <div
      ref={searchRef}
      className={`flex items-center transition-all duration-400 ${
        isExpanded ? "w-[370px] border border-white p-2 rounded" : "w-[50px]"
      } relative`}
    >
      <div className="flex items-center w-full">
        <svg
          onClick={handleClick}
          width="30"
          height="30"
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          className={`cursor-pointer transition-transform duration-400 ${
            isExpanded ? "translate-x-[-50px]" : ""
          }`}
        >
          <circle cx="12" cy="11" r="7" stroke="white" strokeWidth="2" />
          <line
            x1="16"
            y1="16"
            x2="22"
            y2="22"
            stroke="white"
            strokeWidth="2"
          />
        </svg>

        <form onSubmit={handleSearchSubmit} className="relative w-full">
          <input
            type="text"
            placeholder="제목, 사람, 장르"
            value={searchQuery}
            onChange={handleSearchChange}
            className={`bg-transparent border-none outline-none text-white text-lg w-full pr-8 transition-all duration-400 ${
              isExpanded ? "opacity-100" : "w-0 opacity-0"
            }`}
          />
          {isExpanded && searchQuery && (
            <AiOutlineClose
              onClick={clearSearch}
              className="cursor-pointer text-white absolute right-2 top-1/2 transform -translate-y-1/2"
              size={24} // 엑스 아이콘 크기
            />
          )}
        </form>
      </div>
    </div>
  );
};

export default SearchBar;
