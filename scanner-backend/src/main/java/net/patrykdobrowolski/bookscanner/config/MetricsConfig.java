package net.patrykdobrowolski.bookscanner.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.port.BookRepositoryPort;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MetricsConfig {

    private final MeterRegistry meterRegistry;
    private final BookRepositoryPort bookRepository;

    @PostConstruct
    public void registerMetrics() {
        Gauge.builder("books.total.count", bookRepository, BookRepositoryPort::count)
                .description("Total number of books in the database")
                .register(meterRegistry);
    }
}

