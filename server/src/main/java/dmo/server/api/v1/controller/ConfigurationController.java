package dmo.server.api.v1.controller;

import dmo.server.api.v1.dto.ConfigurationDto;
import dmo.server.prop.GoogleOAuthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/conf")
@RequiredArgsConstructor
public class ConfigurationController {
    private final GoogleOAuthProperties googleOAuthProperties;

    @GetMapping
    public ConfigurationDto getConfiguration() {
        return new ConfigurationDto(googleOAuthProperties.clientId);
    }
}
