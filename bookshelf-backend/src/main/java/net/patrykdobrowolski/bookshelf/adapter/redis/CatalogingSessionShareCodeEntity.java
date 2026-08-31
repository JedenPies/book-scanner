package net.patrykdobrowolski.bookshelf.adapter.redis;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.UUID;

@RedisHash(value = "cataloging-session-share-code", timeToLive = 300)
@AllArgsConstructor @NoArgsConstructor
@Getter @Builder
public class CatalogingSessionShareCodeEntity {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID catalogingSessionId;

    @Indexed
    private String shareCode;
}