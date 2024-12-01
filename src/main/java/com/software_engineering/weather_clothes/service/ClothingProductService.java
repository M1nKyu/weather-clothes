package com.software_engineering.weather_clothes.service;

import com.software_engineering.weather_clothes.model.ClothingProduct;
import com.software_engineering.weather_clothes.repository.ClothingProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class ClothingProductService {

    @Async
    public CompletableFuture<Void> updateLikesForAllProductsAsync() {
        updateLikesForAllProducts();
        return CompletableFuture.completedFuture(null);
    }

    @Autowired
    private ClothingProductRepository clothingProductRepository;

    // 좋아요 수 변환 메서드
    public String convertLikes(String likes) {
        if (likes == null || likes.isEmpty()) return likes;
        // 예시: 3.9천 -> 3900, 1.2만 -> 12000 등의 변환 로직
        if (likes.contains("천")) {
            double value = Double.parseDouble(likes.replace("천", "")) * 1000;
            return String.valueOf((int)value);
        } else if (likes.contains("만")) {
            double value = Double.parseDouble(likes.replace("만", "")) * 10000;
            return String.valueOf((int)value);
        }
        return likes;  // 변환 불필요한 경우 그대로 반환
    }


    // DB의 모든 데이터에 대해 좋아요 변환 적용
    public void updateLikesForAllProducts() {
        List<ClothingProduct> products = clothingProductRepository.findAll();
        for (ClothingProduct product : products) {
            product.setLikes(convertLikes(product.getLikes()));
        }
        clothingProductRepository.saveAll(products);  // 한 번에 저장
    }
}
