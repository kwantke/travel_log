export interface Tag {
  id: number;
  tag: string;
}

export interface TravelogueResponse {
  id: number;
  title: string;
  thumbnail: string;
  authorNickname: string;
  authorProfileUrl: string;
  authorId: number;
  createdAt: string;
  likeCount: number;
  isLiked: boolean;
  tags: Tag[];
}

export type SortingOption = "likeCount" | "createdAt";

export type TravelPeriodOption =
  | ""
  | "1"
  | "2"
  | "3"
  | "4"
  | "5"
  | "6"
  | "7"
  | "8";
