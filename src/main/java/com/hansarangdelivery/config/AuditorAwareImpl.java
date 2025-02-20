package com.hansarangdelivery.config;

import com.hansarangdelivery.exception.ForbiddenActionException;
import com.hansarangdelivery.security.UserDetailsImpl;
import lombok.NonNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    @NonNull
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsImpl userDetails) {
            if (userDetails.getUser() != null && userDetails.getUser().getId() != null) {
                return Optional.of(userDetails.getUser().getId().toString()); // userId를 String 으로 변환하여 반환
            }
        }

        return Optional.of(authentication.getName());
    }
}
