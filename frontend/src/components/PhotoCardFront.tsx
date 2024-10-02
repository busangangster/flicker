import React from "react";
import { useNavigate } from "react-router-dom";
import { PhotoCardFrontProps } from "../type";

const PhotoCardFront: React.FC<PhotoCardFrontProps> = ({
  images,
  pageIndex,
}) => {
  const navigate = useNavigate();

  const goToPhotoCardDetail = (
    src: string,
    movieSeq: number,
    movieTitle: string,
    movieYear: number,
    reviewRating: number,
    createdAt: string,
    content: string,
    likes: number,
    backgroundUrl: string,
  ) => {
    navigate(`/photocarddetail/${movieSeq}`, {
      state: {
        src, 
        movieTitle,
        movieYear,
        reviewRating,
        createdAt,
        content,
        likes,
        backgroundUrl,
      },
    });
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
            alt={image.alt}
            className="border rounded-md object-cover w-full h-[320px]"
            onClick={() =>
              goToPhotoCardDetail(
                image.src,
                image.movieSeq,
                image.movieTitle,
                image.movieYear,
                image.reviewRating,
                image.createdAt,
                image.content,
                image.likes,
                image.backgroundUrl,
              )
            }
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
