package net.patrykdobrowolski.bookscanner.adapter.rest;

import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.exception.ShareCodeGenerationException;
import net.patrykdobrowolski.bookscanner.domain.exception.ShareCodeNotFoundException;
import net.patrykdobrowolski.bookscanner.adapter.rest.dto.CreateShareCodeRequestDto;
import net.patrykdobrowolski.bookscanner.adapter.rest.dto.ShareCodeDto;
import net.patrykdobrowolski.bookscanner.service.ShareCodeService;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/share-codes")
@RequiredArgsConstructor
public class ShareCodesResource {

    private final ShareCodeService shareCodeService;

    @PostMapping
    public ShareCodeDto createShareCode(@RequestBody CreateShareCodeRequestDto createShareCodeRequestDto) throws ShareCodeGenerationException {
        String shareCode = shareCodeService.generateShareCode(createShareCodeRequestDto.getSessionId());
        return ShareCodeDto.builder().sessionId(createShareCodeRequestDto.getSessionId()).code(shareCode).build();
    }

    @GetMapping("/{code}")
    public ShareCodeDto getShareCode(@PathVariable String code) throws ShareCodeNotFoundException {
        UUID sessionId = shareCodeService.getSessionByShareCode(code);
        return ShareCodeDto.builder().sessionId(sessionId).code(code).build();
    }
}
