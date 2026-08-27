package net.patrykdobrowolski.bookscanner.adapter.rest.mapper;

import net.patrykdobrowolski.bookscanner.domain.model.Session;
import net.patrykdobrowolski.bookscanner.adapter.rest.dto.SessionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionDtoMapper {

    SessionDto toDto(Session session);
}
