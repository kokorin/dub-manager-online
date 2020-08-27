package dmo.server.integration.anidb;

import dmo.server.okhttp.XmlUngzipInterceptor;
import okhttp3.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jaxb.JaxbConverterFactory;

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
                .baseUrl("https://anidb.net/")
                .client(clientBuilder.build())
                .addConverterFactory(JaxbConverterFactory.create())
                .build();
    }

    @Bean
    public AnidbClient anidbClient() {
        return retrofit().create(AnidbClient.class);
    }

}
