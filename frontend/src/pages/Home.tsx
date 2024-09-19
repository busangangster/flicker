import React from "react";
import Navbar from "../components/common/Navbar";
import CircleCarousel from "../components/CircleCarousel";

const Home = () => {
  return (
    <div className="min-h-screen flex flex-col">
      <header className="sticky top-0 bg-black z-10">
        <Navbar />
      </header>
      <main className="w-hull h-screen">
        <CircleCarousel />
      </main>
    </div>
  );
};

export default Home;
