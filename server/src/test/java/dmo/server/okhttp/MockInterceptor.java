package dmo.server.okhttp;

import lombok.RequiredArgsConstructor;
import okhttp3.*;
import okio.Okio;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
public class MockInterceptor implements Interceptor {
    private final String mediaType;
    private final InputStream inputStream;

    @Override
    public Response intercept(Chain chain) throws IOException {
        return new Response.Builder()
                .code(200)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .message("")
                .header("Content-Type", mediaType)
                .body(ResponseBody.create(
                        MediaType.get(mediaType),
                        -1,
                        Okio.buffer(Okio.source(inputStream))
                ))
                .build();
    }
}
