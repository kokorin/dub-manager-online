package dmo.server.api.v1.mapper;

import dmo.server.api.v1.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {
    UserDto toDto(OidcUser user);
}
