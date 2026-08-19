package net.patrykdobrowolski.bookscanner.bn;

import net.patrykdobrowolski.bookscanner.bn.dto.BnResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "bn-api", url = "${api.bn.url}")
public interface BnFeignClient {

    @GetMapping("/api/bibs.json")
    BnResponseDto searchBooks(@RequestParam("isbnIssn") String isbn);

}
