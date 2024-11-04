package com.software_engineering.weather_clothes.controller;

import com.software_engineering.weather_clothes.model.Weather;
import com.software_engineering.weather_clothes.service.WeatherService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
        Cookie[] cookies = request.getCookies(); // 쿠키 배열을 가져옴
        if (cookies != null) { // 쿠키가 null이 아닐 때만 반복문 실행
            for (Cookie cookie : cookies) {
                if ("userNx".equals(cookie.getName())) {
                    nx = cookie.getValue();
                } else if ("userNy".equals(cookie.getName())) {
                    ny = cookie.getValue();
                }
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
            return ResponseEntity.badRequest().body(Map.of("message", "Location data not found in cookies. Please enter your location."));
        }
    }

    /**
     * 쿠키에서 정보를 가져와 해당하는 날씨 데이터 6개를 반환합니다.
     *
     * @param request HTTP 요청 객체
     * @return 해당하는 날씨 데이터 목록
     */
    @GetMapping("/fetchWeather")
    public ResponseEntity<?> fetchWeather(HttpServletRequest request) {
        String nx = null;
        String ny = null;

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH30");

        Date now = new Date();
        String baseDate = sdf1.format(now);
        String baseTime = sdf2.format(now);

        // 쿠키에서 nx, ny 정보 가져오기
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userNx".equals(cookie.getName())) {
                    nx = cookie.getValue();
                } else if ("userNy".equals(cookie.getName())) {
                    ny = cookie.getValue();
                }
            }
        }

        if (nx != null && ny != null) {
            // 예시로 baseDate와 baseTime을 사용해야 할 곳에 적절한 값을 설정
            List<Weather> weatherData = weatherService.getWeatherData(baseDate, baseTime, Integer.parseInt(nx), Integer.parseInt(ny));
            return ResponseEntity.ok(weatherData);
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Location data not found in cookies."));
        }
    }
}
