import React, { useMemo, useEffect, useState } from "react";
import WordCloud from "react-d3-cloud";
import { getWordCloud } from "../apis/movieApi";
import { WordCloud as WordCloudDataType } from "../type";
import { useParams } from "react-router-dom";

const Keyword: React.FC = () => {
  const { movieSeq } = useParams<{ movieSeq: string }>(); // URL에서 movieSeq 받아오기
  const [words, setWords] = useState<{ text: string; value: number }[]>([]); // 워드 클라우드 데이터를 저장할 상태
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchWordCloudData = async () => {
      setIsLoading(true);
      try {
        const wordCloudData: WordCloudDataType = await getWordCloud(
          Number(movieSeq)
        );
        // API에서 받은 데이터를 변환하여 사용
      const formattedWords = wordCloudData.data
        .filter((item) => item.keyword !== "영화")
        .map((item) => ({
          text: item.keyword,
          value: item.count,
        }));

        
        setWords(formattedWords);
      } catch (err) {
        console.error("워드 클라우드 데이터를 불러오는 중 오류 발생:", err);
        setError("워드 클라우드 데이터를 불러오는 중 오류가 발생했습니다.");
      } finally {
        setIsLoading(false);
      }
    };

    fetchWordCloudData(); // API 호출
  }, [movieSeq]);

  // 글자 크기 설정 (최소 및 최대 크기 제한 추가)
  const fontSize = (word: { value: number }) => {
    const minSize = 50;
    const maxSize = 130;
    return Math.min(Math.max(word.value * 0.1, minSize), maxSize);
  };

  // 글자 회전 설정 (0도 고정)
  const rotate = () => 0;

  // 상위 50개의 단어만 표시하도록 제한
  const limitedWords = words.slice(0, 50);

  // useMemo를 사용하여 워드 클라우드 데이터를 메모이제이션
  const wordCloudComponent = useMemo(() => {
    return (
      <WordCloud
        data={limitedWords}
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
  }, [limitedWords]); // words 배열이 변경될 때마다 업데이트

  return (
    <div className="mt-4">
      <h2 className="text-lg font-semibold mb-2">Keyword</h2>
      <div className="bg-gray-800 p-2 rounded">
        {isLoading ? (
          <p>로딩 중...</p>
        ) : error ? (
          <p>{error}</p>
        ) : (
          wordCloudComponent
        )}
      </div>
    </div>
  );
};

export default Keyword;
