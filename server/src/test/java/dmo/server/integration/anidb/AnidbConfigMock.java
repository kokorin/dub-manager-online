package dmo.server.integration.anidb;

import dmo.server.okhttp.MockInterceptor;
import lombok.RequiredArgsConstructor;
import okhttp3.Interceptor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class AnidbConfigMock extends AnidbConfig {
    private final String mediaType;
    private final InputStream input;

    @Override
    List<Interceptor> interceptors() {
        List<Interceptor> result = new ArrayList<>(super.interceptors());
        result.add(new MockInterceptor(mediaType, input));
        return result;
    }
}
