package net.patrykdobrowolski.bookscanner.db.mapper;

import net.patrykdobrowolski.bookscanner.db.entity.SessionEntity;
import net.patrykdobrowolski.bookscanner.domain.model.Session;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionEntityMapper {

    SessionEntity toEntity(Session entity);
    Session fromEntity(SessionEntity entity);
}
