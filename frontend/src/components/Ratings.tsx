// Ratings.tsx
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

const data = {
  labels: ["☆", "", "", "", "", "", "", "", "", "", "★★★★★"],
  datasets: [
    {
      label: "Ratings",
      data: [0, 20, 50, 150, 200, 300, 100, 500, 400, 800, 600, 1000],
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
        display: true,
        font: {
          size: 12,
          weight: "bold",
        },
        color: "#608CFF",
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
  clip: false,
};

const Ratings: React.FC = () => {
  const averageRating = 4.1;
  const totalRatings = 146000;

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
        <p className="font-semibold mr-2">RATINGS</p>
        <span className="text-xs">{formattedTotalRatings} Flickers</span>
      </div>
      <div className="border-t border-gray-700"></div>
      <div className="relative">
        <Bar data={data} options={options} />
        <div className="absolute right-5 top-1/3 flex items-center text-2xl font-bold transform translate-x-1/2">
          {averageRating}
        </div>
        <div className="absolute bottom-0 right-7 flex text-sm text-[#608CFF] transform translate-x-1/2">
          ★★★★★
        </div>
      </div>
    </div>
  );
};

export default Ratings;
