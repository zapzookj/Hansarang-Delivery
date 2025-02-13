package com.hansarangdelivery.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "p_category")
public class CategoryEntity {
    @Id
    @Column(updatable = false)
    private UUID id;

    @NotBlank
    private String name;
}


