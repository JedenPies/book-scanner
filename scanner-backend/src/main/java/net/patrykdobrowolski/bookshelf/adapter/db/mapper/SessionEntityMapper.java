package net.patrykdobrowolski.bookshelf.adapter.db.mapper;

import net.patrykdobrowolski.bookshelf.adapter.db.entity.SessionEntity;
import net.patrykdobrowolski.bookshelf.domain.model.Session;
import net.patrykdobrowolski.bookshelf.util.ISBNMapper;
import net.patrykdobrowolski.bookshelf.util.YearMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { ISBNMapper.class, YearMapper.class })
public interface SessionEntityMapper {

    SessionEntity toEntity(Session entity);
    Session fromEntity(SessionEntity entity);
}
