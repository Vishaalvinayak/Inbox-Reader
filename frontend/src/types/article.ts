export interface Article {
  id: number;
  senderName: string;
  title: string;
  snippet: string;
  content?: string;
  readingTimeMins?: number;
  gmailLabel: string;
  receivedAt: string;
}
