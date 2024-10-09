import { useMutation, useQueryClient } from "@tanstack/react-query";
import { updateUserInfo } from "../../apis/axios";
import { useUserQuery } from "../../hooks/useUserQuery";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import Modal from "../../components/common/Modal";
import { FaExclamationCircle } from "react-icons/fa"; // Modal에서 사용할 아이콘 가져오기

const UserInfoEditPage: React.FC = () => {
  const { data: userData } = useUserQuery();
  const userSeq = userData?.userSeq;
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const [nickname, setNickname] = useState(userData?.nickname || "");
  const [email, setEmail] = useState(userData?.email || "");
  const [password, setPassword] = useState("");

  const [isModalOpen, setIsModalOpen] = useState(false); // 모달 열림 상태

  const openModal = () => {
    setIsModalOpen(true); // 모달 열기
  };

  const closeModal = () => {
    setIsModalOpen(false); // 모달 닫기
  };

  const mutation = useMutation({
    mutationFn: (updatedData: {
      email: string;
      password: string;
      nickname: string;
    }) => updateUserInfo(userSeq!, updatedData),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["user"] });
      setTimeout(() => {
        navigate("/mypage/myinformation");
      }, 500);
    },
    onError: (error) => {
      console.error("Error updating user info:", error);
    },
  });

  const handleSubmit = () => {
    if (!userSeq) {
      console.error("User sequence is not available.");
      return;
    }

    // 비밀번호가 비어있으면 모달을 열기
    if (!password) {
      openModal();
      return; // 비밀번호가 없으면 제출을 중단
    }

    const updatedData = {
      email,
      password,
      nickname,
    };

    mutation.mutate(updatedData); // mutate로 업데이트 데이터 전송
  };

  return (
    <div className="bg-black p-8 rounded-lg w-[1000px] h-[500px] flex flex-col justify-between">
      <h2 className="text-2xl font-semibold italic mb-4">My Information</h2>
      <div className="flex items-start space-x-8">
        <img
          src="/assets/George.jpg"
          alt="User"
          className="rounded-[90px] w-[220px] h-[170px] object-cover mr-10"
        />
        <div className="grid gap-y-6 w-full text-[18px]">
          <div className="flex items-center">
            <p className="w-24 font-semibold">닉네임</p>
            <input
              type="text"
              className="border-b placeholder-white flex-grow pl-2 bg-black"
              value={nickname}
              onChange={(e) => setNickname(e.target.value)}
            />
          </div>
          <div className="flex items-center">
            <p className="w-24 font-semibold">아이디</p>
            <p className="border-b border-gray-200 text-gray-400 flex-grow pl-2">
              {userData?.userId}
            </p>
          </div>
          <div className="flex items-center">
            <p className="w-24 font-semibold">비밀번호</p>
            <input
              type="password"
              className="border-b border-gray-400 flex-grow pl-2 bg-black"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
          <div className="flex items-center">
            <p className="w-24 font-semibold">이메일</p>
            <input
              type="text"
              className="border-b border-gray-400 placeholder-white flex-grow pl-2 bg-black"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
          </div>
          <div className="flex items-center">
            <p className="w-24 font-semibold">생년월일</p>
            <p className="border-b border-gray-400 text-gray-400 flex-grow pl-2">
              {userData?.birthDate}
            </p>
          </div>
          <div className="flex items-center">
            <p className="w-24 font-semibold">성별</p>
            <p className="border-b border-gray-400 text-gray-400 flex-grow pl-2">
              {userData?.gender === "F" ? "여성" : "남성"}
            </p>
          </div>
        </div>
      </div>

      {/* Edit Button */}
      <div className="flex justify-end mt-6">
        <button
          className="bg-gray-700 px-6 py-2 rounded-md text-white hover:bg-gray-600"
          onClick={handleSubmit}
        >
          수정완료
        </button>
      </div>

      {/* Modal 사용 */}
      {isModalOpen && (
        <Modal
          onClose={closeModal}
          title="입력 오류"
          description="비밀번호를 입력해주세요."
          icon={FaExclamationCircle} // 아이콘으로 경고를 나타냄
          buttonText="확인"
        />
      )}
    </div>
  );
};

export default UserInfoEditPage;
