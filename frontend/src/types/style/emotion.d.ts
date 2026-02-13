import "@emotion/react";

declare module "@emotion/react" {
  export interface Theme {
    typography: typeof import("@styles/tokens").TYPOGRAPHY;
    colors: typeof import("@styles/tokens").SEMANTIC_COLORS;
    spacing: typeof import("@styles/tokens").SPACING;
    zIndex: typeof import("@styles/tokens").Z_INDEX;
    animation: typeof import("@styles/tokens").ANIMATION;
  }
}
