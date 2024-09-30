// src/components/Member.tsx
import React from "react";
import { FaGithub, FaEnvelope } from "react-icons/fa";

interface MemberProps {
  name: string;
  role: string;
  description: string;
  githubUrl: string;
  emailUrl: string;
  imgSrc: string;
}

const Member: React.FC<MemberProps> = ({
  name,
  role,
  description,
  githubUrl,
  emailUrl,
  imgSrc,
}) => {
  return (
    <div className="flex items-center space-x-6 p-6 rounded-lg shadow-lg">
      {/* 프로필 이미지 */}
      <img
        src={imgSrc}
        alt="Profile"
        className="w-48 h-48 object-cover rounded-full"
      />
      {/* 이름, 역할, 설명 */}
      <div>
        <h3 className="text-3xl font-bold text-left mb-1">{name}</h3>
        <p className="text-4D7FFF text-xl text-left text-[#4D7FFF]">{role}</p>
        <p className="text-sm mt-2 text-left">{description}</p>
        {/* 깃허브 및 이메일 아이콘과 텍스트 */}
        <div className="flex space-x-4 mt-4 items-center">
          <a href={githubUrl} target="_blank" rel="noopener noreferrer">
            <FaGithub className="text-xl text-white" />
          </a>
          {/* 이메일 아이콘과 이메일 텍스트 */}
          <div className="flex items-center space-x-2">
            <FaEnvelope className="text-xl text-white" />
            <span className="text-sm text-white">{emailUrl}</span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Member;
