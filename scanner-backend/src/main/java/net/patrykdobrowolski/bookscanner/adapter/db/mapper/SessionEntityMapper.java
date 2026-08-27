package net.patrykdobrowolski.bookscanner.adapter.db.mapper;

import net.patrykdobrowolski.bookscanner.adapter.db.entity.SessionEntity;
import net.patrykdobrowolski.bookscanner.domain.model.Session;
import net.patrykdobrowolski.bookscanner.util.ISBNMapper;
import net.patrykdobrowolski.bookscanner.util.YearMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { ISBNMapper.class, YearMapper.class })
public interface SessionEntityMapper {

    SessionEntity toEntity(Session entity);
    Session fromEntity(SessionEntity entity);
}
