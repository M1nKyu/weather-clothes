package com.software_engineering.weather_clothes.controller;

import com.software_engineering.weather_clothes.dto.ClothingCombinationDto;
import com.software_engineering.weather_clothes.model.ClothingCategory;
import com.software_engineering.weather_clothes.model.ClothingProduct;
import com.software_engineering.weather_clothes.model.Weather;
import com.software_engineering.weather_clothes.service.ClothingCategoryService;
import com.software_engineering.weather_clothes.service.ClothingCombinationService;
import com.software_engineering.weather_clothes.service.ImageService;
import com.software_engineering.weather_clothes.service.WeatherService;
import com.software_engineering.weather_clothes.sheduler.ClothingProductScheduler;
import com.software_engineering.weather_clothes.util.CookieUtil;
import com.software_engineering.weather_clothes.util.DateTimeUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


@Controller
@EnableScheduling // 스케줄링 활성화
public class WeatherPageController {
    Logger logger = Logger.getLogger(ClothingProductScheduler.class.getName()); // Logger 선언

    private final WeatherService weatherService;
    private final ImageService imageService;
    private final ClothingCategoryService clothingCategoryService;
    private final ClothingCombinationService clothingCombinationService;

    @Autowired
    public WeatherPageController(WeatherService weatherService, ImageService imageService,
                                 ClothingCategoryService clothingCategoryService, ClothingCombinationService clothingCombinationService){
        this.weatherService = weatherService;
        this.imageService = imageService;
        this.clothingCategoryService = clothingCategoryService;
        this.clothingCombinationService = clothingCombinationService;
    }

    /**
     * 쿠키에서 nx, ny 값을 가져와 날씨 데이터를 저장하고, 모델을 통해 뷰에 전달합니다.
     *
     * @param request HTTP 요청 객체
     * @param model   모델 객체
     * @return 날씨 데이터를 포함한 global.css 페이지
     */
    @GetMapping("/")
    public String fetchWeather(HttpServletRequest request, Model model) throws Exception {

        String baseDate = DateTimeUtil.getBaseDateTime()[0];
        String baseTime = DateTimeUtil.getBaseDateTime()[1];

        String nx = CookieUtil.getNxNyFromCookies(request)[0];
        String ny = CookieUtil.getNxNyFromCookies(request)[1];
        String[] userLocation = CookieUtil.getLocationFromCookies(request);

        // 기본값으로 서울특별시 설정 (regionList.csv 기준: nx=60, ny=127)
        if (nx == null || ny == null || userLocation == null) {
            nx = "60";
            ny = "127";
            userLocation = new String[]{"default", "서울특별시", "종로구"};  // 두 번째 요소를 서울특별시로 설정
        }
        
        // 항상 위치 정보를 모델에 추가
        model.addAttribute("userLocation", userLocation);


        if (nx != null && ny != null) {

            // 날씨 정보를 데이터베이스에 저장하고 weatherData로 가져옴.
            weatherService.fetchAndStoreWeatherData(nx, ny);
            List<Weather> weatherData = weatherService.getWeatherData(baseDate, baseTime, Integer.parseInt(nx), Integer.parseInt(ny));


            // 성공적으로 가져왔을 때
            if (!weatherData.isEmpty()) {
                Weather nowWeather = weatherData.get(0); // 현재 시간의 날씨 정보 (1 row)
                List<Weather> fcstWeather = weatherData.subList(1, weatherData.size()); // 예보된 날씨 정보 (5 rows)

                Map<String, List<ClothingCategory>> clothingCategory = clothingCategoryService.getClothingCategory(nowWeather); // 추천된 옷 카테고리
                List<ClothingCombinationDto> clothingCombination = clothingCombinationService.generateCombinations(clothingCategory, nowWeather.getT1h());
                Map<String, Map<String, List<ClothingProduct>>> clothingProducts = clothingCategoryService.getClothingProductsFromCategories(clothingCategory);

                nowWeather.setIcon(imageService.selectWeatherIcon(nowWeather));

                // 각 예보의 fcstTime을 포맷팅 (ex: "1600" -> "오후 4시")
                fcstWeather.forEach(weather -> {
                    weather.setIcon(imageService.selectWeatherIcon(weather));

                    String formattedTime = DateTimeUtil.formatTime(weather.getFcstTime());
                    weather.setFcstTime(formattedTime); // 포맷팅된 fcstTime 값으로 변경
                });

                // 추가 날씨 정보 (하늘 상태, 풍속, 강수 형태)
                Map<String, String> weatherDetails = new HashMap<>();
                weatherDetails.put("sky", weatherService.getSkyCondition(nowWeather.getSky()));
                weatherDetails.put("wsd", weatherService.getWindSpeedCondition(nowWeather.getWsd()));
                weatherDetails.put("pty", weatherService.getPrecipitationType(nowWeather.getPty()));
                weatherDetails.put("rn1", weatherService.getRainfallOneHour(nowWeather.getRn1()));
                weatherDetails.put("reh", String.valueOf(nowWeather.getReh()));
                weatherDetails.entrySet().removeIf(entry -> "없음".equals(entry.getValue()));

                // 날씨에 따른 배경화면 지정
                String weatherInfoBackground = imageService.selectBackgroundImage(nowWeather);

                logger.info(weatherInfoBackground);
                logger.info(clothingCombination.toString());

                // 상품 정보를 같은 카테고리, 타입별로 한 줄에 출력
                clothingProducts.forEach((category, productsByType) -> {
                    logger.info("Category: " + category);
                    productsByType.forEach((type, products) -> {
                        StringBuilder productInfo = new StringBuilder("  Type: " + type + " -> ");
                        products.forEach(product -> {
                            productInfo.append(String.format("[ID: %s, Name: %s, Link: %s] ",
                                    product.getCategoryId(),
                                    product.getCategoryName(),
                                    product.getLink()));
                        });
                        logger.info(productInfo.toString());
                    });
                });


                model.addAttribute("nowWeather", nowWeather); // 현재 날씨
                model.addAttribute("weatherDetails", weatherDetails);
                model.addAttribute("weatherInfoBackground", weatherInfoBackground);
                model.addAttribute("fcstWeather", fcstWeather); // 예보 날씨
                model.addAttribute("clothingCategory", clothingCategory); // 추천 카테고리
                model.addAttribute("clothingCombination", clothingCombination); // 추천 조합
                model.addAttribute("clothingProducts", clothingProducts); // 추천 카테고리별 상품
            }
            else{
                logger.info("no weather data");
            }
            return "mainPage";  // global.css 템플릿 렌더링
        } else {
            model.addAttribute("message", "지역 정보를 설정해야 합니다!");
            return "mainPage";
        }
    }

}
