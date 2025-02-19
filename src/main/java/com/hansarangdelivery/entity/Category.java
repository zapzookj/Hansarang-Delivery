package com.hansarangdelivery.entity;

import com.hansarangdelivery.dto.CategoryRequestDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "p_category")
public class Category extends TimeStamped{
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


