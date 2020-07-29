package by.colaba.cache;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlatformCache {
    public static final String CHAT_CACHE = "CHAT_CACHE";
    public static final String USER_CACHE = "USER_CACHE";
    public static final String MAIL_CACHE = "MAIL_CACHE";

    public static Set<String> all() {
        return Set.of(USER_CACHE, CHAT_CACHE, MAIL_CACHE);
    }
}
