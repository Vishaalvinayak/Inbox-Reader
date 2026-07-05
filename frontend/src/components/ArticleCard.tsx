import { useState } from "react";
import type { Article } from "../types/article";
import { toggleBookmark } from "../services/bookmarkService";

interface ArticleCardProps {
  article: Article;
  initiallyBookmarked?: boolean;
}

export default function ArticleCard({
  article,
  initiallyBookmarked = false,
}: ArticleCardProps) {
  const [bookmarked, setBookmarked] = useState(initiallyBookmarked);
  const [pending, setPending] = useState(false);

  const handleToggle = async (e: React.MouseEvent) => {
    e.stopPropagation();
    if (pending) return;
    setPending(true);
    try {
      const result = await toggleBookmark(1, article.id);
      setBookmarked(result.bookmarked);
    } catch (err) {
      console.error("Failed to toggle bookmark", err);
    } finally {
      setPending(false);
    }
  };

  return (
    <div className="bg-neutral-900 border border-neutral-800 rounded-lg p-5 hover:border-neutral-600 transition-colors cursor-pointer relative">
      <button
        onClick={handleToggle}
        disabled={pending}
        className="absolute top-4 right-4 text-lg"
        aria-label="Toggle bookmark"
      >
        {bookmarked ? "★" : "☆"}
      </button>
      <div className="flex items-center justify-between mb-2 pr-6">
        <span className="text-xs font-medium text-emerald-400 uppercase tracking-wide">
          {article.gmailLabel}
        </span>
        <span className="text-xs text-neutral-500">
          {article.readingTimeMins} min read
        </span>
      </div>
      <h3 className="text-lg font-semibold text-neutral-100 mb-1">
        {article.title}
      </h3>
      <p className="text-sm text-neutral-400 mb-3">{article.senderName}</p>
      <p className="text-sm text-neutral-300 line-clamp-2">{article.snippet}</p>
    </div>
  );
}
