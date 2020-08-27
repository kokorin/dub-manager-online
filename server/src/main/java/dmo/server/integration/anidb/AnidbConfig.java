package dmo.server.integration.anidb;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

@Configuration
public class AnidbConfig {

    @Bean
    public AnidbClient anidbClient() {
        return new Retrofit.Builder()
                .baseUrl("https://anidb.net/")
                .build()
                .create(AnidbClient.class);
    }
}
