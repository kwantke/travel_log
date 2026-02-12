import { STORAGE_KEYS_MAP } from "@constants/storage";
import { SaveUserContext, UserContext } from "@contexts/UserContext";
import type { AuthTokenResponse } from "@type/domain/user";
import { useState } from "react";


const UserProvider = ({ children }: React.PropsWithChildren) => {
  const [user, setUser] = useState<AuthTokenResponse | null>(() => {
    const raw = localStorage.getItem(STORAGE_KEYS_MAP.user);
    if (!raw) return null;

    try {
      return JSON.parse(raw) as AuthTokenResponse;
    } catch {
      return null;
    }
  });

  const saveUser = (user: AuthTokenResponse | null) => {
    localStorage.setItem(STORAGE_KEYS_MAP.user, JSON.stringify(user ?? {}));
    setUser(user);
  };
  return (
    <UserContext.Provider value={{ user }}>
      <SaveUserContext.Provider value={{ saveUser }}>
        {children}
      </SaveUserContext.Provider>
    </UserContext.Provider>
  );
};

export default UserProvider;
