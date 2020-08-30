package dmo.server.api.v1.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class EpisodeDto {
    private Long id;

    private Long number;

    private List<EpisodeTitleDto> titles;

    private Type type;

    private Long length;

    private LocalDate airDate;

    public enum Type {
        REGULAR,
        SPECIAL,
        CREDIT,
        TRAILER,
        PARODY,
        OTHER
    }
}
