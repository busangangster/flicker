import React from "react";
import { BrowserRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import AppRouter from "./router/AppRouter.tsx";
import ScrollToTop from "./components/ScrollToTop.tsx";
// import ErrorBoundaryWrapper from "./components/ErrorBoundary.tsx";

const queryClient = new QueryClient();

const App: React.FC = () => {
  return (
    // <ErrorBoundaryWrapper>
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <ScrollToTop>
          <AppRouter />
        </ScrollToTop>
      </BrowserRouter>
    </QueryClientProvider>
    // </ErrorBoundaryWrapper>
  );
};

export default App;
