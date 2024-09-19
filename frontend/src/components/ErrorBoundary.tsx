// ErrorBoundary.tsx
import React, { Component, ErrorInfo, ReactNode } from "react";
import { NavigateFunction, useNavigate } from "react-router-dom";

interface ErrorBoundaryProps {
  children: ReactNode;
  navigate: NavigateFunction;
}

interface ErrorBoundaryState {
  hasError: boolean;
  errorCode: number; // 추가된 상태
}

class ErrorBoundary extends Component<ErrorBoundaryProps, ErrorBoundaryState> {
  constructor(props: ErrorBoundaryProps) {
    super(props);
    this.state = { hasError: false, errorCode: 404 }; // 초기값 설정
  }

  static getDerivedStateFromError() {
    // 에러가 발생하면 state를 업데이트합니다.
    return { hasError: true };
  }

  componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error("ErrorBoundary caught an error", error, errorInfo);
    // 필요한 경우 추가 처리 로직을 여기에 작성합니다.
  }

  handleGoHome = () => {
    this.props.navigate("/home");
  };

  render() {
    if (this.state.hasError) {
      // 에러가 발생하면 에러 페이지로 리다이렉트
      return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-white text-center w-screen">
          <p className="text-[250px] font-bold italic text-[#1D2F5E] -mt-20">
            Oops!
          </p>
          <p className="text-2xl font-semibold mt-12">
            {this.state.errorCode} ERROR
          </p>
          <p className="mt-4">찾을 수 없는 페이지입니다.</p>
          <p>요청하신 페이지가 사라졌거나, 잘못된 경로를 이용하셨어요!</p>
          <button
            onClick={this.handleGoHome}
            className="mt-6 bg-[#4D7FFF] text-white px-8 py-1.5 rounded-lg hover:bg-blue-700"
          >
            HOME
          </button>
        </div>
      );
    }

    // 에러가 발생하지 않으면 children을 렌더링합니다.
    return this.props.children;
  }
}

// Hook을 이용해 navigate를 연결하는 Wrapper 컴포넌트
const ErrorBoundaryWrapper: React.FC<{ children: ReactNode }> = ({
  children,
}) => {
  const navigate = useNavigate();
  return <ErrorBoundary navigate={navigate}>{children}</ErrorBoundary>;
};

export default ErrorBoundaryWrapper;
