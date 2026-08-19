package net.patrykdobrowolski.bookscanner.db.mapper;

import net.patrykdobrowolski.bookscanner.db.entity.BookDetailsEntity;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ISBNMapper.class)
public interface BookDetailsEntityMapper {

    BookDetailsEntity toEntity(BookDetails bookDetails);
    BookDetails fromEntity(BookDetailsEntity bookDetailsEntity);
}
