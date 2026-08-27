package net.patrykdobrowolski.bookscanner.adapter.db.mapper;

import net.patrykdobrowolski.bookscanner.adapter.db.entity.SessionEntity;
import net.patrykdobrowolski.bookscanner.domain.model.Session;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { ISBNMapper.class })
public interface SessionEntityMapper {

    SessionEntity toEntity(Session entity);
    Session fromEntity(SessionEntity entity);
}
