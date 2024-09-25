import React, { useMemo } from "react";
import WordCloud from "react-d3-cloud";

// 단어 리스트
const words = [
  { text: "감동", value: 50 },
  { text: "재미", value: 30 },
  { text: "최고", value: 40 },
  { text: "강추", value: 25 },
  { text: "비장", value: 20 },
  { text: "시원함", value: 35 },
  { text: "인생영화", value: 45 },
];

// 글자 크기 설정 (value 값에 비례해 더 큰 차이를 보이도록 조정)
const fontSize = (word: { value: number }) => word.value * 2; // 기존보다 큰 차이 설정

// 글자 회전 설정 (0도 고정)
const rotate = () => 0;

const Keyword: React.FC = () => {
  // useMemo를 사용하여 워드 클라우드 데이터를 메모이제이션
  const wordCloudComponent = useMemo(() => {
    return (
      <WordCloud
        data={words}
        font={() => "NanumGothic"}
        fontSize={fontSize} // 글자 크기 설정
        rotate={rotate} // 글자 회전 설정
        padding={5} // 단어 간격 설정
        fill={() => {
          const colors = [
            "#FF6384",
            "#36A2EB",
            "#FFCE56",
            "#4BC0C0",
            "#9966FF",
          ];
          return colors[Math.floor(Math.random() * colors.length)];
        }}
        width={500} // 워드 클라우드의 너비
        height={500} // 워드 클라우드의 높이
      />
    );
  }, []); // 빈 배열을 의존성으로 전달하여 처음 렌더링 시에만 실행

  return (
    <div className="mt-4">
      <h2 className="text-lg font-semibold mb-2">Keyword</h2>
      <div className="bg-gray-800 p-2 rounded">{wordCloudComponent}</div>
    </div>
  );
};

export default Keyword;
