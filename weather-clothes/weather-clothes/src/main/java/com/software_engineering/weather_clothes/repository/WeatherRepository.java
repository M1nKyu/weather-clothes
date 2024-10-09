package com.software_engineering.weather_clothes.repository;

import com.software_engineering.weather_clothes.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long> {
}
