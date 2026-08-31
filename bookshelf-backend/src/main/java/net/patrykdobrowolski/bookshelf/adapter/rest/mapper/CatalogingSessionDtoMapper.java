package net.patrykdobrowolski.bookshelf.adapter.rest.mapper;

import net.patrykdobrowolski.bookshelf.domain.model.cataloging.CatalogingSession;
import net.patrykdobrowolski.bookshelf.adapter.rest.dto.CatalogingSessionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CatalogingSessionDtoMapper {

    CatalogingSessionDto toDto(CatalogingSession catalogingSession);
}
