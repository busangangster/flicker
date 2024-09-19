// App.tsx
import React from "react";
import AppRouter from "./router/AppRouter.tsx";
import ErrorBoundaryWrapper from "./components/ErrorBoundary.tsx";

const App = () => {
  return (
    <ErrorBoundaryWrapper>
      <AppRouter />
    </ErrorBoundaryWrapper>
  );
};

export default App;
