package net.patrykdobrowolski.bookscanner.adapter.fetcher.bn.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AuthorsCleaner {

    private static final Pattern PARENTHESES_PATTERN = Pattern.compile("\\s*\\([^)]*\\)");
    private static final Pattern INITIAL_WITH_DOT_PATTERN = Pattern.compile("(?U)^\\p{L}\\.$");

    public static List<String> cleanAndExtract(String rawAuthors, String publisher) {
        if (rawAuthors == null || rawAuthors.isBlank()) {
            return Collections.emptyList();
        }

        Set<String> publisherTokens = extractPublisherTokens(publisher);

        String[] chunks = rawAuthors.split(",");
        if (chunks.length < 2) {
            String fallback = sanitizeSegment(rawAuthors, publisherTokens);
            return fallback.isBlank() ? Collections.emptyList() : List.of(fallback);
        }

        Set<String> uniqueAuthors = new LinkedHashSet<>();
        String currentLastName = extractLastWord(chunks[0]);

        for (int i = 1; i < chunks.length; i++) {
            String chunk = chunks[i].trim();
            boolean isLastChunk = (i == chunks.length - 1);

            String beforeParen = chunk;
            int parenIdx = chunk.indexOf('(');
            if (parenIdx != -1) {
                beforeParen = chunk.substring(0, parenIdx).trim();
            }

            if (isLastChunk) {
                String firstNames = filterWordsUntilPublisher(beforeParen, publisherTokens);
                if (!firstNames.isBlank() && !currentLastName.isBlank()) {
                    uniqueAuthors.add(firstNames + " " + currentLastName);
                }
            } else {
                String nextLastName = extractLastWord(chunk);
                String firstNamesPart = (parenIdx != -1) ? beforeParen : removeLastWord(beforeParen);
                String firstNames = filterWordsUntilPublisher(firstNamesPart, publisherTokens);

                if (!firstNames.isBlank() && !currentLastName.isBlank()) {
                    uniqueAuthors.add(firstNames + " " + currentLastName);
                }

                currentLastName = nextLastName;
            }
        }

        return new ArrayList<>(uniqueAuthors);
    }

    private static Set<String> extractPublisherTokens(String publisher) {
        if (publisher == null || publisher.isBlank()) {
            return Collections.emptySet();
        }
        return Arrays.stream(publisher.split("\\s+"))
                .map(AuthorsCleaner::cleanPunctuation)
                .map(String::toLowerCase)
                .filter(word -> word.length() > 1) // Ignorujemy 1-literowe tokeny, aby nie wyciąć inicjałów A., C.
                .collect(Collectors.toSet());
    }

    private static String filterWordsUntilPublisher(String text, Set<String> publisherTokens) {
        if (text == null || text.isBlank()) return "";

        String[] words = text.split("\\s+");
        StringBuilder sb = new StringBuilder();
        int count = 0;

        for (String word : words) {
            if (count >= 2) break;

            String cleaned = cleanPunctuation(word);
            if (cleaned.isBlank()) continue;

            String lower = cleaned.replaceAll("\\.$", "").toLowerCase();

            // KLUCZOWA ZMIANA: Gdy trafiamy na słowo z publishera, odcinamy wszystko do końca
            if (publisherTokens.contains(lower)) {
                break;
            }

            if (!sb.isEmpty()) sb.append(" ");
            sb.append(cleaned);
            count++;
        }

        return sb.toString();
    }

    private static String removeLastWord(String text) {
        String[] words = text.trim().split("\\s+");
        if (words.length <= 1) return "";
        return String.join(" ", Arrays.copyOf(words, words.length - 1));
    }

    private static String extractLastWord(String text) {
        String sanitized = PARENTHESES_PATTERN.matcher(text).replaceAll("").trim();
        String[] words = sanitized.split("\\s+");
        return cleanPunctuation(words[words.length - 1]);
    }

    private static String sanitizeSegment(String text, Set<String> publisherTokens) {
        String withoutParen = PARENTHESES_PATTERN.matcher(text).replaceAll("").trim();
        return filterWordsUntilPublisher(withoutParen, publisherTokens);
    }

    private static String cleanPunctuation(String text) {
        String trimmed = text.trim();
        if (INITIAL_WITH_DOT_PATTERN.matcher(trimmed).matches()) {
            return trimmed;
        }
        return trimmed.replaceAll("(?U)^[^\\p{L}]+|[^\\p{L}]+$", "").trim();
    }
}