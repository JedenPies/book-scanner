package net.patrykdobrowolski.bookshelf.adapter.rest.mapper;

import net.patrykdobrowolski.bookshelf.domain.model.Session;
import net.patrykdobrowolski.bookshelf.adapter.rest.dto.SessionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionDtoMapper {

    SessionDto toDto(Session session);
}
