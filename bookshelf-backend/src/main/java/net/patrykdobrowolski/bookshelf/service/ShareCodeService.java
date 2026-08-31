package net.patrykdobrowolski.bookshelf.service;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookshelf.domain.exception.ShareCodeGenerationException;
import net.patrykdobrowolski.bookshelf.domain.exception.ShareCodeNotFoundException;
import net.patrykdobrowolski.bookshelf.adapter.redis.CatalogingSessionShareCodeEntity;
import net.patrykdobrowolski.bookshelf.adapter.redis.CatalogingSessionShareCodeRepository;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

@Named
@RequiredArgsConstructor
public class ShareCodeService {

    private final static String ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final static SecureRandom random = new SecureRandom();

    private final CatalogingSessionShareCodeRepository shareCodeRepository;

    public UUID getSessionByShareCode(String shareCode) throws ShareCodeNotFoundException {
        return shareCodeRepository.findByShareCode(shareCode).map(CatalogingSessionShareCodeEntity::getCatalogingSessionId).orElseThrow(ShareCodeNotFoundException::new);
    }

    public String generateShareCode(UUID sessionId) throws ShareCodeGenerationException {
        String shareCode = generateUniqueShareCode().orElseThrow(ShareCodeGenerationException::new);
        CatalogingSessionShareCodeEntity entity = CatalogingSessionShareCodeEntity.builder()
                .catalogingSessionId(sessionId)
                .shareCode(shareCode)
                .build();
        shareCodeRepository.save(entity);
        return entity.getShareCode();
    }

    private Optional<String> generateUniqueShareCode() {
        for (int i = 0; i < 3; i++) {
            String genShareCode = generateRandomCode();
            if (shareCodeRepository.findByShareCode(genShareCode).isEmpty())
                return Optional.of(genShareCode);

        }
        return Optional.empty();
    }

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(ALLOWED_CHARS.charAt(random.nextInt(ALLOWED_CHARS.length())));
        }
        return code.toString();
    }
}
