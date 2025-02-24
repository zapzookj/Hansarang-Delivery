package com.hansarangdelivery.user.service;

import com.hansarangdelivery.global.dto.PageResponseDto;
import com.hansarangdelivery.user.dto.SignupRequestDto;
import com.hansarangdelivery.user.dto.UserResponseDto;
import com.hansarangdelivery.user.dto.UserUpdateDto;
import com.hansarangdelivery.user.model.User;
import com.hansarangdelivery.user.model.UserRole;
import com.hansarangdelivery.global.exception.DuplicateResourceException;
import com.hansarangdelivery.global.exception.ForbiddenActionException;
import com.hansarangdelivery.global.exception.ResourceNotFoundException;
import com.hansarangdelivery.user.repository.UserRepository;
import com.hansarangdelivery.user.repository.UserRepositoryQueryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
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
    public PageResponseDto<UserResponseDto> searchProfiles(Pageable pageable) {

        Sort sort = pageable.getSort().isSorted() ? pageable.getSort() : Sort.by(Sort.Direction.DESC, "createdAt", "updatedAt");
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        Page<UserResponseDto> mappedPage = userRepositoryQuery.searchUsers(sortedPageable).map(UserResponseDto::new);
        return new PageResponseDto<>(mappedPage);
    }

    @Transactional
    @CacheEvict(value = "user", key = "#userId")
    public UserResponseDto updateProfile(Long userId, UserUpdateDto requestDto) {
        User user = findUser(userId);

        String username = requestDto.getUsername();
        String email = requestDto.getEmail();

        validateInfo(username, email);

        user.updateProfile(username, email);
        return new UserResponseDto(user);
    }

    @Transactional
    @CacheEvict(value = "user", key = "#userId")
    public UserResponseDto updateRole(Long userId) {
        User user = findUser(userId);

        user.updateRole();
        return new UserResponseDto(user);
    }

    @Transactional
    @CacheEvict(value = "user", key = "#userId != null ? #userId : #currentUser.id")
    public void deleteUser(User currentUser, Long userId) {
        if (userId == null) {
            userRepository.deleteById(currentUser.getId());
        } else {
            if (currentUser.getRole() != UserRole.MANAGER) {
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

    public boolean isOwner(Long ownerId) {
        return userRepository.existsByIdAndRole(ownerId,UserRole.OWNER);
    }
}
