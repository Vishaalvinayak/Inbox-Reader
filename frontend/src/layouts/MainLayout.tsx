import { Outlet, NavLink } from "react-router-dom";

export default function MainLayout() {
  const linkClass = ({ isActive }: { isActive: boolean }) =>
    `block px-4 py-2 rounded-md text-sm font-medium transition-colors ${
      isActive
        ? "bg-emerald-500/10 text-emerald-400"
        : "text-neutral-400 hover:text-neutral-200 hover:bg-neutral-900"
    }`;

  return (
    <div className="min-h-screen bg-black flex">
      <aside className="w-56 border-r border-neutral-800 p-4">
        <h1 className="text-lg font-bold text-neutral-100 mb-6 px-2">
          Inbox Reader
        </h1>
        <nav className="space-y-1">
          <NavLink to="/" end className={linkClass}>
            Dashboard
          </NavLink>
          <NavLink to="/categories" className={linkClass}>
            Categories
          </NavLink>
          <NavLink to="/bookmarks" className={linkClass}>
            Bookmarks
          </NavLink>
        </nav>
      </aside>
      <main className="flex-1">
        <Outlet />
      </main>
    </div>
  );
}
