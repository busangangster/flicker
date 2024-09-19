import React from "react";
import { useNavigate } from "react-router-dom";

const VerificationPage: React.FC = () => {
  const navigate = useNavigate();

  const goToUseInfoEdit = () => {
    navigate("/mypage/userinfoedit");
  };

  return (
    <div className="bg-black p-8 rounded-lg w-[750px] h-[500px] flex flex-col ">
      <h2 className="text-2xl font-semibold italic mb-[34px]">
        My Information
      </h2>
      <h4 className="text-lg mb-[54px]">
        회원 정보 수정을 위해서는 본인 확인이 필요해요.
      </h4>

      {/* User Input for ID and Password */}
      <div className="flex items-start space-x-8 mb-[37px]">
        <div className="grid gap-y-6 w-full text-[18px]">
          <div className="flex items-center">
            <label className="w-28 font-semibold" htmlFor="userId">
              아이디
            </label>
            <input
              id="userId"
              type="text"
              className="border rounded-[5px] border-white pl-2 w-[575px] h-[40px] bg-black text-white text-[14px]"
              placeholder="아이디를 입력하세요"
            />
          </div>
          <div className="flex items-center">
            <label className="w-28 font-semibold" htmlFor="password">
              비밀번호
            </label>
            <input
              id="password"
              type="password"
              className="border rounded-[5px] border-white pl-2 w-[575px] h-[40px] bg-black text-white text-[14px]"
              placeholder="비밀번호를 입력하세요"
            />
          </div>
        </div>
      </div>

      {/* Confirm Button */}
      <div className="flex justify-end mt-6">
        <button
          onClick={goToUseInfoEdit}
          className="bg-gray-700 px-6 py-2 rounded-md text-white hover:bg-gray-600"
        >
          확인
        </button>
      </div>
    </div>
  );
};

export default VerificationPage;
