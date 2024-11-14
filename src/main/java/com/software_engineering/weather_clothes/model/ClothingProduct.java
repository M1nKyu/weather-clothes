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
    private String link;       // 상품 상세 링크
    private String imageUrl;   // 이미지 URL
    private String likes;     // 상품 좋아요 수

    @Column(name = "last_updated", nullable = false)
    private String lastUpdated;  // 마지막 갱신 시간 (YYMMDDHHmm)

}
