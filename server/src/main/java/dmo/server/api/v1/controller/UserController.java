package dmo.server.api.v1.controller;

import dmo.server.api.v1.dto.UserDto;
import dmo.server.security.DubUserDetails;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users/current")
public class UserController {
    @GetMapping
    @ApiOperation(value = "Get current user information", nickname = "getCurrentUser")
    public UserDto getUser(@AuthenticationPrincipal DubUserDetails userDetails) {
        return new UserDto(
                userDetails.getId(),
                userDetails.getEmail()
        );
    }
}
