import api from "./api";

import type { ReadingHistoryItem } from "../types/readingHistory";

export const getReadingHistoryDetails = async (
  userId: number = 1,
): Promise<ReadingHistoryItem[]> => {
  const response = await api.get<ReadingHistoryItem[]>(
    "/reading-history/details",
    {
      params: { userId },
    },
  );
  return response.data;
};

export const markArticleAsRead = async (
  userId: number,
  articleId: number,
): Promise<void> => {
  await api.post("/reading-history", null, {
    params: { userId, articleId },
  });
};
