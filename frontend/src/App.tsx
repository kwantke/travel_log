import { RouterProvider } from "react-router";
import { router } from "./router";
import UserProvider from "@providers/UserProvider";
export default function App() {
  return (
    <>
      <UserProvider>
        <RouterProvider router={router} />
      </UserProvider>
    </>
  );
}
