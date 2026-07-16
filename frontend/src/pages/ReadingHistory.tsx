import { useEffect, useState } from "react";
import { getReadingHistoryDetails } from "../services/readingHistoryService";
import type { ReadingHistoryItem } from "../types/readingHistory";

function formatGroupLabel(dateKey: string): string {
  const today = new Date();
  const todayKey = today.toDateString();

  const yesterday = new Date(today);
  yesterday.setDate(yesterday.getDate() - 1);
  const yesterdayKey = yesterday.toDateString();

  if (dateKey === todayKey) return "Today";
  if (dateKey === yesterdayKey) return "Yesterday";

  return new Date(dateKey).toLocaleDateString(undefined, {
    weekday: "long",
    month: "long",
    day: "numeric",
    year: "numeric",
  });
}

function groupByDate(
  items: ReadingHistoryItem[],
): [string, ReadingHistoryItem[]][] {
  const groups = new Map<string, ReadingHistoryItem[]>();

  for (const item of items) {
    const dateKey = new Date(item.readAt).toDateString();
    if (!groups.has(dateKey)) groups.set(dateKey, []);
    groups.get(dateKey)!.push(item);
  }

  return Array.from(groups.entries());
}

export default function ReadingHistory() {
  const [items, setItems] = useState<ReadingHistoryItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getReadingHistoryDetails(1)
      .then(setItems)
      .catch((err: Error) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading)
    return (
      <div className="text-neutral-400 p-8">Loading reading history...</div>
    );
  if (error) return <div className="text-red-400 p-8">Error: {error}</div>;

  if (items.length === 0) {
    return (
      <div className="p-8 text-neutral-500">
        No reading history yet — open an article from the Dashboard to see it
        here.
      </div>
    );
  }

  const grouped = groupByDate(items);

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold text-neutral-100 mb-6">
        Reading History
      </h1>
      <div className="space-y-8">
        {grouped.map(([dateKey, group]) => (
          <div key={dateKey}>
            <h2 className="text-sm font-semibold text-neutral-500 uppercase tracking-wide mb-3">
              {formatGroupLabel(dateKey)}
            </h2>
            <div className="space-y-3">
              {group.map((item) => (
                <div
                  key={item.articleId}
                  className="bg-neutral-900 border border-neutral-800 rounded-lg p-4"
                >
                  <div className="flex items-center justify-between mb-1">
                    <span className="text-xs font-medium text-emerald-400 uppercase tracking-wide">
                      {item.gmailLabel}
                    </span>
                    <span className="text-xs text-neutral-500">
                      {new Date(item.readAt).toLocaleTimeString(undefined, {
                        hour: "numeric",
                        minute: "2-digit",
                      })}
                    </span>
                  </div>
                  <h3 className="text-base font-semibold text-neutral-100 mb-1">
                    {item.title}
                  </h3>
                  <p className="text-sm text-neutral-400 mb-2">
                    {item.senderName}
                  </p>
                  <p className="text-sm text-neutral-300 line-clamp-2">
                    {item.snippet}
                  </p>
                </div>
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
