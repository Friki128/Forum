package net.esliceu.Rest_Api_Forum.Utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SecretClass {

    @Value("${app.secret}")
    private String secret;

    // Static field to hold the secret for static access
    private static String staticSecret;

    @PostConstruct
    public void init() {
        // This method will be called after Spring bean is initialized
        staticSecret = secret;
    }

    public String getSecret() {
        return secret;
    }

    public static String getStaticSecret() {
        if (staticSecret == null) {
            throw new IllegalStateException("Static secret is not initialized.");
        }
        return staticSecret;
    }
}
