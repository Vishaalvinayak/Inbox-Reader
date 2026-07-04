import type { Article } from "../types/article";

interface ArticleCardProps {
  article: Article;
}

export default function ArticleCard({ article }: ArticleCardProps) {
  return (
    <div className="bg-neutral-900 border border-neutral-800 rounded-lg p-5 hover:border-neutral-600 transition-colors cursor-pointer">
      <div className="flex items-center justify-between mb-2">
        <span className="text-xs font-medium text-emerald-400 uppercase tracking-wide">
          {article.categoryName}
        </span>
        <span className="text-xs text-neutral-500">
          {article.readingTimeMins} min read
        </span>
      </div>
      <h3 className="text-lg font-semibold text-neutral-100 mb-1">
        {article.subject}
      </h3>
      <p className="text-sm text-neutral-400 mb-3">{article.senderName}</p>
      <p className="text-sm text-neutral-300 line-clamp-2">
        {article.plainText}
      </p>
    </div>
  );
}
