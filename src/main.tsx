import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { Global } from "@emotion/react";
import { globalStyle } from "@styles/globalStyle";
import App from './App.tsx'

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <Global styles={globalStyle}/>
    <App />
  </StrictMode>,
)
