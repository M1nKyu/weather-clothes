package com.software_engineering.weather_clothes.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "clothing_category")
public class ClothingCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String categoryId;

    @Column(nullable = false)
    private String categoryName;

}
