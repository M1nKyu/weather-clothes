package com.software_engineering.weather_clothes.controller;

import com.software_engineering.weather_clothes.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * WeatherController는 날씨 데이터를 요청받고 처리.
 */
@RestController
public class WeatherController {

    // WeatherService 객체를 주입받아 사용.
    private final WeatherService weatherService;
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * /weather 엔드포인트로 날씨 데이터를 가져옴.
     * @param nx 예보 지점 X 좌표
     * @param ny 예보 지점 Y 좌표
     * @return 처리 결과 메시지
     */
    @GetMapping("/weather")
    public String getWeather(@RequestParam("nx") String nx, @RequestParam("ny") String ny) {
        try {
            weatherService.fetchAndStoreWeatherData(nx, ny);
            return "Weather data successfully stored!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to fetch weather data!";
        }
    }
}
