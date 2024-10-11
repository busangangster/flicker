// src/components/ServiceContent.tsx
import React from "react";
import { ServiceContentProps } from "../type";

const ServiceContent: React.FC<ServiceContentProps> = ({
  title,
  description,
  tags,
  imageUrl,
}) => {
  return (
    <div className="flex justify-between px-48 py-20 mt-72 mb-16">
      <div>
        <h2 className="text-3xl ml-2">{title}</h2>
        <p className="text-5xl mt-5 font-semibold leading-snug">
          {description.split("\\n").map((line, index) => (
            <React.Fragment key={index}>
              {line}
              <br />
            </React.Fragment>
          ))}
        </p>
        <div className="flex space-x-6 mt-7 ml-1">
          {tags.map((tag, index) => (
            <span key={index} className="text-xl text-gray-400">
              {tag}
            </span>
          ))}
        </div>
      </div>
      <img
        src={imageUrl}
        alt="Service Placeholder"
        className="w-[550px] h-[700px] object-cover"
      />
    </div>
  );
};

export default ServiceContent;
