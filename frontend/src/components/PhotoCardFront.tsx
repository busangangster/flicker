import React from "react";
import { PhotoCardFrontProps } from "../type";

const PhotoCardFront: React.FC<PhotoCardFrontProps> = ({
  images,
  pageIndex,
  onCardClick, // 클릭 핸들러 추가
}) => {
  return (
    <div className="bg-[#FFFDF8] flex flex-col justify-between h-full rounded-sm">
      <div className="h-6" />

      <div className="grid grid-cols-2 grid-row-2 gap-x-14 gap-y-6 px-28 flex-grow">
        {images.map((image, index) => (
          <div
            key={index}
            className="bg-white w-[250px] h-[350px] rounded-lg border border-gray-600 pt-3 pb-5 flex flex-col justify-center items-center shadow-lg photo-card-hover cursor-pointer"
            onClick={() => onCardClick(image)} // 카드 클릭 시 상세 페이지 열기
          >
            <img
              src={image.src}
              alt={image.alt}
              className="border rounded-md object-cover w-5/6 mb-2" // 이미지 아래에 간격 추가
            />
            <div className="text-gray-600 text-sm self-end mr-6" lang="ko">
              {image.createdAt.slice(0, 10).replace(/-/g, ".")}
            </div>
          </div>
        ))}
      </div>

      <p className="text-lg text-gray-700 mt-1 text-center w-full">
        {pageIndex}
      </p>
      <div className="h-6" />
    </div>
  );
};

export default PhotoCardFront;
