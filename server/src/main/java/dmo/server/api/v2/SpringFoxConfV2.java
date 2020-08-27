package dmo.server.api.v2;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.HashSet;

@Configuration
@EnableSwagger2
public class SpringFoxConfV2 {
    @Bean
    // TODO pretty print
    public Docket docketApiV2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .protocols(new HashSet<>(Arrays.asList("http", "https")))
                .groupName("v2")
                .select().paths(s -> s.startsWith("/api/v2")).build()
                .forCodeGeneration(true);
    }
}
