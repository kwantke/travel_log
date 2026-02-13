import axios from "axios";
import { handleAPIError, handlePreviousRequest } from "./interceptor";

export const client = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_URL,
});

export const authClient = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_URL,
});

authClient.interceptors.request.use(handlePreviousRequest);
authClient.interceptors.response.use((response) => response, handleAPIError);

client.interceptors.response.use((response) => response, handleAPIError);
