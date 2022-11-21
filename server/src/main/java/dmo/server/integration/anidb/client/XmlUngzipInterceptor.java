package dmo.server.integration.anidb.client;

import okhttp3.*;
import okio.GzipSource;
import okio.Okio;
import okio.Source;

import java.io.IOException;

/**
 * OkHttp3 Interceptor to ungzip xml.gz file return by server on the fly.
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

        return response.newBuilder()
                .header("content-type", "application/xml")
                .header("content-encoding", "gzip")
                .build();
    }
}
