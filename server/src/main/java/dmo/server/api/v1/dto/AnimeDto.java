package dmo.server.api.v1.dto;

import lombok.Data;

import java.util.List;

@Data
public class AnimeDto {
    private Long id;
    private List<AnimeTitleDto> titles;
    private Type type;

    public enum Type {
        MOVIE,
        OVA,
        TV_SERIES,
        TV_SPECIAL,
        WEB,
        MUSIC_VIDEO,
        OTHER,
        UNKNOWN;
    }
}
