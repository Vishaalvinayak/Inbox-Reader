export interface Article {
  id: number;
  senderName: string;
  title: string;
  snippet: string;
  readingTimeMins: number;
  gmailLabel: string;
  receivedAt: string; // OffsetDateTime serializes to an ISO string over JSON
}
