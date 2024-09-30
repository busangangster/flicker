import React from "react";
import { useParams } from "react-router-dom";
import SelectionList from "../../components/SelectionList";
import Navbar from "../../components/common/Navbar";

export const MovieGenrePage: React.FC = () => {
    const { genre } = useParams<{ genre: string }>();
    
  return (
    <div className="flex flex-col bg-black h-screen overflow-y-auto">
      <header className="sticky top-0 bg-transparent z-10">
        <Navbar />
      </header>
      <div className="mt-32 mb-4">
        <span className="text-gray-300 text-2xl ml-16">영화 </span>
        <span className="text-white text-4xl ml-4">&gt; {genre}</span>
      </div>
      <SelectionList />
    </div>
  );
};

export default MovieGenrePage;
