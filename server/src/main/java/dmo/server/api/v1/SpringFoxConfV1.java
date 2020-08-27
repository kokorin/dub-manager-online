package dmo.server.api.v1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.core.Authentication;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

@Configuration
@EnableSwagger2
public class SpringFoxConfV1 {
    @Bean
    // TODO pretty print
    public Docket docketApiV1() {
        return new Docket(DocumentationType.SWAGGER_2)
                .protocols(new HashSet<>(Arrays.asList("http", "https")))
                .groupName("v1")
                .securitySchemes(Collections.singletonList(
                        new OAuth(
                                "jwt",
                                Collections.singletonList(new AuthorizationScope("default", "Default auth scope")),
                                // TODO generate login URL based on incoming request protocol and port
                                Collections.singletonList(new ResourceOwnerPasswordCredentialsGrant("/login"))
                        )
                        //new ApiKey("Bearer", "Authorization", "header")
                ))
                //.ignoredParameterTypes(Authentication.class)
                .select().paths(s -> s.startsWith("/api/v1")).build()
                .forCodeGeneration(true);
    }
}
