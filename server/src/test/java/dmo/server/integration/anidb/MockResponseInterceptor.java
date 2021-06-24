package dmo.server.integration.anidb;

import okhttp3.*;
import okio.Okio;
import okio.Source;

import java.io.IOException;

public class MockResponseInterceptor implements Interceptor {
    private String responseFile = "";

    public void useResponseFileOnce(String responseFile) {
        this.responseFile = responseFile;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        var responseType = responseType(responseFile);
        var source = Okio.source(getClass().getResourceAsStream(responseFile));
        responseFile = null;

        return new Response.Builder()
                .code(200)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .message("")
                .header("Content-Type", responseType)
                .body(ResponseBody.create(
                        MediaType.get(responseType),
                        -1,
                        Okio.buffer(source)
                ))
                .build();
    }

    private String responseType(String file) {
        var extension = file.split("\\.", 2)[1];
        switch (extension) {
            case "xml.gz":
                return "application/gzip";
            case "xml":
                return "application/xml";
            case "html":
                return "application/html";
        }

        throw new RuntimeException("Unknown extension: " + extension);
    }
}
