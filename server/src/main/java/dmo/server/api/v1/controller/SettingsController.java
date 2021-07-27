package dmo.server.api.v1.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("api/v1/conf")
@RequiredArgsConstructor
public class SettingsController {
    private final OAuth2ClientProperties oAuth2ClientProperties;

    @GetMapping("oauth/clients")
    @ApiOperation(value = "Get list of registered OAuth2 clients", nickname = "getOAuthClients")
    public Set<String> getRegistered() {
        return oAuth2ClientProperties.getRegistration().keySet();
    }
}
