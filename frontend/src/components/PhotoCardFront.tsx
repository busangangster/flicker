// src/components/PhotoCardFront.tsx
import React from "react";
import { useNavigate } from "react-router-dom";

interface PhotoCardFrontProps {
  images: { src: string; alt: string }[];
  pageIndex: number;
}

const PhotoCardFront: React.FC<PhotoCardFrontProps> = ({
  images,
  pageIndex,
}) => {
  const navigate = useNavigate();

  const goTophotocarddetail = () => {
    navigate("/mypage/photocarddetail");
  };

  return (
    <div className="grid grid-cols-2 grid-row-2 gap-4 p-4 bg-white h-full w-full page-content">
      {images.map((image, index) => (
        <div
          key={index}
          className="bg-white rounded-lg border border-gray-700 pl-3 pr-3 pt-3 pb-10 flex justify-center items-center shadow-lg photo-card-hover"
        >
          <img
            src={image.src}
            className="border rounded-md object-cover w-full h-[320px]"
            onClick={goTophotocarddetail}
          />
        </div>
      ))}
      <p className="text-lg text-gray-700 mt-1 text-right w-full">
        {pageIndex}
      </p>
    </div>
  );
};

export default PhotoCardFront;
