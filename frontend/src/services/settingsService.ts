import api from "./api";

export interface Settings {
  theme: string;
  gmailLabelName: string;
  syncFrequencyMinutes: number;
}

export interface UpdateSettingsRequest {
  theme?: string;
  gmailLabelName?: string;
  syncFrequencyMinutes?: number;
}

export const getSettings = async (userId: number = 1): Promise<Settings> => {
  const response = await api.get<Settings>("/settings", {
    params: { userId },
  });
  return response.data;
};

export const updateSettings = async (
  userId: number = 1,
  data: UpdateSettingsRequest,
): Promise<Settings> => {
  const response = await api.put<Settings>("/settings", data, {
    params: { userId },
  });
  return response.data;
};
