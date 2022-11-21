package dmo.server.junit;

import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.api.extension.support.TypeBasedParameterResolver;

public class MockWebServerResolver extends TypeBasedParameterResolver<MockWebServer> implements AfterEachCallback {
    private static final String KEY = "mock-web-server";
    private static final AutoCloseable NOOP = new AutoCloseable() {
        @Override
        public void close() throws Exception {

        }
    };

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        context.getStore(ExtensionContext.Namespace.GLOBAL)
                .getOrDefault(KEY, AutoCloseable.class, NOOP)
                .close();
    }

    @Override
    public MockWebServer resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        var mock = new MockWebServer();
        extensionContext.getStore(ExtensionContext.Namespace.GLOBAL)
                .put(KEY, mock);
        return mock;
    }
}
