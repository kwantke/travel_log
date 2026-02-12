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
