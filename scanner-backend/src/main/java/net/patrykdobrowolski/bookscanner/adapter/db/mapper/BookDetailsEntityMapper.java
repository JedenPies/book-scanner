package net.patrykdobrowolski.bookscanner.adapter.db.mapper;

import net.patrykdobrowolski.bookscanner.adapter.db.entity.BookDetailsEntity;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ISBNMapper.class)
public interface BookDetailsEntityMapper {

    BookDetailsEntity toEntity(BookDetails bookDetails);
    BookDetails fromEntity(BookDetailsEntity bookDetailsEntity);
}
