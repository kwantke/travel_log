import { CYPRESS_DATA_MAP, CYPRESS_SELECTOR_MAP } from "@constants/cypress";
import { DEFAULT_ELEMENT } from "./Chip.constants";
import * as S from "./Chip.styled";
import Text from "../Text/Text";
interface ChipOwnProps<
  Element extends React.ElementType = typeof DEFAULT_ELEMENT,
> {
  as?: Element;
  label: string;
  isSelected?: boolean;
  index?: number;
  iconPosition?: "none" | "left" | "right";
  iconType?: "sort-icon" | "down-arrow" | "reset-icon";
}

type ChipProps<E extends React.ElementType> = ChipOwnProps<E> &
  Omit<React.ComponentPropsWithoutRef<E>, keyof ChipOwnProps>;

export default function Chip<E extends React.ElementType>({
  as,
  label,
  isSelected = false,
  index,
  iconPosition = "none",
  iconType = "down-arrow",
  ...props
}: ChipProps<E>) {
  const Component = as ?? DEFAULT_ELEMENT;

  return (
    <>
      <S.Layout
        as={Component}
        $isSelected={isSelected}
        $index={index}
        data-cy={
          isSelected
            ? `selected-${CYPRESS_DATA_MAP.chip}`
            : CYPRESS_SELECTOR_MAP.chip
        }
        {...props}
      >
        <Text textType={isSelected ? "detailBold" : "detail"} />
      </S.Layout>
      ;
    </>
  );
}
