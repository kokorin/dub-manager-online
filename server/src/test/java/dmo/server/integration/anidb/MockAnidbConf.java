package dmo.server.integration.anidb;

import dmo.server.util.okhttp.XmlUngzipInterceptor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockAnidbConf {
    @Bean
    public MockResponseInterceptor mockResponseInterceptor() {
        return new MockResponseInterceptor();
    }

    @Bean
    @Qualifier("anidb")
    public OkHttpClient okHttpClient(MockResponseInterceptor mockResponseInterceptor) {

        return new OkHttpClient.Builder()
                .addInterceptor(new XmlUngzipInterceptor())
                .addInterceptor(mockResponseInterceptor)
                .build();
    }
}
