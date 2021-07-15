package dmo.server.okhttp;

import lombok.NonNull;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InMemoryCookieJar implements CookieJar {
    private final List<Cookie> cookies = new ArrayList<>();

    @NonNull
    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl httpUrl) {
        return cookies;
    }

    @Override
    public void saveFromResponse(@NonNull HttpUrl httpUrl, @NonNull List<Cookie> list) {
        cookies.addAll(list);
    }
}
