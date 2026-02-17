export const ROUTE_PATHS_MAP = {
  root: "/",
  main: "/main",
  travelogue: (id?: number | string) =>
    id ? `/travelogue/${id}` : "trabelogue/:id",
  login: "/login",
} as const;
