package net.patrykdobrowolski.bookscanner.db.mapper;

import net.patrykdobrowolski.bookscanner.db.entity.ScanEntity;
import net.patrykdobrowolski.bookscanner.domain.model.Scan;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = { BookDetailsEntityMapper.class, ISBNMapper.class })
public interface ScanEntityMapper {

    ScanEntity toEntity(Scan scan);
    Scan fromEntity(ScanEntity entity);
}
