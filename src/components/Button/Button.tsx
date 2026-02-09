import type { ComponentPropsWithoutRef } from "react";
import * as S from "./Button.styled";
import type { ButtonPosition, ButtonVariants } from "./Button.type";

export interface ButtonProps extends ComponentPropsWithoutRef<"button"> {
  variants?: ButtonVariants;
  position?: ButtonPosition;
}

export default function Button({
  variants = "primary",
  position = "center",
  children,
  ...props
}: ButtonProps) {
  return (
    <>
      <S.Button $variants={variants} $position={position} {...props}>
        {children}
      </S.Button>
    </>
  );
}
