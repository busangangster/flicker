import React from "react";
import { BrowserRouter } from "react-router-dom";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import AppRouter from "./router/AppRouter.tsx";
// import ErrorBoundaryWrapper from "./components/ErrorBoundary.tsx";

const queryClient = new QueryClient();

const App: React.FC = () => {
  return (
    // <ErrorBoundaryWrapper>
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <AppRouter />
      </BrowserRouter>
    </QueryClientProvider>
    // </ErrorBoundaryWrapper>
  );
};

export default App;
