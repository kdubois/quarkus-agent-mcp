package io.quarkus.agent.mcp;

import java.net.http.HttpClient;
import java.time.Duration;

/**
 * Provides a lazily-initialized, shared HttpClient instance for native image compatibility.
 * HttpClient must not be created at build time to avoid being stored in the native image heap.
 */
public final class HttpClientProvider {

    private static volatile HttpClient httpClient;

    private HttpClientProvider() {
    }

    /**
     * Returns a shared HttpClient instance, creating it on first access.
     * Thread-safe using double-checked locking.
     */
    public static HttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (HttpClientProvider.class) {
                if (httpClient == null) {
                    httpClient = HttpClient.newBuilder()
                            .connectTimeout(Duration.ofSeconds(10))
                            .followRedirects(HttpClient.Redirect.NORMAL)
                            .build();
                }
            }
        }
        return httpClient;
    }
}

