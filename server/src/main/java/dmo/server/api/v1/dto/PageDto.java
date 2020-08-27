package dmo.server.api.v1.dto;

import java.util.List;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class PageDto<T> {
    private final int number;
    private final int size;
    private final int numberOfElements;
    private final int totalPages;
    private final long totalElements;
    private final List<T> content;
}
