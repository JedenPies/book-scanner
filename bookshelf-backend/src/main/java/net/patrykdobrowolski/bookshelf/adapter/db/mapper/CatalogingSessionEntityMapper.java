package net.patrykdobrowolski.bookshelf.adapter.db.mapper;

import net.patrykdobrowolski.bookshelf.adapter.db.entity.CatalogingSessionEntity;
import net.patrykdobrowolski.bookshelf.domain.model.cataloging.CatalogingSession;
import net.patrykdobrowolski.bookshelf.util.ISBNMapper;
import net.patrykdobrowolski.bookshelf.util.YearMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { ISBNMapper.class, YearMapper.class, BookDetailsEntityMapper.class })
public interface CatalogingSessionEntityMapper {

    CatalogingSessionEntity toEntity(CatalogingSession entity);
    CatalogingSession fromEntity(CatalogingSessionEntity entity);
}
