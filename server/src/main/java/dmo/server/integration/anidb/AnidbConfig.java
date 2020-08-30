package dmo.server.integration.anidb;

import dmo.server.util.okhttp.XmlUngzipInterceptor;
import dmo.server.util.retrofit.JaxbConverterFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;

import java.util.Collections;
import java.util.List;

@Configuration
public class AnidbConfig {
    List<Interceptor> interceptors() {
        return Collections.singletonList(
                new XmlUngzipInterceptor()
        );
    }

    Retrofit retrofit() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        interceptors().forEach(clientBuilder::addInterceptor);

        return new Retrofit.Builder()
                //because anidb uses different hosts for anime list and anime
                .baseUrl("http://localhost/why/")
                .client(clientBuilder.build())
                .addConverterFactory(JaxbConverterFactory.create(
                        AnidbAnime.class,
                        AnidbAnimeLightList.class,
                        AnidbError.class
                ))
                .build();
    }

    @Bean
    public AnidbClient anidbClient() {
        return retrofit().create(AnidbClient.class);
    }

}
