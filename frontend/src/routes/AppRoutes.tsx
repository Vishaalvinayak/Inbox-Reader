import { Routes, Route } from "react-router-dom";
import MainLayout from "../layouts/MainLayout";
import Dashboard from "../pages/Dashboard";
import Categories from "../pages/Categories";
import Bookmarks from "../pages/Bookmarks";

export default function AppRoutes() {
  return (
    <Routes>
      <Route element={<MainLayout />}>
        <Route path="/" element={<Dashboard />} />
        <Route path="/categories" element={<Categories />} />
        <Route path="/bookmarks" element={<Bookmarks />} />
      </Route>
    </Routes>
  );
}
