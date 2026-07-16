package com.inboxreader.backend.service.impl;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;
import com.inboxreader.backend.config.GmailApiClientFactory;
import com.inboxreader.backend.entity.Article;
import com.inboxreader.backend.entity.User;
import com.inboxreader.backend.repository.ArticleRepository;
import com.inboxreader.backend.repository.UserRepository;
import com.inboxreader.backend.service.GmailAuthService;
import com.inboxreader.backend.service.GmailSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.inboxreader.backend.repository.SyncMetadataRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
public class GmailSyncServiceImpl implements GmailSyncService {

    private static final long MAX_RESULTS = 50L;

    // sender email -> friendly newsletter name, shown in gmail_label
    private static final Map<String, String> TRACKED_SENDERS = new LinkedHashMap<>();
    static {
        TRACKED_SENDERS.put("futuretools@mail.beehiiv.com", "Matt from Future Tools");
        TRACKED_SENDERS.put("crew@morningbrew.com", "Morning Brew");
        TRACKED_SENDERS.put("techbrew@morningbrew.com", "Tech Brew");
        TRACKED_SENDERS.put("theaireport@mail.beehiiv.com", "The AI Report");
        TRACKED_SENDERS.put("news@thehustle.co", "The Hustle");
        TRACKED_SENDERS.put("newsletter@email.roadmap.sh", "The Roadmap");
    }

    private final GmailAuthService gmailAuthService;
    private final GmailApiClientFactory clientFactory;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    private static final int MAX_PAGES = 20; // safety ceiling: 20 pages * 50 = up to 1000 messages per sync

    @Override
    public int syncNewsletters(Long userId) {
        try {
            String accessToken = gmailAuthService.getValidAccessToken(userId);
            Gmail gmail = clientFactory.build(accessToken);
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalStateException("User not found: " + userId));
            String query = buildSenderQuery(userId);
            log.info("Gmail sync query: {}", query);

            int importedCount = 0;
            int totalSeen = 0;
            int totalSkippedDupe = 0;
            String pageToken = null;
            int pagesFetched = 0;

            do {
                ListMessagesResponse listResponse = gmail.users().messages()
                        .list("me")
                        .setQ(query)
                        .setMaxResults(MAX_RESULTS)
                        .setPageToken(pageToken)
                        .execute();
                List<Message> messageRefs = listResponse.getMessages();
                pagesFetched++;
                log.info("Page {}: {} messages returned", pagesFetched,
                        messageRefs != null ? messageRefs.size() : 0);

                if (messageRefs != null && !messageRefs.isEmpty()) {
                    for (Message ref : messageRefs) {
                        totalSeen++;
                        if (articleRepository.existsByGmailMessageId(ref.getId())) {
                            totalSkippedDupe++;
                            continue;
                        }
                        Message fullMessage = gmail.users().messages()
                                .get("me", ref.getId())
                                .setFormat("full")
                                .execute();
                        String from = getHeader(fullMessage, "From");
                        log.info("Importing message id={} from={}", ref.getId(), from);
                        Article article = mapToArticle(fullMessage, user);
                        articleRepository.save(article);
                        importedCount++;
                    }
                }
                pageToken = listResponse.getNextPageToken();
            } while (pageToken != null && pagesFetched < MAX_PAGES);

            log.info("Sync complete: totalSeen={}, skippedDupe={}, imported={}, pagesFetched={}",
                    totalSeen, totalSkippedDupe, importedCount, pagesFetched);

            return importedCount;

        }  catch (Exception e) {
            log.error("Gmail sync failed for user {}", userId, e);
            throw new RuntimeException("Gmail sync failed for user " + userId, e);
        }
    }
    

    private static final int DEFAULT_LOOKBACK_DAYS = 7;

