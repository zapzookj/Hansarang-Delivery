package com.hansarangdelivery.security;

import com.hansarangdelivery.dto.UserCacheDto;
import com.hansarangdelivery.entity.User;
import com.hansarangdelivery.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService {

    private final UserRepository userRepository;

    @Cacheable(value = "user", key = "#userId")
    public UserCacheDto loadUserByIdForRegular(Long userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException("Not Found " + userId));
        return new UserCacheDto(user);
    }
}
