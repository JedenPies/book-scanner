package net.patrykdobrowolski.bookscanner.adapter.db.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.patrykdobrowolski.bookscanner.domain.model.Modifier;
import net.patrykdobrowolski.bookscanner.domain.model.ScanStatus;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "scans")
@EntityListeners(AuditingEntityListener.class)
@Getter @Builder @NoArgsConstructor @AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ScanEntity {

    @Id
    private UUID id;

    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScanStatus status;

    @Column(nullable = false)
    private String isbn;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "book_details_id")
    private BookDetailsEntity bookDetails;

    private String errorMessage;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant modifiedAt;

    @Enumerated(EnumType.STRING)
    private Modifier modifiedBy;
}