private String buildSenderQuery(Long userId) {
    String senders = TRACKED_SENDERS.keySet().stream()
            .collect(Collectors.joining(" OR "));

    LocalDate afterDate = syncMetadataRepository.findLatestSuccessfulByUserId(userId)
            .map(sm -> sm.getLastSyncedAt().toLocalDate())
            .orElse(LocalDate.now().minusDays(DEFAULT_LOOKBACK_DAYS));

    String afterStr = afterDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

    return "from:(" + senders + ") after:" + afterStr;
}

    private Article mapToArticle(Message message, User user) {
        String subject = getHeader(message, "Subject");
        String from = getHeader(message, "From");

        String senderName = extractSenderName(from);
        String senderEmail = extractSenderEmail(from);

        String source = TRACKED_SENDERS.entrySet().stream()
                .filter(e -> senderEmail != null && senderEmail.equalsIgnoreCase(e.getKey()))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(senderName);

        OffsetDateTime receivedAt = Instant.ofEpochMilli(message.getInternalDate())
                .atOffset(ZoneOffset.UTC);

        String content = extractBody(message.getPayload());

        return new Article(
                null,
                user,
                subject != null ? subject : "(no subject)",
                senderName,
                senderEmail,
                message.getSnippet(),
                content,
                message.getId(),
                source,
                receivedAt,
                OffsetDateTime.now()
        );
    }

    private String getHeader(Message message, String name) {
        if (message.getPayload() == null || message.getPayload().getHeaders() == null) {
            return null;
        }
        return message.getPayload().getHeaders().stream()
                .filter(h -> h.getName().equalsIgnoreCase(name))
                .map(MessagePartHeader::getValue)
                .findFirst()
                .orElse(null);
    }

    private String extractSenderEmail(String from) {
        if (from == null) return null;
        int start = from.indexOf('<');
        int end = from.indexOf('>');
        if (start != -1 && end != -1) {
            return from.substring(start + 1, end).trim();
        }
        return from.trim();
    }

    private String extractSenderName(String from) {
        if (from == null) return null;
        int start = from.indexOf('<');
        if (start > 0) {
            return from.substring(0, start).replace("\"", "").trim();
        }
        return from.trim();
    }

   private String extractBody(MessagePart payload) {
        String htmlText = extractMimePart(payload, "text/html");

        if (htmlText != null && !htmlText.isBlank()) {
            return htmlToMarkdown(htmlText);
        }

        // Fallback: no HTML part at all — use plain text as-is
        String plainText = extractMimePart(payload, "text/plain");
        return plainText != null ? plainText : "";
    }
    private String extractMimePart(MessagePart payload, String mimeType) {
        if (payload == null) return null;

        if (mimeType.equalsIgnoreCase(payload.getMimeType()) && payload.getBody() != null
                && payload.getBody().getData() != null) {
            return decodeBase64(payload.getBody().getData());
        }

        if (payload.getParts() != null) {
            for (MessagePart part : payload.getParts()) {
                String result = extractMimePart(part, mimeType);
                if (result != null && !result.isEmpty()) {
                    return result;
                }
            }
        }

        return null;
    }

    // Some senders (e.g. Tech Brew) put only a short "view online" placeholder
    // in their text/plain part and keep the real newsletter body in text/html.
    private boolean isUsablePlainText(String plainText) {
        if (plainText == null || plainText.isBlank()) {
            return false;
        }
        String normalized = plainText.toLowerCase();
        boolean looksLikePlaceholder = normalized.contains("scrambling the email")
                || normalized.contains("read it in full online");
        return !looksLikePlaceholder && plainText.trim().length() > 200;
    }

    private String htmlToMarkdown(String html) {
        Document doc = Jsoup.parse(html);
        StringBuilder sb = new StringBuilder();
        appendNode(doc.body(), sb);
        return sb.toString().replaceAll("\n{3,}", "\n\n").trim();
    }

    private void appendNode(Node node, StringBuilder sb) {
        if (node instanceof TextNode textNode) {
            String text = textNode.text();
            if (!text.isBlank()) {
                sb.append(text);
            }
            return;
        }

        if (!(node instanceof Element element)) {
            return;
        }

        switch (element.tagName()) {
            case "a" -> {
                String href = element.absUrl("href");
                String text = element.text();
                if (!text.isBlank() && !href.isBlank()) {
                    sb.append("[").append(text).append("](").append(href).append(")");
                } else {
                    for (Node child : element.childNodes()) appendNode(child, sb);
                }
            }
            case "img" -> {
                String src = element.absUrl("src");
                if (!src.isBlank()) {
                    sb.append("\n![](").append(src).append(")\n");
                }
            }
            case "br" -> sb.append("\n");
            case "script", "style" -> { /* skip - no visible content */ }
            default -> {
                boolean isBlock = element.isBlock();
                if (isBlock) sb.append("\n");
                for (Node child : element.childNodes()) {
                    appendNode(child, sb);
                }
                if (isBlock) sb.append("\n");
            }
        }
    }
    private String decodeBase64(String data) {
        byte[] decoded = Base64.getUrlDecoder().decode(data);
        return new String(decoded, java.nio.charset.StandardCharsets.UTF_8);
    }
    private final SyncMetadataRepository syncMetadataRepository;
}