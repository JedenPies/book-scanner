package net.patrykdobrowolski.bookshelf.adapter.db.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "books_details")
@EntityListeners(AuditingEntityListener.class)
@Builder @NoArgsConstructor @AllArgsConstructor(access = lombok.AccessLevel.PRIVATE)
@Getter
public class BookDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private Set<String> sources;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant modifiedAt;

    @Column(nullable = false)
    private String title;

    private List<String> authors;

    private String publisher;
    private String publicationYear;
    private String publicationPlace;
    private String language;

    private List<String> genres;
}
