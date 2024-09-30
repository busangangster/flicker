    // theme.ts
import { createTheme } from "@mui/material/styles";

// 라이트 모드 테마
export const lightTheme = createTheme({
  palette: {
    mode: "light",
    primary: {
      main: "#1976d2", // 기본 색상
    },
    background: {
      default: "#ffffff", // 배경 색상
      paper: "#f5f5f5", // 종이 색상
    },
    text: {
      primary: "#000000", // 기본 텍스트 색상
    },
  },
  typography: {
    fontFamily: "Arial, sans-serif",
  },
});

// 다크 모드 테마
export const darkTheme = createTheme({
  palette: {
    mode: "dark",
    primary: {
      main: "#90caf9", // 기본 색상
    },
    background: {
      default: "#121212", // 배경 색상
      paper: "#1e1e1e", // 종이 색상
    },
    text: {
      primary: "#ffffff", // 기본 텍스트 색상
    },
  },
  typography: {
    fontFamily: "Arial, sans-serif",
  },
});
