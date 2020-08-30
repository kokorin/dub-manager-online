package dmo.server.integration.anidb;

import dmo.server.util.okhttp.XmlUngzipInterceptor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jaxb.JaxbConverterFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
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

        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(AnidbAnimeLight.class, AnidbAnime.class, AnidbError.class);
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to create JAXB context");
        }

        return new Retrofit.Builder()
                //because anidb uses different hosts for anime list and anime
                .baseUrl("http://localhost/why/")
                .client(clientBuilder.build())
                .addConverterFactory(JaxbConverterFactory.create(jaxbContext))
                .build();
    }

    @Bean
    public AnidbClient anidbClient() {
        return retrofit().create(AnidbClient.class);
    }

}
