import { Routes, Route } from "react-router-dom";
import MainLayout from "../layouts/MainLayout";
import Dashboard from "../pages/Dashboard";
import Bookmarks from "../pages/Bookmarks";
import ReadingHistory from "../pages/ReadingHistory";
import Settings from "../pages/Settings";

export default function AppRoutes() {
  return (
    <Routes>
      <Route element={<MainLayout />}>
        <Route path="/" element={<Dashboard />} />
        <Route path="/bookmarks" element={<Bookmarks />} />
        <Route path="/reading-history" element={<ReadingHistory />} />
        <Route path="/settings" element={<Settings />} />
      </Route>
    </Routes>
  );
}
