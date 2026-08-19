package net.patrykdobrowolski.bookscanner.googleapi;

import net.patrykdobrowolski.bookscanner.googleapi.dto.BooksResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "google-books-api", url = "${api.google-books.url}")
public interface GoogleBooksFeignClient {

    @GetMapping("/volumes")
    BooksResponseDto searchBooks(
            @RequestParam("q") String query,
            @RequestParam("key") String apiKey);
}
