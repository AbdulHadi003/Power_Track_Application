package com.powertrack.config;

import com.powertrack.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ⭐⭐⭐ CRITICAL: SecurityContextRepository for persisting auth to session
    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring SecurityFilterChain for session-based authentication");

        http
                // CORS Configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // CSRF Disabled
                .csrf(csrf -> csrf.disable())

                // ⭐⭐⭐ MOST IMPORTANT: Store SecurityContext in HttpSession
                .securityContext(securityContext -> securityContext
                        .securityContextRepository(securityContextRepository())
                )

                // SESSION MANAGEMENT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .sessionFixation().migrateSession()
                        .maximumSessions(1)
                        .expiredUrl("/auth/login")
                )

                // AUTHORIZATION
                .authorizeHttpRequests(auth -> auth
                        // PUBLIC ENDPOINTS
                        .requestMatchers("/auth/register", "/auth/login").permitAll()
                        .requestMatchers("/auth/logout").permitAll()
                        .requestMatchers("/error").permitAll()

                        // DEBUG ENDPOINTS (PUBLIC)
                        .requestMatchers("/debug/**").permitAll()

                        // PUBLIC DATA
                        .requestMatchers(HttpMethod.GET, "/api/feeders/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tariffs/active").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/tariffs/current").permitAll()

                        // AUTHENTICATED USERS
                        .requestMatchers(HttpMethod.GET, "/api/users/me").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/me").authenticated()
                        .requestMatchers("/auth/change-password").authenticated()
                        .requestMatchers("/auth/profile").authenticated()

                        // CUSTOMER ENDPOINTS
                        .requestMatchers("/api/bills/my-bills/**").authenticated()
                        .requestMatchers("/api/meters/my-meters/**").authenticated()
                        .requestMatchers("/api/load-shedding/my-schedule/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/meter-requests").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/meter-requests/my-requests").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/complaints").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/complaints/my-complaints").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/complaints/token/**").permitAll()
                        .requestMatchers("/api/chat/**").authenticated()
                        .requestMatchers("/api/notifications/my-notifications/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/payments/pay").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/payments/my-payments").authenticated()
                        .requestMatchers("/api/installments/my-plans").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/installments/request").authenticated()

                        // ADMIN ONLY
                        .requestMatchers(HttpMethod.POST, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers("/api/bills/generate").hasRole("ADMIN")
                        .requestMatchers("/api/bills/**").hasRole("ADMIN")
                        .requestMatchers("/api/tariffs/**").hasRole("ADMIN")
                        .requestMatchers("/api/load-shedding/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/notifications").hasRole("ADMIN")

                        // ANY OTHER REQUEST MUST BE AUTHENTICATED
                        .anyRequest().authenticated()
                )

                // LOGIN/LOGOUT
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("POWERTRACK_SESSION")
                        // ⭐ CRITICAL: Use logoutSuccessHandler instead of logoutSuccessUrl
                        .logoutSuccessHandler((request, response, authentication) -> {
                            log.info("User logged out successfully");
                            response.setStatus(200);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"status\": \"success\", \"message\": \"Logged out successfully\"}");
                        })
                        .permitAll()
                )

                // EXCEPTION HANDLING
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.warn("Authentication failed: {}", authException.getMessage());
                            response.setStatus(401);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"status\": \"error\", \"message\": \"Unauthorized - Please login\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.warn("Access denied: {}", accessDeniedException.getMessage());
                            response.setStatus(403);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"status\": \"error\", \"message\": \"Forbidden - Access Denied\"}");
                        })
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:4200",
                "http://localhost:5173",
                "http://localhost:8080",
                "http://127.0.0.1:3000"
        ));

        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"
        ));

        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList(
                "Content-Type",
                "Authorization",
                "X-Requested-With",
                "Set-Cookie"
        ));

        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}