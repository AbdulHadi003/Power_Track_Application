package com.powertrack.security;

import com.powertrack.entity.User;
import com.powertrack.enums.UserStatus;
import com.powertrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Loading user by email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found: {}", email);
                    return new UsernameNotFoundException("User not found: " + email);
                });

        // Check if user is active
        if (user.getStatus() != UserStatus.ACTIVE) {
            log.warn("User status is not ACTIVE: {} - Status: {}", email, user.getStatus());
            throw new UsernameNotFoundException("User account is not active: " + user.getStatus());
        }

        log.info("User loaded successfully: {} with role: {}", email, user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                getAuthorities(user)
        );
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        String roleWithPrefix = "ROLE_" + user.getRole().name();
        log.debug("Granting authority to user {}: {}", user.getEmail(), roleWithPrefix);

        return Collections.singletonList(
                new SimpleGrantedAuthority(roleWithPrefix)
        );
    }
}