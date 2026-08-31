package net.patrykdobrowolski.bookshelf.adapter.db.mapper;

import net.patrykdobrowolski.bookshelf.adapter.db.entity.ExportEntity;
import net.patrykdobrowolski.bookshelf.domain.model.export.Export;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExportEntityMapper {

    ExportEntity toEntity(Export export);
    Export fromEntity(ExportEntity exportEntity);
}
