package com.hansarangdelivery.category.repository;

import com.hansarangdelivery.category.model.Category;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByNameAndDeletedAtIsNull(String name);
    Optional<Category> findByIdAndDeletedAtIsNull(UUID id);
    Optional<Category> findByName(String name);

    Page<Category> findByDeletedAtIsNull(Pageable pageable);

    boolean existsByNameIgnoreCase(String name);
}
