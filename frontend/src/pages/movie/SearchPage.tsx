// src/pages/movie/SearchPage.tsx
import React, { useState } from "react";
import { useLocation } from "react-router-dom";
import SelectionList from "../../components/SelectionList";
import Navbar from "../../components/common/Navbar";
import SearchBar from "../../components/SearchBar";

const SearchPage: React.FC = () => {
  const location = useLocation();
  const searchParams = new URLSearchParams(location.search);
  const searchQuery = searchParams.get("query") || "";
  const { isExpanded: initialIsExpanded } = location.state || {}; // isExpanded 상태를 가져옴

  const [isExpanded, setIsExpanded] = useState(initialIsExpanded || false);

  return (
    <div className="flex flex-col bg-black h-screen text-white overflow-y-auto">
      <header className="sticky top-0 bg-transparent z-10">
        <Navbar />
      </header>

      <div className="mt-[100px] flex justify-end items-end w-[1800px]">
        <SearchBar
          initialSearchQuery={searchQuery}
          isExpanded={isExpanded}
          setIsExpanded={setIsExpanded}
        />
      </div>

      <h1 className="text-2xl font-bold text-gray-300 mt-8 mb-4 ml-16">
        "{searchQuery}" 검색 결과
      </h1>
      <SelectionList searchQuery={searchQuery} />
    </div>
  );
};

export default SearchPage;
