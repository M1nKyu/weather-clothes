package com.software_engineering.weather_clothes.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "region_list")
@Data
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String region; // 시
    private String district; // 구
    private String town; // 읍면동
    private Integer nx; // 위도
    private Integer ny; // 경도
}
