package dmo.server.integration.anidb;

import com.google.common.collect.Iterables;
import okhttp3.*;
import okio.Okio;

import java.io.IOException;

public class MockResponseInterceptor implements Interceptor {
    private String useResponseFile = null;

    public void useResponseFileOnce(String responseFile) {
        this.useResponseFile = responseFile;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        var request = chain.request();
        var responseFile = responseFile(useResponseFile, request.url());
        useResponseFile = null;
        var responseType = responseType(responseFile);
        var source = Okio.source(getClass().getResourceAsStream(responseFile));

        return new Response.Builder()
                .code(200)
                .request(request)
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

    private static String responseFile(String responseFile, HttpUrl url) {
        if (responseFile != null) {
            return responseFile;
        }

        var path = url.encodedPath();

        if (path.startsWith("/httpapi")) {
            var aid = url.queryParameter("aid");
            return "anime-" + aid + ".xml";
        }

        if (path.endsWith("/anime-titles.xml.gz")) {
            return "anime-titles.xml.gz";
        }

        throw new RuntimeException("Unknown path: " + path);
    }

    private static String responseType(String file) {
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
