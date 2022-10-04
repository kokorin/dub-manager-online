package dmo.server.api.v1.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PageDto<T> {
    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private int number;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private int size;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private int numberOfElements;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private int totalPages;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private long totalElements;

    @Schema(required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private List<T> content;
}
