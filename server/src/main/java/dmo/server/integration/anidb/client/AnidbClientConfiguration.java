package dmo.server.integration.anidb.client;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jaxb.JaxbConverterFactory;

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
    public AnidbClient anidbClient(
            @Value("${integration.anidb.api.base_url}") String apiBaseUrl,
            @Qualifier("anidb") OkHttpClient okHttpClient
    ) {
        return new Retrofit.Builder()
                .baseUrl(apiBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(JaxbConverterFactory.create())
                .build()
                .create(AnidbClient.class);
    }

    @Bean
    @Qualifier("anidb")
    public AnidbTitlesClient anidbTitlesClient(
            @Value("${integration.anidb.titles.base_url}") String titlesBaseUrl,
            @Qualifier("anidb") OkHttpClient okHttpClient
    ) {
        return new Retrofit.Builder()
                .baseUrl(titlesBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(JaxbConverterFactory.create())
                .build()
                .create(AnidbTitlesClient.class);
    }

}
