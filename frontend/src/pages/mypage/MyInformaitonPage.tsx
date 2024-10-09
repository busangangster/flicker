import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useUserQuery } from "../../hooks/useUserQuery";

const MyInformationPage: React.FC = () => {
  const navigate = useNavigate();

  const { data, error, isLoading, refetch } = useUserQuery();

  // 페이지에 도착할 때마다 데이터를 강제로 다시 가져오도록 설정
  useEffect(() => {
    refetch(); // 데이터를 다시 불러오는 작업
  }, []);

  const goToVerification = () => {
    navigate("/mypage/verification");
  };

  if (isLoading) return <p>로딩 중...</p>;
  if (error) return <p>유저 정보를 불러오는데 실패했습니다.</p>;

  return (
    <div className="bg-black p-8 rounded-lg w-[1000px] h-[500px] flex flex-col justify-between">
      <h2 className="text-2xl font-semibold italic mb-4">My Information</h2>

      {/* User Profile Picture and Information */}
      <div className="flex items-start space-x-8">
        <img
          src="/assets/George.jpg"
          alt="User"
          className="rounded-[90px] w-[220px] h-[170px] object-cover mr-10"
        />
        <div className="grid gap-y-6 w-full text-[18px]">
          <div className="flex items-center">
            <p className="w-24 font-semibold">닉네임</p>
            <p className="border-b border-gray-400 flex-grow pl-2">
              {data?.nickname}
            </p>
          </div>
          <div className="flex items-center">
            <p className="w-24 font-semibold">아이디</p>
            <p className="border-b border-gray-400 flex-grow pl-2">
              {data?.userId}
            </p>
          </div>
          <div className="flex items-center">
            <p className="w-24 font-semibold">이메일</p>
            <p className="border-b border-gray-400 flex-grow pl-2">
              {data?.email}
            </p>
          </div>
          <div className="flex items-center">
            <p className="w-24 font-semibold">생년월일</p>
            <p className="border-b border-gray-400 flex-grow pl-2">
              {data?.birthDate}
            </p>
          </div>
          <div className="flex items-center">
            <p className="w-24 font-semibold">성별</p>
            <p className="border-b border-gray-400 flex-grow pl-2">
              {data?.gender === "M"
                ? "남성"
                : data?.gender === "F"
                ? "여성"
                : "정보 없음"}
            </p>
          </div>
        </div>
      </div>

      {/* Edit Button */}
      <div className="flex justify-end mt-6">
        <button
          className="bg-gray-700 px-6 py-2 rounded-md text-white hover:bg-gray-600"
          onClick={goToVerification}
        >
          수정하기
        </button>
      </div>
    </div>
  );
};

export default MyInformationPage;
