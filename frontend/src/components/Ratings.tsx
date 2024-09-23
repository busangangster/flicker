import React from "react";
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

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend
);

// x라벨 (0부터 0.5 단위로 5까지)
const xLabels = Array.from({ length: 11 }, (_, i) => (i * 0.5).toFixed(1));

// y라벨 (해당 별점을 준 사람의 수)
const yValues = [0, 20, 50, 150, 200, 300, 100, 500, 400, 800, 600];

// weighted average 별점 계산
const weightedAverage = (xValues: number[], yValues: number[]): number => {
  const sumOfProducts = xValues.reduce(
    (sum, value, index) => sum + value * yValues[index],
    0
  );
  const totalRatings = yValues.reduce((sum, value) => sum + value, 0);
  return sumOfProducts / totalRatings;
};

const averageRating = weightedAverage(xLabels.map(Number), yValues).toFixed(2); // 소수점 2자리로 반올림

const data = {
  labels: ["", "", "", "", "", "", "", "", "", "", ""],
  datasets: [
    {
      label: "Ratings",
      data: yValues,
      backgroundColor: "rgba(128, 128, 128, 0.8)",
      borderColor: "rgba(128, 128, 128, 1)",
      borderWidth: 1,
      barPercentage: 0.8,
      categoryPercentage: 0.9,
    },
  ],
};

const options = {
  maintainAspectRatio: false,
  scales: {
    x: {
      ticks: {
        maxRotation: 0,
        minRotation: 0,
        padding: 0,
        display: false,
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
      enabled: false,
    },
  },
  layout: {
    padding: {
      right: 60,
    },
  },
  clip: false as const,
};

const Ratings: React.FC = () => {
  const totalRatings = yValues.reduce((sum, value) => sum + value, 0);

  // totalRatings 숫자 포맷 변환
  const formattedTotalRatings =
    totalRatings >= 1000
      ? `${(totalRatings / 1000).toFixed(0)}K`
      : totalRatings;

  return (
    <div
      className="mt-4 bg-black rounded text-white relative"
      style={{ height: "200px" }}
    >
      <div className="flex justify-between items-center mb-1">
        <p className="font-semibold text-lg mr-2">RATINGS</p>
        <span className="text-sm">{formattedTotalRatings} Flickers</span>
      </div>
      <div className="border-t border-gray-700 mb-4"></div>
      <div className="relative">
        <Bar data={data} options={options} />
        <div className="absolute right-5 top-1/3 flex items-center text-2xl font-bold transform translate-x-1/2">
          {averageRating}
        </div>
        <div className="absolute bottom-0 -left-1 flex text-sm text-[#608CFF] transform translate-x-1/2">
          ☆
        </div>
        <div className="absolute bottom-0 right-7 flex text-sm text-[#608CFF] transform translate-x-1/2">
          ★★★★★
        </div>
      </div>
    </div>
  );
};

export default Ratings;
