// App.tsx
import React from "react";
import { BrowserRouter } from "react-router-dom";
import AppRouter from "./router/AppRouter.tsx";
// import ErrorBoundaryWrapper from "./components/ErrorBoundary.tsx";

const App: React.FC = () => {
  return (
    // <ErrorBoundaryWrapper>
    <BrowserRouter>
      <AppRouter />
    </BrowserRouter>
    // </ErrorBoundaryWrapper>
  );
};

export default App;
