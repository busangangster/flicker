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
import { getReviewRating } from "../apis/movieApi"; 
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
  const [yValues, setYValues] = useState<number[]>(Array(10).fill(0)); 
  const [averageRating, setAverageRating] = useState<number>(0); 
  const [totalCnt, setTotalCnt] = useState<number>(0); 

  const xLabels = Array.from({ length: 10 }, (_, i) =>
    (i * 0.5 + 0.5).toFixed(1)
  );

  // 데이터를 불러오는 함수
  const fetchRatingsData = async () => {
    try {
      const response: RatingData = await getReviewRating(movieSeq);

      const ratingCountMap = Array(10).fill(0); 

      response.data.reviewRatingCount.forEach((rating: ReviewRatingCount) => {
        const index = (rating.reviewRating - 0.5) / 0.5; 
        if (index >= 0 && index < ratingCountMap.length) {
          ratingCountMap[index] = rating.count;
        }
      });

      setYValues(ratingCountMap); 

      const sumOfRatings = response.data.reviewRatingCount.reduce(
        (sum, rating) => sum + rating.reviewRating * rating.count,
        0
      );
      setAverageRating(sumOfRatings / response.data.totalCnt || 0); 

      setTotalCnt(response.data.totalCnt);
    } catch (error) {
      console.error("별점 데이터를 불러오는 중 오류 발생:", error);
    }
  };

  useEffect(() => {
    fetchRatingsData(); 
  }, [movieSeq]);

  const data = {
    labels: xLabels,
    datasets: [
      {
        label: "Ratings",
        data: yValues,
        backgroundColor: "#8694B8",
        borderColor: "#8694B8",
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
            size: 14, 
          },
          color: "#FFFFFF", 
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
        enabled: true, 
        callbacks: {
          label: function (tooltipItem: TooltipItem<"bar">) {
            return ` ${tooltipItem.raw} Flickers`; 
          },
        },
      },
    },
    layout: {
      padding: {
        right: 10,
        top: 30,
      },
    },
    clip: false as const,
  };


  return (
    <div
      className="mt-4 bg-black rounded text-white relative"
    >
      <div className="flex justify-between items-center mb-1">
        <p className="font-semibold text-lg mr-2">RATINGS</p>
        <span className="text-sm">
          <span lang="ko">{totalCnt}</span> Flickers
        </span>
      </div>
      <div className="border-t border-gray-700 mb-4"></div>
      <div className="relative ml-3">
        <Bar data={data} options={options} />
        <div
          className="absolute right-8 top-0 flex items-center text-2xl font-bold transform translate-x-1/2"
          lang="ko"
        >
          <span className="text-lg mr-1">☆</span>
          {averageRating.toFixed(2)}
        </div>
      </div>
    </div>
  );
};

export default Ratings;
