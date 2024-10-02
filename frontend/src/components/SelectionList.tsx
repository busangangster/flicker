import React from "react";

const SelectionList: React.FC = () => {
  const movieImages = Array.from(
    { length: 30 },
    (_, index) => `/assets/survey/image${(index % 13) + 1}.jpg` // 예시 이미지 13개 반복
  );

  return (
    <div className="grid grid-cols-6 gap-x-5 gap-y-12 px-16 py-8 w-full">
      {movieImages.map((imgSrc, index) => (
        <div key={index} className="w-[270px] h-[350px]">
          <img
            src={imgSrc}
            alt={`Movie Poster ${index + 1}`}
            className="w-full h-full object-cover rounded-md photo-card-hover"
          />
        </div>
      ))}
    </div>
  );
};

export default SelectionList;
