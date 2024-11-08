package com.software_engineering.weather_clothes.controller;

import com.software_engineering.weather_clothes.model.Weather;
import com.software_engineering.weather_clothes.service.WeatherService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

@Controller
public class WeatherPageController {
    private final WeatherService weatherService;

    @Autowired
    public WeatherPageController(WeatherService weatherService){
        this.weatherService = weatherService;
    }

    /**
     * 쿠키에서 nx, ny 값을 가져와 날씨 데이터를 저장하고, 모델을 통해 뷰에 전달합니다.
     *
     * @param request HTTP 요청 객체
     * @param model   모델 객체
     * @return 날씨 데이터를 포함한 mainPage.html 페이지
     */
    @GetMapping("/")
    public String fetchWeather(HttpServletRequest request, Model model) throws Exception {
        String nx = null;
        String ny = null;

        // baseDate, baseTime 구함
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        if (minute < 30) {
            cal.add(Calendar.HOUR_OF_DAY, -1);
            hour = cal.get(Calendar.HOUR_OF_DAY);
        }
        String baseTime = String.format("%02d30", hour);
        String baseDate = sdf1.format(cal.getTime());


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

        // 현재 계절 결정
        int month = cal.get(Calendar.MONTH) + 1; // 0-based 이므로 1을 더함
        String season;
        if (month >= 3 && month <= 5) {
            season = "spring";
        } else if (month >= 6 && month <= 8) {
            season = "summer";
        } else if (month >= 9 && month <= 11) {
            season = "autumn";
        } else {
            season = "winter";
        }
        model.addAttribute("season", season);

        if (nx != null && ny != null) {
            // 날씨 데이터를 저장하고 조회
            weatherService.fetchAndStoreWeatherData(nx, ny);
            List<Weather> weatherData = weatherService.getWeatherData(baseDate, baseTime, Integer.parseInt(nx), Integer.parseInt(ny));
            if (!weatherData.isEmpty()) {
                Weather nowWeather = weatherData.get(0);
                List<String> clothingRecommendations = weatherService.getClothingRecommendations(nowWeather);
                model.addAttribute("clothingRecommendations", clothingRecommendations);

                model.addAttribute("weatherData", weatherData);  // 모델에 데이터 추가
            }
            return "mainPage";  // mainPage.html 템플릿 렌더링
        } else {
            model.addAttribute("message", "지역 정보를 설정해야 합니다!");
            return "mainPage";
        }
    }
}
