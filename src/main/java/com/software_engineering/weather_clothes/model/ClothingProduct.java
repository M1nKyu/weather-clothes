package com.software_engineering.weather_clothes.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "clothing_product")
public class ClothingProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // auto_increment가 적용됨
    private Long id;  // primary key 및 auto_increment

    @Column(nullable = false, unique = true)  // productId는 유일해야 하므로 unique 제약을 추가
    private String productId;  // 상품 ID (data-item-id)

    private String category;   // 상품 카테고리

    private String price;      // 가격
    private String link;       // 상품 상세 링크
    private String brand;      // 브랜드
    private String imageUrl;   // 이미지 URL

    // 기본 생성자
    public ClothingProduct() {
    }

    // 모든 필드를 포함한 생성자
    public ClothingProduct(String productId, String price, String link, String brand, String imageUrl, String category) {
        this.productId = productId;
        this.price = price;
        this.link = link;
        this.brand = brand;
        this.imageUrl = imageUrl;
        this.category = category;
    }
}
