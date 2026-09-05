package net.patrykdobrowolski.bookshelf.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.port.BookFetchJobRepositoryPort;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MetricsConfig {

    private final MeterRegistry meterRegistry;
    private final BookFetchJobRepositoryPort bookRepository;

    @PostConstruct
    public void registerMetrics() {
        Gauge.builder("books.total.count", bookRepository, BookFetchJobRepositoryPort::count)
                .description("Total number of books in the database")
                .register(meterRegistry);
    }
}

