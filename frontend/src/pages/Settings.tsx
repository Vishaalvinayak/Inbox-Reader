import { useEffect, useState } from "react";
import { getSettings, updateSettings } from "../services/settingsService";
import type { Settings as SettingsType } from "../services/settingsService";
import { useTheme } from "../contexts/ThemeContext";

export default function Settings() {
  const { setTheme } = useTheme();
  const [settings, setSettings] = useState<SettingsType | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [saved, setSaved] = useState(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getSettings(1)
      .then(setSettings)
      .catch((err: Error) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  const handleSave = async () => {
    if (!settings) return;
    setSaving(true);
    setSaved(false);
    try {
      const updated = await updateSettings(1, {
        theme: settings.theme,
        gmailLabelName: settings.gmailLabelName,
        syncFrequencyMinutes: settings.syncFrequencyMinutes,
      });
      setSettings(updated);
      setSaved(true);
      setTimeout(() => setSaved(false), 2000);
    } catch (err) {
      console.error("Failed to save settings", err);
      setError("Failed to save settings");
    } finally {
      setSaving(false);
    }
  };

  if (loading)
    return <div className="text-neutral-400 p-8">Loading settings...</div>;
  if (error) return <div className="text-red-400 p-8">Error: {error}</div>;
  if (!settings) return null;

  return (
    <div className="p-8 max-w-lg">
      <h1 className="text-2xl font-bold text-neutral-100 mb-6">Settings</h1>

      <div className="space-y-6">
        <div>
          <label className="block text-sm font-medium text-neutral-300 mb-1">
            Theme
          </label>
          <select
            value={settings.theme}
            onChange={(e) => {
              const newTheme = e.target.value;
              setSettings({ ...settings, theme: newTheme });
              setTheme(newTheme as "light" | "dark");
            }}
            className="w-full bg-neutral-900 border border-neutral-800 rounded-md px-3 py-2 text-neutral-100"
          >
            <option value="light">Light</option>
            <option value="dark">Dark</option>
          </select>
        </div>

        <div>
          <label className="block text-sm font-medium text-neutral-300 mb-1">
            Sync frequency (minutes)
          </label>
          <input
            type="number"
            min={1}
            value={settings.syncFrequencyMinutes}
            onChange={(e) =>
              setSettings({
                ...settings,
                syncFrequencyMinutes: Number(e.target.value),
              })
            }
            className="w-full bg-neutral-900 border border-neutral-800 rounded-md px-3 py-2 text-neutral-100"
          />
          <p className="text-xs text-neutral-500 mt-1">
            Not yet active — automatic scheduled sync is coming in a future
            update.
          </p>
        </div>

        <div>
          <label className="block text-sm font-medium text-neutral-300 mb-1">
            Gmail label name
          </label>
          <input
            type="text"
            value={settings.gmailLabelName}
            onChange={(e) =>
              setSettings({ ...settings, gmailLabelName: e.target.value })
            }
            className="w-full bg-neutral-900 border border-neutral-800 rounded-md px-3 py-2 text-neutral-100"
          />
          <p className="text-xs text-neutral-500 mt-1">
            Currently unused — sync filters by sender, not by Gmail label.
          </p>
        </div>

        <button
          onClick={handleSave}
          disabled={saving}
          className="bg-emerald-500/10 text-emerald-400 border border-emerald-500/30 rounded-md px-4 py-2 text-sm font-medium hover:bg-emerald-500/20 transition-colors disabled:opacity-50"
        >
          {saving ? "Saving..." : "Save Settings"}
        </button>

        {saved && <span className="ml-3 text-sm text-emerald-400">Saved!</span>}
      </div>
    </div>
  );
}
