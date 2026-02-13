import { createContext } from "react";
import type { AuthTokenResponse } from "@type/domain/user";

export interface UserContextProps {
  user: AuthTokenResponse | null;
}

export interface SaveUserContextProps {
  saveUser: (userInfo: AuthTokenResponse | null) => void;
}

export const UserContext = createContext<UserContextProps>({
  user: null,
});

export const SaveUserContext = createContext<SaveUserContextProps>({
  saveUser: () => {},
});