package net.patrykdobrowolski.bookscanner.adapter.fetcher.googleapi.mapper;

import jakarta.inject.Named;
import net.patrykdobrowolski.bookscanner.adapter.fetcher.googleapi.dto.VolumeInfoDto;
import net.patrykdobrowolski.bookscanner.domain.model.BookDetails;
import net.patrykdobrowolski.bookscanner.domain.model.Year;
import org.apache.logging.log4j.util.Strings;

@Named
public class BooksResponseDtoMapper {

    public BookDetails fromDto(VolumeInfoDto dto) {
        return BookDetails.builder()
                .title(makeTitle(dto))
                .authors(dto.getAuthors())
                .publicationYear(publicationYear(dto.getPublishedDate()))
                .language(dto.getLanguage())
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

    private Year publicationYear(String publishedDate) {
        if (publishedDate != null && publishedDate.matches("^[0-9]{4}")) {
            return Year.parse(publishedDate.substring(4));
        }
        else return null;
    }

}
