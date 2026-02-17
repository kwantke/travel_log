import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { Global, ThemeProvider } from "@emotion/react";
import { globalStyle } from "@styles/globalStyle";
import App from "./App.tsx";
import theme from "@styles/theme";
createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <ThemeProvider theme={theme}>
      <Global styles={globalStyle} />
      <App />
    </ThemeProvider>
  </StrictMode>,
);
