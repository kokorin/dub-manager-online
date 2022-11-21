package dmo.server.integration.anidb.client;

import dmo.server.junit.MockWebServerResolver;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith({MockitoExtension.class, MockWebServerResolver.class})
@RequiredArgsConstructor
class ApiErrorHandlingInterceptorTest {

    private final OkHttpClient http = new OkHttpClient.Builder()
            .addInterceptor(new ApiErrorHandlingInterceptor())
            .build();
    private final MockWebServer mockWebServer;


    @Test
    void interceptError() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("<error code=\"500\">banned</error>")
        );
        var response = http.newCall(new Request.Builder()
                .url(mockWebServer.url("/intercept"))
                .build()
        ).execute();

        assertEquals(500, response.code());
        assertEquals("banned", response.message());
    }

    @Test
    void interceptSuccess() throws IOException {
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("<result>success</result>")
        );
        var response = http.newCall(new Request.Builder()
                .url(mockWebServer.url("/intercept"))
                .build()
        ).execute();

        assertEquals(200, response.code());
        assertEquals("OK", response.message());
    }
}