import { useEffect, useState } from "react";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import type { Article } from "../types/article";
import { getArticleById } from "../services/articleService";
import { markArticleAsRead } from "../services/readingHistoryService";

interface ArticleDetailModalProps {
  article: Article;
  onClose: () => void;
}

// Converts the newsletter's custom "View image: (url)" lines into
// real markdown image syntax, and drops the redundant
// "Follow image link:" / bare "Caption:" lines that add no value.
/*function shortenUrlLabel(url: string): string {
  try {
    return new URL(url).hostname.replace(/^www\./, "");
  } catch {
    return url;
  }
}*/

function preprocessContent(raw: string): string {
  let text = raw
    .split("\n")
    .filter((line) => !line.trim().startsWith("Follow image link:"))
    .filter((line) => line.trim() !== "Caption:")
    .map((line) => {
      const match = line.match(/^View image:\s*\((.+?)\)\s*$/);
      if (match) {
        return `![](${match[1]})`;
      }
      return line;
    })
    .join("\n")
    .replace(/\*\*\s*\*\*/g, " ")
    .replace(/\*\*\s+/g, "**")
    .replace(/\s+\*\*/g, "**");

  // Normalize "url )" -> "url)" so the link-matching regexes below can work
  text = text.replace(/(https?:\/\/[^\s()]+)\s+\)/g, "$1)");

  // Convert "some label text (URL)" into a proper markdown link
  text = text.replace(
    /([^\n(\]]{2,150}?)\s*(?<!\])\((https?:\/\/[^\s)]+)\)/g,
    (_match, label, url) => `[${label.trim()}](${url})`,
  );

  // Any remaining bare URLs get their displayed text shortened to just the domain
  // Convert "some label text (URL)" into a proper markdown link.
  // The gap before "(" is restricted to same-line whitespace only —
  // otherwise this can wrongly grab the previous line's text as the label.
  text = text.replace(
    /([^\n(\]]{2,150}?)[ \t]*(?<!\])\((https?:\/\/[^\s)]+)\)/g,
    (_match, label, url) => `[${label.trim()}](${url})`,
  );

  return text;
}

export default function ArticleDetailModal({
  article,
  onClose,
}: ArticleDetailModalProps) {
  const [fullArticle, setFullArticle] = useState<Article | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let cancelled = false;
    setLoading(true);
    setFullArticle(null);

    getArticleById(article.id)
      .then((data) => {
        if (!cancelled) setFullArticle(data);
      })
      .catch((err) => console.error("Failed to load article", err))
      .finally(() => {
        if (!cancelled) setLoading(false);
      });

    markArticleAsRead(1, article.id).catch((err) =>
      console.error("Failed to mark article as read", err),
    );

    return () => {
      cancelled = true;
    };
  }, [article.id]);

  return (
    <div
      onClick={onClose}
      className="fixed inset-0 bg-black/70 flex items-center justify-center p-4 z-50"
    >
      <div className="relative w-full max-w-4xl">
        <button
          onClick={onClose}
          className="absolute -top-10 right-0 text-neutral-300 hover:text-white text-2xl"
          aria-label="Close"
        >
          ✕
        </button>

        <div
          onClick={(e) => e.stopPropagation()}
          className="bg-neutral-900 border border-neutral-800 rounded-lg w-full max-h-[85vh] overflow-y-auto p-8"
        >
          <div className="flex items-center justify-between mb-2">
            <span className="text-xs font-medium text-emerald-400 uppercase tracking-wide">
              {article.gmailLabel}
            </span>
            {article.readingTimeMins !== undefined && (
              <span className="text-xs text-neutral-500">
                {article.readingTimeMins} min read
              </span>
            )}
          </div>
          <h2 className="text-xl font-bold text-neutral-100 mb-1">
            {article.title}
          </h2>
          <p className="text-sm text-neutral-400 mb-4">{article.senderName}</p>

          {loading && (
            <div className="text-neutral-400 text-sm">
              Loading full article...
            </div>
          )}

          {!loading && fullArticle && (
            <div className="text-base text-neutral-300 leading-relaxed space-y-3 [&_a]:text-emerald-400 [&_a]:underline [&_img]:rounded-md [&_img]:my-2 [&_img]:max-w-full [&_h1]:text-xl [&_h1]:font-bold [&_h1]:text-neutral-100 [&_h2]:text-lg [&_h2]:font-bold [&_h2]:text-neutral-100 [&_strong]:text-neutral-100 [&_ul]:list-disc [&_ul]:pl-5 [&_hr]:border-neutral-700">
              <ReactMarkdown
                remarkPlugins={[remarkGfm]}
                components={{
                  a: (props) => (
                    <a {...props} target="_blank" rel="noopener noreferrer" />
                  ),
                }}
              >
                {preprocessContent(fullArticle.content ?? "")}
              </ReactMarkdown>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
