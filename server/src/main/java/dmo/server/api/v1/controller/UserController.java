package dmo.server.api.v1.controller;

import dmo.server.api.v1.dto.UserDto;
import dmo.server.api.v1.mapper.UserMapper;
import dmo.server.security.JwtUser;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users/current")
@AllArgsConstructor
public class UserController {
    private final UserMapper userMapper;

    @GetMapping
    @ApiOperation(value = "Get current user information", nickname = "getCurrentUser")
    public UserDto getUser(@AuthenticationPrincipal JwtUser user) {
        return userMapper.toDto( user);
    }
}
