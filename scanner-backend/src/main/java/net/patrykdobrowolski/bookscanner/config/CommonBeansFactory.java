package net.patrykdobrowolski.bookscanner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class CommonBeansFactory {

    @Bean(name = "apiFetchExecutor")
    public Executor apiFetchExecutor() {
        return Executors.newFixedThreadPool(20);
    }
}
