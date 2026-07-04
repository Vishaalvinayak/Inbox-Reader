import { useEffect, useState } from "react";
import { getArticles } from "../services/articleService";
import ArticleCard from "../components/ArticleCard";
import type { Article } from "../types/article";

export default function Dashboard() {
  const [articles, setArticles] = useState<Article[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getArticles(1)
      .then(setArticles)
      .catch((err: Error) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading)
    return <div className="text-neutral-400 p-8">Loading articles...</div>;
  if (error) return <div className="text-red-400 p-8">Error: {error}</div>;

  return (
    <div className="min-h-screen bg-black p-8">
      <h1 className="text-2xl font-bold text-neutral-100 mb-6">Inbox Reader</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {articles.map((article) => (
          <ArticleCard key={article.id} article={article} />
        ))}
      </div>
    </div>
  );
}
