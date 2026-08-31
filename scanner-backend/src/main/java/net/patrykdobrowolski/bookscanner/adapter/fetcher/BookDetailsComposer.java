package net.patrykdobrowolski.bookscanner.adapter.fetcher;

import net.patrykdobrowolski.bookscanner.domain.model.value.BookDetails;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class BookDetailsComposer {

    private final List<BookDetails> sortedDetails;

    public BookDetailsComposer(Collection<BookDetails> details) {
        this.sortedDetails = Optional.ofNullable(details)
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(Objects::nonNull)
                .sorted(BookDetailsComparator.INSTANCE)
                .toList();
    }

    public BookDetails compose() {
        if (sortedDetails.isEmpty()) {
            return BookDetails.builder().build();
        }
        Set<String> contributingSources = new HashSet<>();
        return BookDetails.builder()
                .title(extractAndRecord(BookDetails::title, contributingSources))
                .publisher(extractAndRecord(BookDetails::publisher, contributingSources))
                .publicationPlace(extractAndRecord(BookDetails::publicationPlace, contributingSources))
                .publicationYear(extractAndRecord(BookDetails::publicationYear, contributingSources))
                .authors(extractAndRecordCollection(BookDetails::authors, contributingSources))
                .language(extractAndRecord(BookDetails::language, contributingSources))
                .genres(extractAndJoin(BookDetails::genres, contributingSources))
                .sources(contributingSources)
                .build();
    }

    private <T> @Nullable T extractAndRecord(Function<BookDetails, T> getter, Set<String> sourcesRef) {
        return sortedDetails.stream()
                .filter(bd -> getter.apply(bd) != null)
                .findFirst()
                .map(bd -> {
                    if (bd.sources() != null) {
                        sourcesRef.addAll(bd.sources());
                    }
                    return getter.apply(bd);
                })
                .orElse(null);
    }
    private <T> @Nullable List<T> extractAndRecordCollection(Function<BookDetails, ? extends Collection<T>> getter, Set<String> sourcesRef) {
        return (List<T>) sortedDetails.stream()
                .filter(bd -> getter.apply(bd) != null && !getter.apply(bd).isEmpty())
                .findFirst()
                .map(bd -> {
                    if (bd.sources() != null) {
                        sourcesRef.addAll(bd.sources());
                    }
                    return getter.apply(bd);
                })
                .orElse(null);
    }

    private <T> List<T> extractAndJoin(Function<BookDetails, List<T>> getter, Set<String> sourcesRef) {
        return sortedDetails.stream()
                .filter(r -> getter.apply(r) != null)
                .peek(r -> {
                    if (r.sources() != null) sourcesRef.addAll(r.sources());
                })
                .map(getter)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .toList();
    }

    private static class BookDetailsComparator implements Comparator<BookDetails> {

        private final static BookDetailsComparator INSTANCE = new BookDetailsComparator();

        private final List<String> providers = List.of("biblioteka-narodowa", "google", "open-library");

        @Override
        public int compare(BookDetails o1, BookDetails o2) {
            int score1 = getHighestPriority(o1);
            int score2 = getHighestPriority(o2);
            return Integer.compare(score1, score2);
        }

        private int getHighestPriority(BookDetails bd) {
            if (bd == null || bd.sources() == null || bd.sources().isEmpty()) {
                return Integer.MAX_VALUE;
            }
            return bd.sources().stream()
                    .map(source -> {
                        int idx = providers.indexOf(source);
                        return idx == -1 ? Integer.MAX_VALUE - 1 : idx;
                    })
                    .min(Integer::compare)
                    .orElse(Integer.MAX_VALUE);
        }
    }
}
