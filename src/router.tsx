import { createBrowserRouter } from "react-router";
import { ROUTE_PATHS_MAP } from "@constants/route";
import LandingPage from "./pages/landing/LandingPage";

export const router = createBrowserRouter([
  {
    path: ROUTE_PATHS_MAP.root,
    Component: LandingPage,
  },
]);
