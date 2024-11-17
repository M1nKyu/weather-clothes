package com.software_engineering.weather_clothes.repository;

import com.software_engineering.weather_clothes.model.ClothingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothingCategoryRepository extends JpaRepository <ClothingCategory, Long> {
    boolean existsByCategoryId(String categoryId);
}
