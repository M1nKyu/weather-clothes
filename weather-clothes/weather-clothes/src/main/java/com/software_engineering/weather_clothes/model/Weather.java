package com.software_engineering.weather_clothes.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "weather_data")
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String baseDate;   // 기준 날짜
    private String baseTime;   // 기준 시간

    private String fcstDate;   // 예보 날짜
    private String fcstTime;   // 예보 시간

    private int nx;            // 예보 지점 X 좌표
    private int ny;            // 예보 지점 Y 좌표

    private double t1h;   // 기온 (℃)
    private double rn1;   // 1시간 강수량 (mm)
    private double uuu;   // 동서바람성분 (m/s)
    private double vvv;   // 남북바람성분 (m/s)
    private int reh;      // 습도 (%)
    private int pty;      // 강수형태 (코드값)
    private double vec;   // 풍향 (deg)
    private double wsd;   // 풍속 (m/s)
}
