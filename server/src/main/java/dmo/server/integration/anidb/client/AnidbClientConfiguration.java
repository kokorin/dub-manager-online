package dmo.server.integration.anidb.client;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

@Configuration
public class AnidbClientConfiguration {
    @Bean
    @Qualifier("anidb")
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new XmlUngzipInterceptor())
                .addInterceptor(new ApiErrorHandlingInterceptor())
                .build();
    }

    @Bean
    @Qualifier("anidb")
    public Retrofit retrofit(@Qualifier("anidb") OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                //because anidb uses different hosts for anime list and anime
                .baseUrl("http://localhost/why/")
                .client(okHttpClient)
                .build();
    }

    @Bean
    public AnidbClient anidbClient(@Qualifier("anidb") Retrofit retrofit) {
        return retrofit.create(AnidbClient.class);
    }

}
