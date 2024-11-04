package com.software_engineering.weather_clothes.repository;

import com.software_engineering.weather_clothes.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository <Weather, Long> {
    /**
     * 주어진 조건에 해당하는 날씨 데이터가 이미 존재하는지 확인합니다.
     */
    boolean existsByBaseDateAndBaseTimeAndFcstDateAndFcstTimeAndNxAndNy(
            String baseDate,
            String baseTime,
            String fcstDate,
            String fcstTime,
            int nx,
            int ny
    );


}
