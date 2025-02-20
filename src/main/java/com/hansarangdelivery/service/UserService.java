package com.hansarangdelivery.service;

import com.hansarangdelivery.dto.SignupRequestDto;
import com.hansarangdelivery.dto.UserResponseDto;
import com.hansarangdelivery.dto.UserUpdateDto;
import com.hansarangdelivery.entity.User;
import com.hansarangdelivery.entity.UserRole;
import com.hansarangdelivery.exception.DuplicateResourceException;
import com.hansarangdelivery.exception.ForbiddenActionException;
import com.hansarangdelivery.exception.ResourceNotFoundException;
import com.hansarangdelivery.repository.UserRepository;
import com.hansarangdelivery.repository.UserRepositoryQueryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${admin.code}")
    private String adminCode;

    @Transactional
    public UserResponseDto signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String email = requestDto.getEmail();
        UserRole role = requestDto.getRole(); //사용자가 입력 x -> 기본값 customer로 지정

        validateInfo(username, email);
        
        if (role == null) {
            role = UserRole.CUSTOMER;
        } else if (role.equals(UserRole.MANAGER)) {
            throw new ForbiddenActionException("선택 불가능한 Role 입니다.");
        } else if (role.equals(UserRole.MASTER)) {
            if (!adminCode.equals(requestDto.getAdminCode())) {
                throw new ForbiddenActionException("어드민 코드가 틀려 등록이 불가능 합니다.");
            }
        }

        // 사용자 등록
        User user = new User(username, password, email, role);
        userRepository.save(user);
        return new UserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto readProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
            () -> new ResourceNotFoundException("존재하지 않는 사용자입니다."));
        return new UserResponseDto(user);
    }

    @Transactional(readOnly = true)
    public Page<UserResponseDto> searchProfiles(Pageable pageable) {

        Sort sort = pageable.getSort().isSorted() ? pageable.getSort() : Sort.by(Sort.Direction.DESC, "createdAt", "updatedAt");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return userRepositoryQuery.searchUsers(sortedPageable).map(UserResponseDto::new);
    }

    @Transactional
    public UserResponseDto updateProfile(Long userId, UserUpdateDto requestDto) {
        User user = findUser(userId);

        String username = requestDto.getUsername();
        String email = requestDto.getEmail();

        validateInfo(username, email);

        user.updateProfile(username, email);
        return new UserResponseDto(user);
    }

    @Transactional
    public UserResponseDto updateRole(Long userId) {
        User user = findUser(userId);

        user.updateRole();
        return new UserResponseDto(user);
    }

    @Transactional
    public void deleteUser(User currentUser, Long userId) {
        if (userId == null) {
            userRepository.deleteById(currentUser.getId());
        } else {
            if (currentUser.getRole() != UserRole.MASTER) {
                throw new ForbiddenActionException("권한이 없습니다.");
            }
            userRepository.deleteById(userId);
        }
    }

    private void validateInfo(String username, String email) {
        // 회원 중복 확인
        if (userRepository.existsByUsername(username)) {
            throw new DuplicateResourceException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("중복된 Email 입니다.");
        }
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 사용자입니다."));
    }
}
