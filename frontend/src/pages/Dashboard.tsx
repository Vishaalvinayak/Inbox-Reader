import { useEffect, useState } from "react";
import { getArticles } from "../services/articleService";
import { getBookmarkedArticleIds } from "../services/bookmarkService";
import ArticleCard from "../components/ArticleCard";
import type { Article } from "../types/article";

export default function Dashboard() {
  const [articles, setArticles] = useState<Article[]>([]);
  const [bookmarkedIds, setBookmarkedIds] = useState<number[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    Promise.all([getArticles(1), getBookmarkedArticleIds(1)])
      .then(([articlesData, bookmarksData]) => {
        setArticles(articlesData);
        setBookmarkedIds(bookmarksData);
      })
      .catch((err: Error) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading)
    return <div className="text-neutral-400 p-8">Loading articles...</div>;
  if (error) return <div className="text-red-400 p-8">Error: {error}</div>;

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold text-neutral-100 mb-6">Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {articles.map((article) => (
          <ArticleCard
            key={article.id}
            article={article}
            initiallyBookmarked={bookmarkedIds.includes(article.id)}
          />
        ))}
      </div>
    </div>
  );
}
