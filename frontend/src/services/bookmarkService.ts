import api from "./api";
import type { BookmarkResponse } from "../types/bookmark";

export const toggleBookmark = async (
  userId: number,
  articleId: number,
): Promise<BookmarkResponse> => {
  const response = await api.post<BookmarkResponse>("/bookmarks/toggle", null, {
    params: { userId, articleId },
  });
  return response.data;
};

export const getBookmarkedArticleIds = async (
  userId: number = 1,
): Promise<number[]> => {
  const response = await api.get<number[]>("/bookmarks", {
    params: { userId },
  });
  return response.data;
};
