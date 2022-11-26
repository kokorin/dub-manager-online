package dmo.server.junit;

import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver;

import java.io.IOException;

public class MockWebServerExtension extends TypeBasedParameterResolver<MockWebServer> implements AfterAllCallback {
    private static final String KEY = "mock-web-server";
    private static final AutoCloseable NOOP = new AutoCloseable() {
        @Override
        public void close() throws Exception {

        }
    };

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        context.getStore(ExtensionContext.Namespace.GLOBAL)
                .getOrDefault(KEY, AutoCloseable.class, NOOP)
                .close();
    }

    @Override
    public MockWebServer resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(ExtensionContext.Namespace.GLOBAL)
                .getOrComputeIfAbsent(
                        KEY,
                        x -> {
                            var result = new MockWebServer();
                            try {
                                result.start();
                            } catch (IOException e) {
                                throw new ParameterResolutionException("Failed to start MockWebServer", e);
                            }
                            return result;
                        },
                        MockWebServer.class
                );
    }
}
