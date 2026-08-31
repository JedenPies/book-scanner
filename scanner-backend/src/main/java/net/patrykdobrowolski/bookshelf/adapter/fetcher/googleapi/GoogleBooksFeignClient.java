package net.patrykdobrowolski.bookshelf.adapter.fetcher.googleapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "google-books-api", url = "${api.google-books.url}")
public interface GoogleBooksFeignClient {

    @GetMapping("/volumes")
    String searchBooks(
            @RequestParam("q") String query,
            @RequestParam("key") String apiKey);
}
