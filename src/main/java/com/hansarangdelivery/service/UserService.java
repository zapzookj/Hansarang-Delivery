package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.PageRequestDto;
import com.hansarangdelivery.dto.SignupRequestDto;
import com.hansarangdelivery.dto.UserResponseDto;
import com.hansarangdelivery.dto.UserUpdateDto;
import com.hansarangdelivery.entity.User;
import com.hansarangdelivery.entity.UserRole;
import com.hansarangdelivery.repository.UserRepository;
import com.hansarangdelivery.repository.UserRepositoryQueryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRepositoryQueryImpl userRepositoryQuery;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();
        UserRole role = requestDto.getRole();

        validateInfo(username, email);

        // 사용자 등록
        User user = new User(username, password, email, role);
        userRepository.save(user);
    }

    public UserResponseDto getProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        return new UserResponseDto(user);
    }

    public Page<UserResponseDto> getAllProfile(PageRequestDto requestDto) {
        int page = requestDto.getPage()-1; // 0부터 시작하도록
        int size = requestDto.getSize();
        boolean isAsc = requestDto.isAsc();

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        return userRepositoryQuery.searchUsers(pageable).map(UserResponseDto::new);
    }

    @Transactional
    public void updateProfile(Long userId, UserUpdateDto requestDto) {
        User user = findUser(userId);

        String username = requestDto.getUsername();
        String email = requestDto.getEmail();

        validateInfo(username, email);

        user.updateProfile(username, email);
        userRepository.save(user);
    }

    @Transactional
    public void updateRole(Long userId) {
        User user = findUser(userId);

        user.updateRole();
        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(User currentUser, Long userId) {
        if (userId == null) {
            userRepository.deleteById(currentUser.getId());
        } else {
            if (currentUser.getRole() != UserRole.MASTER) {
                throw new IllegalArgumentException("권한이 없습니다.");
            }
            userRepository.deleteById(userId);
        }
    }

    private void validateInfo(String username, String email) {
        // 회원 중복 확인
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("중복된 Email 입니다.");
        }
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }
}
