import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { AiOutlineClose } from "react-icons/ai";
import { IoIosSearch } from "react-icons/io";
import { SearchBarProps } from "../type";

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
        <div className="cursor-pointer" onClick={handleClick}>
          <IoIosSearch size={30} className="text-white" />{" "}
        </div>

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
