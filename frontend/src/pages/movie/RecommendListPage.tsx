// RecommandListPage.tsx
import React, { useState } from "react";
import { useSwipeable } from "react-swipeable"; // react-swipeable import

// 목업 포스터 이미지 임의 설정
const posters = [
  {
    id: 1,
    src: "https://via.placeholder.com/200x300?text=Movie+1",
    alt: "Movie 1",
  },
  {
    id: 2,
    src: "https://via.placeholder.com/200x300?text=Movie+2",
    alt: "Movie 2",
  },
  {
    id: 3,
    src: "https://via.placeholder.com/200x300?text=Movie+3",
    alt: "Movie 3",
  },
  {
    id: 4,
    src: "https://via.placeholder.com/200x300?text=Movie+4",
    alt: "Movie 4",
  },
  {
    id: 5,
    src: "https://via.placeholder.com/200x300?text=Movie+5",
    alt: "Movie 5",
  },
  {
    id: 6,
    src: "https://via.placeholder.com/200x300?text=Movie+6",
    alt: "Movie 6",
  },
  {
    id: 7,
    src: "https://via.placeholder.com/200x300?text=Movie+7",
    alt: "Movie 7",
  },
  {
    id: 8,
    src: "https://via.placeholder.com/200x300?text=Movie+8",
    alt: "Movie 8",
  },
];

const RecommandListPage: React.FC = () => {
  const [scrollIndex, setScrollIndex] = useState(0);

  const scrollRight = () => {
    if (scrollIndex < posters.length - 4) {
      setScrollIndex((prev) => prev + 1);
    }
  };

  const scrollLeft = () => {
    if (scrollIndex > 0) {
      setScrollIndex((prev) => prev - 1);
    }
  };

  // 스와이프 핸들러 설정
  const handlers = useSwipeable({
    onSwipedLeft: () => scrollRight(),
    onSwipedRight: () => scrollLeft(),
    preventScrollOnSwipe: true,
    trackMouse: true, // 마우스 드래그 동작 감지 활성화
    delta: 5, // 스와이프를 인식하는 최소 거리
  });

  return (
    <div
      className="relative bg-black text-white min-h-screen flex items-center justify-center"
      style={{
        backgroundImage: `url(src/assets/movie/theater3.jpg)`,
        backgroundSize: "cover",
        backgroundPosition: "center",
      }}
    >
      {/* 영화관 스타일 화면 */}
      <div className="relative w-[72%] h-[60vh] rounded-md overflow-hidden">
        <h2 className="text-center text-2xl font-bold mt-6 text-black">
          My own movie theater
        </h2>

        {/* 포스터 슬라이더 */}
        <div className="flex justify-center mt-8">
          <div className="flex overflow-hidden relative">
            {/* 포스터 리스트 */}
            <div
              className="flex transition-transform ease-in-out duration-300"
              style={{ transform: `translateX(-${scrollIndex * 220}px)` }}
            >
              {posters.map((poster) => (
                <div
                  key={poster.id}
                  {...handlers} // 각 포스터 이미지에 핸들러 적용
                  className="flex-shrink-0 cursor-pointer"
                >
                  <img
                    src={poster.src}
                    alt={poster.alt}
                    className="w-[200px] h-[300px] object-cover mx-2"
                    draggable={false} // 이미지를 드래그할 수 없도록 설정하여 스와이프와 충돌 방지
                  />
                </div>
              ))}
            </div>

            {/* 왼쪽 화살표 */}
            {scrollIndex > 0 && (
              <button
                onClick={scrollLeft}
                className="absolute left-0 top-1/2 transform -translate-y-1/2 bg-black bg-opacity-0 text-white text-opacity-0 text-2xl p-2 rounded-full hover:bg-opacity-50 hover:text-white outline-none focus:outline-none focus-visible:outline-none"
              >
                &lt;
              </button>
            )}

            {/* 오른쪽 화살표 */}
            {scrollIndex < posters.length - 4 && (
              <button
                onClick={scrollRight}
                className="absolute right-0 top-1/2 transform -translate-y-1/2 bg-black bg-opacity-0 text-white text-opacity-0 text-2xl p-2 rounded-full hover:bg-opacity-50 hover:text-white outline-none focus:outline-none focus-visible:outline-none"
              >
                &gt;
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default RecommandListPage;
