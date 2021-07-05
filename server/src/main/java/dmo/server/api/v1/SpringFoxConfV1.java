package dmo.server.api.v1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Configuration
@EnableSwagger2
public class SpringFoxConfV1 {
    @Bean
    // TODO pretty print
    public Docket docketApiV1() {
        return new Docket(DocumentationType.SWAGGER_2)
                .protocols(new HashSet<>(Arrays.asList("http", "https")))
                .groupName("v1")
                .securitySchemes(List.of(
                        /*new OAuth(
                                "jwt",
                                List.of(new AuthorizationScope("default", "Default auth scope")),
                                // TODO generate login URL based on incoming request protocol and port
                                List.of(new ResourceOwnerPasswordCredentialsGrant("/login"))
                        ),*/
                        new ApiKey("Bearer", "Authorization", "header")
                ))
                .ignoredParameterTypes(AuthenticationPrincipal.class)
                .select().paths(s -> s.startsWith("/api/v1")).build()
                .forCodeGeneration(true);
    }
}
