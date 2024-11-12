package com.software_engineering.weather_clothes.controller;

import com.software_engineering.weather_clothes.model.Weather;
import com.software_engineering.weather_clothes.service.WeatherService;
import com.software_engineering.weather_clothes.util.date.CookieUtil;
import com.software_engineering.weather_clothes.util.date.DateTimeUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
     * @return 날씨 데이터를 포함한 mainPage.css 페이지
     */
    @GetMapping("/")
    public String fetchWeather(HttpServletRequest request, Model model) throws Exception {

        String baseDate = DateTimeUtil.getBaseDateTime()[0];
        String baseTime = DateTimeUtil.getBaseDateTime()[1];

        String nx = CookieUtil.getNxNyFromCookies(request)[0];
        String ny = CookieUtil.getNxNyFromCookies(request)[1];

        String season = DateTimeUtil.getSeason();
        model.addAttribute("season", season);

        if (nx != null && ny != null) {

            weatherService.fetchAndStoreWeatherData(nx, ny);
            List<Weather> weatherData = weatherService.getWeatherData(baseDate, baseTime, Integer.parseInt(nx), Integer.parseInt(ny));

            if (!weatherData.isEmpty()) {
                Weather nowWeather = weatherData.get(0);
                List<Weather> fcstWeather = weatherData.subList(1, weatherData.size());

                // 각 예보의 fcstTime을 포맷팅
                fcstWeather.forEach(weather -> {
                    String formattedTime = DateTimeUtil.formatTime(weather.getFcstTime());
                    weather.setFcstTime(formattedTime); // 포맷팅된 fcstTime 값을 설정
                });

                List<String> clothingRecommendations = weatherService.getClothingRecommendations(nowWeather);

                model.addAttribute("nowWeather", nowWeather);
                model.addAttribute("fcstWeather", fcstWeather);
                model.addAttribute("clothingRecommendations", clothingRecommendations);
            }
            return "mainPage";  // mainPage.css 템플릿 렌더링
        } else {
            model.addAttribute("message", "지역 정보를 설정해야 합니다!");
            return "mainPage";
        }
    }
}
