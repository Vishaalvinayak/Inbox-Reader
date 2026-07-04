import { useEffect, useState } from "react";
import { getCategories } from "../services/categoryService";
import type { Category } from "../types/category";

export default function Categories() {
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getCategories()
      .then(setCategories)
      .finally(() => setLoading(false));
  }, []);

  if (loading)
    return <div className="text-neutral-400 p-8">Loading categories...</div>;

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold text-neutral-100 mb-6">Categories</h1>
      <div className="grid grid-cols-2 md:grid-cols-3 gap-3">
        {categories.map((cat) => (
          <div
            key={cat.id}
            className="bg-neutral-900 border border-neutral-800 rounded-lg p-4 text-center text-neutral-200 font-medium hover:border-emerald-500/50 transition-colors cursor-pointer"
          >
            {cat.name}
          </div>
        ))}
      </div>
    </div>
  );
}
