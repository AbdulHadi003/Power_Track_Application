package com.powertrack.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
public class SessionConfig {

    /**
     * Configure how session cookies are serialized
     * This ensures cookies are properly sent and stored
     */
    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();

        serializer.setCookieName("POWERTRACK_SESSION");
        serializer.setCookiePath("/");  // Available for all paths
        serializer.setUseHttpOnlyCookie(true);  // JavaScript can't access
        serializer.setUseBase64Encoding(false);
        serializer.setSameSite("Lax");  // Allow cross-site for same-origin

        // Important: Allow session to be set from any origin
        serializer.setDomainNamePattern("^.+?\\.(\\w+(?:\\.[a-z]+)?)$");

        return serializer;
    }

    /**
     * Enable HTTP session events for proper session lifecycle management
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

}