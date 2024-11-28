package com.software_engineering.weather_clothes.service;

import com.software_engineering.weather_clothes.enums.WeatherStatusEnums.TemperatureRange;
import com.software_engineering.weather_clothes.model.ClothingCategory;
import com.software_engineering.weather_clothes.repository.ClothingCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ClothingCombinationService {

    private final ClothingCategoryRepository clothingCategoryRepository;

    public ClothingCombinationService (ClothingCategoryRepository clothingCategoryRepository){
        this.clothingCategoryRepository = clothingCategoryRepository;
    }


    public List<Map<String, ClothingCategory>> generateCombinations(
            Map<String, List<ClothingCategory>> categories, int t1h) {

        TemperatureRange temperatureRange = TemperatureRange.getRange(t1h);
        Random random = new Random();

        while (true) { // 유효한 조합이 나올 때까지 반복
            List<Map<String, ClothingCategory>> combinations = new ArrayList<>();

            // 각 카테고리에서 랜덤 선택
            for (String key : categories.keySet()) {
                List<ClothingCategory> items = categories.get(key);
                if (items != null && !items.isEmpty()) {
                    ClothingCategory randomCategory = items.get(random.nextInt(items.size()));
                    Map<String, ClothingCategory> selected = new HashMap<>();
                    selected.put(key, randomCategory);
                    combinations.add(selected);
                }
            }

            // 조합 유효성 검사
            if (isValidCombination(combinations, temperatureRange)) {
                return combinations; // 유효한 조합이면 반환
            }
        }
    }

    /**
     * 조합이 유효한지 확인하는 로직
     */
    private boolean isValidCombination(List<Map<String, ClothingCategory>> combinations, TemperatureRange range) {
        // 예시: 특정 조건에 따라 유효성 검사
        for (Map<String, ClothingCategory> combination : combinations) {
            for (Map.Entry<String, ClothingCategory> entry : combination.entrySet()) {
//                if (좋지 않은 조합인 조건) {
//                }
            }
        }
        return true; // 모든 조건을 통과하면 유효
    }



}
