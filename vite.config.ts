import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import path from "path";
// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react({
      babel: {
        plugins: ["@emotion"],
      },
    }),
  ],
  resolve: {
    alias: {
      // 2. @styles 별칭 설정 (src/styles 폴더를 가리킴)
      "@styles": path.resolve(__dirname, "./src/styles"),
    },
  },
});
