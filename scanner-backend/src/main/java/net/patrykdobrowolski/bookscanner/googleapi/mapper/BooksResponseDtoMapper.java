package net.patrykdobrowolski.bookscanner.googleapi.mapper;

import jakarta.inject.Named;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.googleapi.dto.VolumeInfoDto;

@Named
public class BooksResponseDtoMapper {

    public static final String SOURCE = "google";

    public BookDetails fromDto(VolumeInfoDto dto) {
        return BookDetails.builder()
                .source(SOURCE)
                .title(dto.getTitle())
                .subtitle(dto.getSubtitle())
                .authors(dto.getAuthors())
                .build();
    }

}
