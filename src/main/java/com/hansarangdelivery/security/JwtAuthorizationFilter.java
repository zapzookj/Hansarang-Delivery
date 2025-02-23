package com.hansarangdelivery.security;

import com.hansarangdelivery.user.dto.UserCacheDto;
import com.hansarangdelivery.user.model.User;
import com.hansarangdelivery.user.model.UserRole;
import com.hansarangdelivery.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, CustomUserDetailsService customUserDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtUtil.getJwtFromHeader(req);

        if (StringUtils.hasText(tokenValue)) {

            if (!jwtUtil.validateToken(tokenValue)) {
                log.error("Token Error");
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);
            UserRole role = UserRole.valueOf((String) info.get(JwtUtil.AUTHORIZATION_KEY));
            Long userId = info.get("userId", Long.class);

            try {
                setAuthentication(info.getSubject(), role, userId);
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username, UserRole userRole, Long userId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username, userRole, userId);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username, UserRole userRole, Long userId) {
        UserDetails userDetails;
        if (userRole.equals(UserRole.MANAGER) || userRole.equals(UserRole.MASTER)) {
            userDetails = userDetailsService.loadUserByUsername(username);
        } else {
            UserCacheDto dto = customUserDetailsService.loadUserByIdForRegular(userId);
            User user = new User(
                dto.getUserId(), dto.getUsername(), dto.getPassword(), dto.getEmail(), dto.getRole(), dto.getAddressList());
            userDetails = new UserDetailsImpl(user);
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}