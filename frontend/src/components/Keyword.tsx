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

        const formattedWords = wordCloudData.data
          .filter(
            (item) =>
              item.keyword !== "영화" &&
              item.keyword !== "시리즈" &&
              item.keyword !== "배우" &&
              item.keyword !== "감독"
          )
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

  const fontSize = (word: { value: number }) => {
    const minSize = 50;
    const maxSize = 130;
    return Math.min(Math.max(word.value * 0.3, minSize), maxSize);
  };

  const rotate = () => 0;

  const limitedWords = words.slice(0, 50);

  const wordCloudComponent = useMemo(() => {
    return (
      <WordCloud
        data={limitedWords}
        font={() => "NanumGothic"}
        fontSize={fontSize}
        rotate={rotate}
        padding={5}
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
        width={500}
        height={500}
      />
    );
  }, [limitedWords]);

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

export default React.memo(Keyword);
