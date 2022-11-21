package dmo.server.integration.anidb.client;

import dmo.server.junit.MockWebServerResolver;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okio.Buffer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockWebServerResolver.class)
@RequiredArgsConstructor
public class XmlUngzipInterceptorTest  {
    private final OkHttpClient http = new OkHttpClient.Builder()
            .addNetworkInterceptor(new XmlUngzipInterceptor())
            .build();
    private final MockWebServer mockWebServer;

    @Test
    void xmlGzIsUngzipped() throws IOException {
        var buffer = new Buffer();
        try (var gzip = new GZIPOutputStream(buffer.outputStream())) {
            gzip.write("<result>success</result>".getBytes());
        }

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/gzip")
                .setResponseCode(200)
                .setBody(buffer)
        );
        var response = http.newCall(new Request.Builder()
                .url(mockWebServer.url("/intercept.xml.gz"))
                .build()
        ).execute();

        assertEquals(200, response.code());
        assertEquals("OK", response.message());
        assertEquals("<result>success</result>", response.body().string());
    }

    @Test
    void nonGzipBodyIsNotChanged() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("plaintext")
        );
        var response = http.newCall(new Request.Builder()
                .url(mockWebServer.url("/intercept.xml"))
                .build()
        ).execute();

        assertEquals(200, response.code());
        assertEquals("OK", response.message());
        assertEquals("plaintext", response.body().string());
    }
}