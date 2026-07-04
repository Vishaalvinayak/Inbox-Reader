import { useEffect, useState } from "react";
import { getBookmarkedArticleIds } from "../services/bookmarkService";
import { getArticleById } from "../services/articleService";
import ArticleCard from "../components/ArticleCard";
import type { Article } from "../types/article";

export default function Bookmarks() {
  const [articles, setArticles] = useState<Article[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getBookmarkedArticleIds(1)
      .then(async (ids) => {
        const fetched = await Promise.all(ids.map((id) => getArticleById(id)));
        setArticles(fetched);
      })
      .finally(() => setLoading(false));
  }, []);

  if (loading)
    return <div className="text-neutral-400 p-8">Loading bookmarks...</div>;

  if (articles.length === 0) {
    return (
      <div className="p-8 text-neutral-500">
        No bookmarks yet — star an article from the Dashboard to save it here.
      </div>
    );
  }

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold text-neutral-100 mb-6">Bookmarks</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
        {articles.map((article) => (
          <ArticleCard
            key={article.id}
            article={article}
            initiallyBookmarked={true}
          />
        ))}
      </div>
    </div>
  );
}
