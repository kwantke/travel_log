import { createBrowserRouter } from "react-router";
import { ROUTE_PATHS_MAP } from "@constants/route";
import LandingPage from "./pages/landing/LandingPage";
import AppLayout from "./layout/AppLayout/AppLayout";
import MainPage from "./pages/main/MainPage";



export const router = createBrowserRouter([
  {
    path: ROUTE_PATHS_MAP.root,
    Component: LandingPage,
  },
  {
    Component: AppLayout,
    children:[
      {
        path: ROUTE_PATHS_MAP.main,
        Component: MainPage
      },
      {
        path: ROUTE_PATHS_MAP.travelogue(),
        element: 
      }
    ]
  }
]);
