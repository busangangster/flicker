// ThemeContext.tsx
import React, { createContext, useContext, useState } from "react";

// 다크 모드 Context 생성
const ThemeContext = createContext({
  isDarkMode: false,
  toggleTheme: () => {},
  setDarkMode: () => {},
  setLightMode: () => {},
});

export const ThemeProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [isDarkMode, setIsDarkMode] = useState(false);

  const toggleTheme = () => {
    setIsDarkMode((prevMode) => {
      console.log("Toggle Theme: ", !prevMode); // 토글 상태 출력
      return !prevMode;
    });
  };
  const setDarkMode = () => {
    setIsDarkMode(true);
    console.log("Set to Dark Mode"); // 다크 모드로 변경할 때 출력
  };

  const setLightMode = () => {
    setIsDarkMode(false);
    console.log("Set to Light Mode"); // 라이트 모드로 변경할 때 출력
  };

  return (
    <ThemeContext.Provider
      value={{ isDarkMode, toggleTheme, setDarkMode, setLightMode }}
    >
      {children}
    </ThemeContext.Provider>
  );
};

export const useTheme = () => useContext(ThemeContext);
