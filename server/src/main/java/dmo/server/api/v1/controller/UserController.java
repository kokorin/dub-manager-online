package dmo.server.api.v1.controller;

import dmo.server.api.v1.dto.UserDto;
import dmo.server.api.v1.mapper.UserMapper;
import dmo.server.security.JwtUser;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users/current")
@AllArgsConstructor
public class UserController {
    private final UserMapper userMapper;

    @GetMapping
    @Operation(summary = "Get current user information", operationId = "getCurrentUser")
    public UserDto getUser(@AuthenticationPrincipal JwtUser user) {
        return userMapper.toDto( user);
    }
}
