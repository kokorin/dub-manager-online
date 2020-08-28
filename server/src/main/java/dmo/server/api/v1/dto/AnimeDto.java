package dmo.server.api.v1.dto;

import lombok.Data;

import java.util.List;

@Data
public class AnimeDto {
    private Long id;
    private List<AnimeTitleDto> titles;
    // TODO make it enum
    private String type;
}