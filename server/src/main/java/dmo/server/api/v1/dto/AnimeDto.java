package dmo.server.api.v1.dto;

import lombok.Data;

@Data
public class AnimeDto {
    private Long id;
    private String title;
    // TODO make it enum
    private String type;
}