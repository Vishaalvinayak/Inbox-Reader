export interface Article {
  id: number;
  senderName: string;
  subject: string;
  plainText: string;
  readingTimeMins: number;
  categoryName: string;
  receivedAt: string; // Instant serializes to an ISO string over JSON
}
