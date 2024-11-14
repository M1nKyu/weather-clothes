package com.software_engineering.weather_clothes.repository;

import com.software_engineering.weather_clothes.model.ClothingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClothingProductRepository extends JpaRepository<ClothingProduct, Long> {
}
