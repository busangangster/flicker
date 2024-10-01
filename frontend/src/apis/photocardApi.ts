import axios from "./axios";
import { PhotocardResponse } from "../type";


export const getPhotocard = async (
  userSeq: number
): Promise<PhotocardResponse> => {
  try {
    const response = await axios.get<PhotocardResponse>(
      `/api/bff/user/${userSeq}/photocard`
    );
    return response.data;
  } catch (error) {
    console.error("포토카드 데이터를 불러오는 데 실패했습니다.", error);
    throw error;
  }
};
