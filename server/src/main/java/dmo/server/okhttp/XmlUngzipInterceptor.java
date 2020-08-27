package dmo.server.okhttp;

import okhttp3.*;
import okio.GzipSource;
import okio.Okio;
import okio.Source;

import java.io.IOException;

/**
 * OkHttp3 Interceptor which simply decompresses received XML files.
 */
public class XmlUngzipInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);

        if (!request.url().encodedPath().endsWith("xml.gz")) {
            return response;
        }

        if (!"application/gzip".equals(response.header("content-type"))) {
            return response;
        }

        if (response.body() == null) {
            return response;
        }

        Source source = new GzipSource(response.body().source());
        ResponseBody responseBody = ResponseBody.create(
                MediaType.get("application/xml"),
                -1L,
                Okio.buffer(source)
        );

        return response.newBuilder()
                .body(responseBody)
                .build();
    }
}
