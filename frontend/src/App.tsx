import React from "react";
import { BrowserRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import AppRouter from "./router/AppRouter.tsx";
// import ScrollToTop from "./components/ScrollToTop.tsx";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import "./App.css";

// import ErrorBoundaryWrapper from "./components/ErrorBoundary.tsx";

const queryClient = new QueryClient();

const App: React.FC = () => {
  return (
    // <ErrorBoundaryWrapper>
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <AppRouter />
        <ReactQueryDevtools initialIsOpen={false} /> {/* DevTools 추가 */}
        {/* <ScrollToTop> */}

      </BrowserRouter>
    </QueryClientProvider>
    // </ErrorBoundaryWrapper>
  );
};

export default App;
