// src/components/SearchList.tsx
import React from "react";

interface SearchListProps {
  searchQuery: string;
}

const SearchList: React.FC<SearchListProps> = () => {
  // 검색 결과로 임시 포스터 이미지 리스트 생성
  const movieImages = Array.from(
    { length: 30 },
    (_, index) => `/assets/survey/image${index + 1}.jpg`
  );

  return (
    <div className="grid grid-cols-6 gap-x-5 gap-y-12 px-16 py-8 w-full">
      {movieImages.map((imgSrc, index) => (
        <div key={index} className="w-[270px] h-[350px]">
          <img
            src={imgSrc}
            alt={`Movie Poster ${index + 1}`}
            className="w-full h-full object-cover rounded-md"
          />
        </div>
      ))}
    </div>
  );
};

export default SearchList;
