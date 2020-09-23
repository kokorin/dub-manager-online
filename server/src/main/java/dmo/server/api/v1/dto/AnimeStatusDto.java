package dmo.server.api.v1.dto;

import lombok.Data;

import java.util.List;

@Data
public class AnimeStatusDto {
    private Long id;
    private List<AnimeTitleDto> titles;
    private AnimeDto.Type type;

    private Status status;

    public enum Status {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }
}
