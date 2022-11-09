package dmo.server.domain;

import lombok.NonNull;
import org.springframework.data.annotation.Id;

public record User(
        @NonNull
        String email
) {
}
