import api from "./api";
import type { Article } from "../types/article";

export const getArticles = async (userId: number = 1): Promise<Article[]> => {
  const response = await api.get<Article[]>("/articles", {
    params: { userId },
  });
  return response.data;
};

export const getArticleById = async (id: number): Promise<Article> => {
  const response = await api.get<Article>(`/articles/${id}`);
  return response.data;
};
