package com.software_engineering.weather_clothes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Region {
    @Id
    private Long id;
    private String region; // 시
    private String district; // 구
    private String town; // 읍면동
    private String nx; // 위도
    private String ny; // 경도
}
