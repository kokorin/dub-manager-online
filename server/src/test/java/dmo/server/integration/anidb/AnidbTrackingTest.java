package dmo.server.integration.anidb;

import dmo.server.okhttp.MockInterceptor;
import okhttp3.Interceptor;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AnidbTrackingTest {

    @Test
    void testUpdate() throws Exception {
        try (InputStream input = AnidbTrackingTest.class.getResourceAsStream("anime-titles.xml.gz")) {
            AnidbConfig anidbConfig = new AnidbConfig() {
                @Override
                List<Interceptor> interceptors() {
                    List<Interceptor> result = new ArrayList<>(super.interceptors());
                    result.add(new MockInterceptor("application/gzip", input));
                    return result;
                }
            };

            AnidbClient anidbClient = anidbConfig.anidbClient();
            AnidbTracking tracking = new AnidbTracking(anidbClient);
            tracking.update();
        }
    }

}