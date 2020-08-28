package dmo.server.api.v1.dto;

import lombok.Data;

@Data
public class AnimeTitleDto {
    // TODO make it enum
    private String type;
    private String lang;
    private String text;
}
