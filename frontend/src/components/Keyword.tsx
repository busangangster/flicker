// Keyword.tsx
import React from "react";
import WordCloud from "react-wordcloud";

const words = [
  { text: "감동", value: 50 },
  { text: "재미", value: 30 },
  { text: "최고", value: 40 },
  { text: "강추", value: 25 },
  { text: "비장", value: 20 },
  { text: "시원함", value: 35 },
  { text: "인생영화", value: 45 },
];

const options = {
  rotations: 1,
  rotationAngles: [0, 0],
  fontSizes: [10, 60],
};

const callbacks = {
  getWordColor: (word: { text: string; value: number }) => {
    // 원하는 색상 로직을 여기에 작성합니다. 예시로 파스텔 색상 팔레트 적용
    const colors = ["#FF6384", "#36A2EB", "#FFCE56", "#4BC0C0", "#9966FF"];
    return colors[Math.floor(Math.random() * colors.length)];
  },
};

const Keyword: React.FC = () => {
  return (
    <div className="mt-4">
      <h2 className="text-lg font-semibold mb-2">Keyword</h2>
      <div className="bg-gray-800 p-1 rounded">
        <WordCloud words={words} options={options} callbacks={callbacks} />
      </div>
    </div>
  );
};

export default Keyword;
