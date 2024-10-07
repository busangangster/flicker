import { useMutation, useQueryClient } from "@tanstack/react-query";
import Swal from "sweetalert2";
import { updateUserInfo } from "../../apis/axios";
import { useUserQuery } from "../../hooks/useUserQuery";
import { useNavigate } from "react-router-dom";
import { useState } from "react";

const UserInfoEditPage: React.FC = () => {
  const { data: userData } = useUserQuery();
  const userSeq = userData?.userSeq;
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const [nickname, setNickname] = useState(userData?.nickname || "");
  const [email, setEmail] = useState(userData?.email || "");
  const [password, setPassword] = useState("");

  const mutation = useMutation({
    mutationFn: (updatedData: {
      email: string;
      password: string;
      nickname: string;
    }) => updateUserInfo(userSeq!, updatedData), // userSeq가 확실히 존재할 때만 실행
    onSuccess: () => {
      // 성공 시 캐시 무효화 (유저 정보 쿼리 재실행)
      queryClient.invalidateQueries({ queryKey: ["user"] });
      // queryClient.refetchQueries({ queryKey: ["user"] }); // user 쿼리를 다시 실행

      // SweetAlert로 성공 메시지 띄우기
      Swal.fire({
        title: "성공!",
        text: "회원 정보가 성공적으로 업데이트되었습니다.",
        icon: "success",
        confirmButtonText: "확인",
      }).then(() => {
        setTimeout(() => {
          navigate("/mypage/myinformation");
        }, 500);
      });
    },
    onError: (error) => {
      console.error("Error updating user info:", error);
      Swal.fire({
        title: "오류!",
        text: "회원 정보 업데이트 중 문제가 발생했습니다.",
        icon: "error",
        confirmButtonText: "확인",
      });
    },
  });

  const handleSubmit = () => {
    if (!userSeq) {
      console.error("User sequence is not available.");
      return;
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
    </div>
  );
};

export default UserInfoEditPage;
