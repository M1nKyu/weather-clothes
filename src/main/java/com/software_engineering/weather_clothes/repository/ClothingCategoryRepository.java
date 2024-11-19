package com.software_engineering.weather_clothes.repository;

import com.software_engineering.weather_clothes.model.ClothingCategory;
import com.software_engineering.weather_clothes.model.ClothingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClothingCategoryRepository extends JpaRepository <ClothingCategory, Long> {
    boolean existsByCategoryId(String categoryId);

    @Query("SELECT c FROM ClothingCategory c WHERE c.categoryId = :categoryId")
    Optional<ClothingCategory> findByCategoryId(@Param("categoryId") String categoryId);

}
