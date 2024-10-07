import React, { useState, useEffect } from "react";
import { Bar } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { getReviewRating } from "../apis/movieApi"; // API 호출 함수 가져오기
import { RatingData, ReviewRatingCount } from "../type";
import { TooltipItem } from "chart.js";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

const Ratings: React.FC<{ movieSeq: number }> = ({ movieSeq }) => {
  const [yValues, setYValues] = useState<number[]>(Array(10).fill(0)); // 초기값은 0으로 채워진 배열
  const [averageRating, setAverageRating] = useState<number>(0); // 가중 평균 값
  const [totalCnt, setTotalCnt] = useState<number>(0); // 총 리뷰 수

  // x 라벨 (0.5부터 시작해서 5.0까지)
  const xLabels = Array.from({ length: 10 }, (_, i) =>
    (i * 0.5 + 0.5).toFixed(1)
  );

  // 데이터를 불러오는 함수
  const fetchRatingsData = async () => {
    try {
      const response: RatingData = await getReviewRating(movieSeq);

      // 별점에 따라 yValues를 업데이트하는 로직
      const ratingCountMap = Array(10).fill(0); // 0.5부터 5.0까지 0.5 단위로 값 설정

      response.data.reviewRatingCount.forEach((rating: ReviewRatingCount) => {
        const index = (rating.reviewRating - 0.5) / 0.5; // reviewRating에 따른 index 계산
        if (index >= 0 && index < ratingCountMap.length) {
          ratingCountMap[index] = rating.count;
        }
      });

      setYValues(ratingCountMap); // yValues 업데이트

      // 가중 평균 계산
      const sumOfRatings = response.data.reviewRatingCount.reduce(
        (sum, rating) => sum + rating.reviewRating * rating.count,
        0
      );
      setAverageRating(sumOfRatings / response.data.totalCnt || 0); // 평균 값 설정

      // 총 리뷰 수 업데이트
      setTotalCnt(response.data.totalCnt);
    } catch (error) {
      console.error("별점 데이터를 불러오는 중 오류 발생:", error);
    }
  };

  useEffect(() => {
    fetchRatingsData(); // 컴포넌트가 로드될 때 별점 데이터를 불러옴
  }, [movieSeq]);

  // 최고 값을 가진 막대의 색상을 다르게 설정
  const maxValue = Math.max(...yValues);
  const maxIndex = yValues.indexOf(maxValue);

  const data = {
    labels: xLabels,
    datasets: [
      {
        label: "Ratings",
        data: yValues,
        backgroundColor: yValues.map(( index) =>
          index === maxIndex ? "#6282D3" : "#8694B8"
        ),
        borderColor: yValues.map(( index) =>
          index === maxIndex ? "#6282D3" : "#8694B8"
        ),
        borderWidth: 0.5,
        borderRadius: 5,
        barPercentage: 1,
        categoryPercentage: 0.9,
      },
    ],
  };

  const options = {
    maintainAspectRatio: false,
    scales: {
      x: {
        ticks: {
          display: true,
          maxRotation: 0,
          minRotation: 0,
          padding: 0,
          font: {
            size: 14, // X라벨 크기 조정
          },
          color: "#FFFFFF", // X라벨 글씨 색상 더 하얗게
          callback: function (_: string | number, index: number) {
            const numericLabel = parseFloat(xLabels[index]);
            return numericLabel % 1 === 0 ? numericLabel.toString() : "";
          },
        },
        grid: {
          display: false,
        },
      },
      y: {
        beginAtZero: true,
        display: false,
      },
    },
    plugins: {
      legend: {
        display: false,
      },
      tooltip: {
        enabled: true, // 툴팁 활성화
        callbacks: {
          label: function (tooltipItem: TooltipItem<"bar">) {
            return ` ${tooltipItem.raw} Flickers`; // y값을 툴팁으로 표시
          },
        },
      },
    },
    layout: {
      padding: {
        right: 10,
        top: 30
      },
    },
    clip: false as const,
  };

  // const formattedTotalRatings =
  //   totalCnt >= 1000 ? `${(totalCnt / 1000).toFixed(0)}K` : totalCnt;

  return (
    <div
      className="mt-4 bg-black rounded text-white relative"
      // style={{ height: "300px" }}
    >
      <div className="flex justify-between items-center mb-1">
        <p className="font-semibold text-lg mr-2">RATINGS</p>
        <span className="text-sm">{totalCnt} Flickers</span>
      </div>
      <div className="border-t border-gray-700 mb-4"></div>
      <div className="relative ml-3">
        <Bar data={data} options={options} />
        <div className="absolute right-7 top-0 flex items-center text-2xl font-bold transform translate-x-1/2">
          {averageRating.toFixed(2)}
        </div>
      </div>
    </div>
  );
};

export default Ratings;
