package net.patrykdobrowolski.bookscanner.adapter.fetcher.googleapi.mapper;

import jakarta.inject.Named;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.adapter.fetcher.googleapi.dto.VolumeInfoDto;
import org.apache.logging.log4j.util.Strings;

@Named
public class BooksResponseDtoMapper {

    public BookDetails fromDto(VolumeInfoDto dto) {
        return BookDetails.builder()
                .title(makeTitle(dto))
                .authors(dto.getAuthors())
                .build();
    }

    private String makeTitle(VolumeInfoDto dto) {
        StringBuilder title = new StringBuilder();
        title.append(dto.getTitle());
        if (Strings.isNotBlank(dto.getSubtitle())) {
            title.append(" - ").append(dto.getSubtitle());
        }
        return title.toString();
    }

}
