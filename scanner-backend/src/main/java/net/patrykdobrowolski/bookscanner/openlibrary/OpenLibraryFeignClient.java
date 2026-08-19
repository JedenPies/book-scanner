package net.patrykdobrowolski.bookscanner.openlibrary;

import net.patrykdobrowolski.bookscanner.openlibrary.dto.BookDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "openlibrary-api", url = "${api.open-library.url}")
public interface OpenLibraryFeignClient {

    @GetMapping("api/books?format=json&jscmd=data")
    Map<String, BookDto> searchBooks(@RequestParam("bibkeys") String bibkeys);
}
