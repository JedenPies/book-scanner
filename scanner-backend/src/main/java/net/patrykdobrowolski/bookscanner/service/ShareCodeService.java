package net.patrykdobrowolski.bookscanner.service;

import jakarta.inject.Named;
import lombok.RequiredArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.exception.ShareCodeGenerationException;
import net.patrykdobrowolski.bookscanner.domain.exception.ShareCodeNotFoundException;
import net.patrykdobrowolski.bookscanner.adapter.redis.SessionShareCodeEntity;
import net.patrykdobrowolski.bookscanner.adapter.redis.SessionShareCodeRepository;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.UUID;

@Named
@RequiredArgsConstructor
public class ShareCodeService {

    private final static String ALLOWED_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final static SecureRandom random = new SecureRandom();

    private final SessionShareCodeRepository shareCodeRepository;

    public UUID getSessionByShareCode(String shareCode) throws ShareCodeNotFoundException {
        return shareCodeRepository.findByShareCode(shareCode).map(SessionShareCodeEntity::getSessionId).orElseThrow(ShareCodeNotFoundException::new);
    }

    public String generateShareCode(UUID sessionId) throws ShareCodeGenerationException {
        String shareCode = generateUniqueShareCode().orElseThrow(ShareCodeGenerationException::new);
        SessionShareCodeEntity entity = SessionShareCodeEntity.builder()
                .sessionId(sessionId)
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
