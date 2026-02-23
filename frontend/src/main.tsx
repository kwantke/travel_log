import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { Global, ThemeProvider } from "@emotion/react";
import { globalStyle } from "@styles/globalStyle";
import App from "./App.tsx";
import theme from "@styles/theme";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";

const queryClient = new QueryClient({
  defaultOptions:{
    queries:{
      refetchOnWindowFocus: false,
      retry: 0,
    }
  }
})

createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <QueryClientProvider client={queryClient}>
      <ThemeProvider theme={theme}>
        <Global styles={globalStyle} />
        <App />
      </ThemeProvider>
    </QueryClientProvider>
  </StrictMode>,
);
