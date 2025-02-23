package com.hansarangdelivery.category.model;

import com.hansarangdelivery.category.dto.CategoryRequestDto;
import com.hansarangdelivery.global.model.TimeStamped;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;
import org.hibernate.annotations.Where;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "p_category")
@Where(clause = "deleted_at IS NULL")
public class Category extends TimeStamped {
    @Id
    @UuidGenerator
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;


    @NotBlank
    private String name;

    public Category(String name) {
        this.name = name;
    }

    public void update(CategoryRequestDto categoryRequestDto){
        name = categoryRequestDto.getName();
    }
}


