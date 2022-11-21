package dmo.server.integration.anidb.client;

import dmo.server.integration.anidb.dto.AnidbError;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.JAXBContext;
import java.io.IOException;

/**
 * Sometime HTTP API reports errors not as HTTP response code, but returns error describing object as response body.
 */
@Slf4j
public class ApiErrorHandlingInterceptor implements Interceptor {
    private static final long MAX_ERROR_OBJECT_RESPONSE_SIZE = 100_000;

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        var response = chain.proceed(chain.request());
        if (response.isSuccessful()) {
            AnidbError error = null;
            try {
                var context = JAXBContext.newInstance(AnidbError.class);
                var unmarshaller = context.createUnmarshaller();
                error = (AnidbError) unmarshaller.unmarshal(response.peekBody(MAX_ERROR_OBJECT_RESPONSE_SIZE).byteStream());
            } catch (Exception e) {
                log.debug("Response body is not an error (failed to parse API error: {}", e.getMessage());
            }
            if (error != null) {
                log.debug("Response body contains error object: {}", error);
                response = new Response.Builder(response)
                        .code(error.code)
                        .message(error.message)
                        .build();
            }
        }
        return response;
    }
}
