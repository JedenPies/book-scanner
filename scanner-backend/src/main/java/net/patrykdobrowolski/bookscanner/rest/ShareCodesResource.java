package net.patrykdobrowolski.bookscanner.rest;

import net.patrykdobrowolski.bookscanner.rest.dto.CreateShareCodeRequestDto;
import net.patrykdobrowolski.bookscanner.rest.dto.ShareCodeDto;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/share-codes")
public class ShareCodesResource {

    @PostMapping
    public ShareCodeDto createShareCode(@RequestBody CreateShareCodeRequestDto createShareCodeRequestDto) {
        return ShareCodeDto.builder().sessionId(createShareCodeRequestDto.getSessionId()).code("123-123").build();
    }

    @GetMapping("/{code}")
    public ShareCodeDto getShareCode(@PathVariable String code) {
        return ShareCodeDto.builder().sessionId(UUID.randomUUID()).code(code).build();
    }
}
