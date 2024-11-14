package com.software_engineering.weather_clothes.service;

import com.software_engineering.weather_clothes.model.Weather;
import com.software_engineering.weather_clothes.repository.ClothingProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClothingCategoryService {

    private final ClothingProductRepository clothingProductRepository;

    public ClothingCategoryService(ClothingProductRepository clothingProductRepository){
        this.clothingProductRepository = clothingProductRepository;
    }

    /**
     * 온도 값에 따른 여러 옷 카테고리를 추천합니다.
     *
     * @param weather
     * @return 옷 카테고리 리스트
     */
    public List<String> getClothingRecommendations(Weather weather){

        List<String> recommendations = new ArrayList<>();
        double temp = weather.getT1h();

        if (temp >= 28) {
            recommendations.add("민소매");
            recommendations.add("반팔");
            recommendations.add("반바지");
            recommendations.add("짧은 치마");
            recommendations.add("린넨 옷");
        } else if (temp >= 23) {
            recommendations.add("반팔");
            recommendations.add("얇은 셔츠");
            recommendations.add("반바지");
            recommendations.add("면바지");
        } else if (temp >= 20) {
            recommendations.add("블라우스");
            recommendations.add("긴팔 티");
            recommendations.add("면바지");
            recommendations.add("슬랙스");
        } else if (temp >=17) {
            recommendations.add("얇은 가디건");
            recommendations.add("얇은 니트");
            recommendations.add("맨투맨");
            recommendations.add("후드");
            recommendations.add("긴 바지");
        } else if (temp >= 12){
            recommendations.add("자켓");
            recommendations.add("가디건");
            recommendations.add("청자켓");
            recommendations.add("니트");
            recommendations.add("스타킹");
            recommendations.add("청바지");
        } else if (temp >= 9){
            recommendations.add("트렌치 코트");
            recommendations.add("야상");
            recommendations.add("점퍼");
            recommendations.add("스타킹");
            recommendations.add("기모바지");
        }
        else if (temp >= 5){
            recommendations.add("울 코트");
            recommendations.add("가죽 옷");
            recommendations.add("기모");
        }else {
            recommendations.add("패딩");
            recommendations.add("두꺼운 코트");
            recommendations.add("기모");
            recommendations.add("목도리");
        }

        return recommendations;
    }
}
