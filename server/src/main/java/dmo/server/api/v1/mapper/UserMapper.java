package dmo.server.api.v1.mapper;

import dmo.server.api.v1.dto.UserDto;
import dmo.server.security.JwtUser;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface UserMapper {
    UserDto toDto(JwtUser user);
}
