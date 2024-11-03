package com.software_engineering.weather_clothes.controller;

import com.software_engineering.weather_clothes.service.WeatherService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * WeatherController는 날씨 데이터를 요청받고 처리.
 */
@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * 쿠키에서 nx, ny 값을 가져와 날씨 데이터를 저장합니다.
     *
     * @param request HTTP 요청 객체
     * @return 성공 메시지 또는 오류 메시지를 포함한 응답
     */
    @PostMapping("/fetchData")
    public ResponseEntity<?> fetchWeatherData(HttpServletRequest request) {
        String nx = null;
        String ny = null;

        // 쿠키에서 nx, ny 정보 가져오기
        for (Cookie cookie : request.getCookies()) {
            if ("userNx".equals(cookie.getName())) {
                nx = cookie.getValue();
            } else if ("userNy".equals(cookie.getName())) {
                ny = cookie.getValue();
            }
        }

        if (nx != null && ny != null) {
            try {
                // 날씨 데이터 저장 메소드 호출
                weatherService.fetchAndStoreWeatherData(nx, ny);
                return ResponseEntity.ok().body(Map.of("message", "Weather data successfully stored!"));
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body(Map.of("message", "Failed to fetch weather data!"));
            }
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Location data not found in cookies"));
        }
    }
}
