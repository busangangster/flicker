// PhotoCardPage.tsx
import React, { useRef, useEffect } from "react";
import HTMLFlipBook from "react-pageflip";
import "../../css/photobook.css";

const PhotoCardPage: React.FC = () => {
  const book = useRef(null);

  useEffect(() => {
    // 컴포넌트가 마운트되면 첫 페이지를 자동으로 넘겨 두 번째 페이지부터 시작
    if (book.current) {
      setTimeout(() => {
        (book.current as any).pageFlip().flipNext();
      }, 10); // 페이지 넘김 딜레이 (0.1초 후 넘김)
    }
  }, []);

  const images = [
    { src: "/assets/survey/image3.jpg", alt: "Movie 1" },
    { src: "/assets/survey/image5.jpg", alt: "Movie 2" },
    { src: "/assets/survey/image7.jpg", alt: "Movie 3" },
    { src: "/assets/survey/image10.jpg", alt: "Movie 4" },
    { src: "/assets/survey/image8.jpg", alt: "Movie 5" },
    { src: "/assets/survey/image14.jpg", alt: "Movie 6" },
    { src: "/assets/survey/image17.jpg", alt: "Movie 7" },
    { src: "/assets/survey/image20.jpg", alt: "Movie 8" },
  ];

  const pages = [
    {
      id: 1,
      content: (
        <div className="flex flex-col items-center justify-center w-full h-full bg-white">
          <h1 className="text-5xl font-bold italic text-black mb-3">
            MOVIE MEMORIES
          </h1>
          <hr className="border-t-2 border-neutral-500 my-1 w-2/3" />
          <hr className="border-t-2 border-neutral-500 my-1 w-2/3" />
          <p className="text-lg mt-2 text-gray-700 self-end mr-40">
            made by nickname
          </p>
          <img
            src="https://via.placeholder.com/600x400"
            alt="Cover Image"
            className="mt-6"
          />
          <p className="text-lg text-gray-700 mt-4 text-center w-full">
            Page 1
          </p>
        </div>
      ),
    },
    {
      id: 2,
      content: (
        <div className="grid grid-cols-2 grid-row-2 gap-4 p-4 bg-white h-full w-full">
          {images.slice(0, 4).map((image, index) => (
            <img
              key={index}
              src={image.src}
              alt={image.alt}
              className="border rounded-md object-cover w-full h-[340px]"
            />
          ))}
          <p className="text-lg text-gray-700 mt-4 text-center w-full">
            Page 2
          </p>
        </div>
      ),
    },
    {
      id: 3,
      content: (
        <div className="grid grid-cols-2 grid-row-2 gap-4 p-4 bg-white h-full w-full">
          {images.slice(4, 8).map((image, index) => (
            <img
              key={index}
              src={image.src}
              alt={image.alt}
              className="border rounded-md object-cover w-full h-[340px]"
            />
          ))}
          <p className="text-lg text-gray-700 mt-4 text-center w-full">
            Page 3
          </p>
        </div>
      ),
    },
    {
      id: 4,
      content: (
        <div className="grid grid-cols-2 grid-row-2 gap-4 p-4 bg-white h-full">
          {images.slice(0, 4).map((image, index) => (
            <img
              key={index}
              src={image.src}
              alt={image.alt}
              className="border rounded-md object-cover w-full h-[340px]"
            />
          ))}
          <p className="text-lg text-gray-700 mt-4 text-center w-full">
            Page 4
          </p>
        </div>
      ),
    },
    {
      id: 5,
      content: (
        <div className="grid grid-cols-2 grid-row-2 gap-4 p-4 bg-white h-full">
          {images.slice(2, 6).map((image, index) => (
            <img
              key={index}
              src={image.src}
              alt={image.alt}
              className="border rounded-md object-cover w-full h-[340px]"
            />
          ))}
          <p className="text-lg text-gray-700 mt-4 text-center w-full">
            Page 5
          </p>
        </div>
      ),
    },
    {
      id: 6,
      content: (
        <div className="grid grid-cols-2 grid-row-2 gap-4 p-4 bg-white h-full">
          {images.slice(1, 5).map((image, index) => (
            <img
              key={index}
              src={image.src}
              alt={image.alt}
              className="border rounded-md object-cover w-full h-[340px]"
            />
          ))}
          <p className="text-lg text-gray-700 mt-4 text-center w-full">
            Page 6
          </p>
        </div>
      ),
    },
    {
      id: 7,
      content: (
        <div className="grid grid-cols-2 grid-row-2 gap-4 p-4 bg-white h-full">
          {images.slice(4, 8).map((image, index) => (
            <img
              key={index}
              src={image.src}
              alt={image.alt}
              className="border rounded-md object-cover w-full h-[340px]"
            />
          ))}
          <p className="text-lg text-gray-700 mt-4 text-center w-full">
            Page 7
          </p>
        </div>
      ),
    },
  ];

  return (
    <div className="bg-black rounded-lg w-[1200px] relative">
      <div className="flex w-full h-full justify-center items-center">
        <HTMLFlipBook
          width={1000}
          height={1350}
          size="stretch"
          minWidth={300}
          maxWidth={1000}
          minHeight={400}
          maxHeight={1350}
          showCover={true}
          mobileScrollSupport={true}
          className="shadow-2xl book-animation-in"
          ref={book}
          drawShadow={true}
          flippingTime={1000}
          useMouseEvents={true}
          startPage={0}
        >
          {pages.map((page) => (
            <div key={page.id} className="flip-page">
              {page.content}
            </div>
          ))}
        </HTMLFlipBook>
      </div>
    </div>
  );
};

export default PhotoCardPage;
