package com.software_engineering.weather_clothes.repository;

import com.software_engineering.weather_clothes.model.ClothingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClothingProductRepository extends JpaRepository<ClothingProduct, Long> {
    Optional<ClothingProduct> findByProductId(String productId);

    @Query("SELECT c FROM ClothingProduct c WHERE c.categoryId = :categoryId")
    List<ClothingProduct> findByCategoryId(@Param("categoryId") String categoryId);




}
