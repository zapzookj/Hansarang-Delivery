package com.hansarangdelivery.repository;

import com.hansarangdelivery.entity.User;
import com.hansarangdelivery.entity.UserRole;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = "addressList")
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = "addressList")
    Optional<User> findById(Long userId);

    boolean existsByIdAndRole(Long id, UserRole role);
}
