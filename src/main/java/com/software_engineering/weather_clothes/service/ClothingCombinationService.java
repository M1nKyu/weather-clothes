package com.software_engineering.weather_clothes.service;

import com.software_engineering.weather_clothes.dto.ClothingCombinationDto;
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

    private static final Map<String, List<String>> TOP_BOTTOM_COLOR_COMBINATIONS = Map.of(
            "White", List.of("Black", "Navy", "Gray", "Beige"),
            "Blue", List.of("White", "Gray", "Beige"),
            "Red", List.of("Black", "White"),
            "Yellow", List.of("Blue", "Gray"),
            "Green", List.of("White", "Brown")
    );

    private static final Map<String, List<String>> TOP_SKIRT_COLOR_COMBINATIONS = Map.of(
            "White", List.of("Black", "Navy", "Gray", "Pink"),
            "Blue", List.of("White", "Pink"),
            "Red", List.of("Black", "Gray"),
            "Yellow", List.of("Blue", "White"),
            "Green", List.of("Brown", "Beige")
    );

    private String generateTopBottomColorCombination(String topColor) {
        List<String> possibleColors = TOP_BOTTOM_COLOR_COMBINATIONS.getOrDefault(topColor, Collections.emptyList());
        if (possibleColors.isEmpty()) {
            return "Black"; // 기본값
        }
        return possibleColors.get(new Random().nextInt(possibleColors.size()));
    }

    private String generateTopSkirtColorCombination(String topColor) {
        List<String> possibleColors = TOP_SKIRT_COLOR_COMBINATIONS.getOrDefault(topColor, Collections.emptyList());
        if (possibleColors.isEmpty()) {
            return "Black"; // 기본값
        }
        return possibleColors.get(new Random().nextInt(possibleColors.size()));
    }

    private String generateColorCombination(String mainCategory, List<ClothingCombinationDto> existingCombinations) {
        Random random = new Random();

        // 색상 정의
        String[] outerColors = {"Black", "Gray", "Navy", "Beige"};
        String[] topColors = {"White", "Blue", "Red", "Yellow", "Green"};

        // "아우터"는 독립적인 색상 지정
        if ("아우터".equals(mainCategory)) {
            return outerColors[random.nextInt(outerColors.length)];
        }

        // "상의"는 독립적으로 랜덤 색상 선택
        if ("상의".equals(mainCategory)) {
            return topColors[random.nextInt(topColors.length)];
        }

        // "바지"는 상의 색상 기반으로 조합 생성
        if ("바지".equals(mainCategory)) {
            String topColor = existingCombinations.stream()
                    .filter(comb -> "상의".equals(comb.getMainCategory()))
                    .map(ClothingCombinationDto::getColor)
                    .findFirst()
                    .orElse(null);

            if (topColor != null) {
                return generateTopBottomColorCombination(topColor);
            }
        }

        // "스커트"는 상의 색상 기반으로 조합 생성
        if ("원피스/스커트".equals(mainCategory)) {
            String topColor = existingCombinations.stream()
                    .filter(comb -> "상의".equals(comb.getMainCategory()))
                    .map(ClothingCombinationDto::getColor)
                    .findFirst()
                    .orElse(null);

            if (topColor != null) {
                return generateTopSkirtColorCombination(topColor);
            }
        }

        // "기타"는 색상 지정 없음
        return null;
    }

    public List<ClothingCombinationDto> generateCombinations(
            Map<String, List<ClothingCategory>> categories, int t1h) {

        TemperatureRange temperatureRange = TemperatureRange.getRange(t1h);
        Random random = new Random();

        while (true) { // 유효한 조합이 나올 때까지 반복
            List<ClothingCombinationDto> combinations = new ArrayList<>();

            for (String mainCategory : categories.keySet()) {
                List<ClothingCategory> items = categories.get(mainCategory);
                if (items != null && !items.isEmpty()) {
                    // 랜덤 의상 선택
                    ClothingCategory randomCategory = items.get(random.nextInt(items.size()));

                    // 색상 생성
                    String randomColor = generateColorCombination(mainCategory, combinations);

                    // DTO 생성 및 추가
                    combinations.add(new ClothingCombinationDto(mainCategory, randomCategory, randomColor));
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
    private boolean isValidCombination(List<ClothingCombinationDto> combinations, TemperatureRange range) {
        for (ClothingCombinationDto combination : combinations) {
            String mainCategory = combination.getMainCategory();
            ClothingCategory clothingCategory = combination.getClothingCategory();
            String color = combination.getColor();

            // 유효성 검사 로직 예시 (특정 색상 조합 금지 등)
            if ("Red".equals(color) && "상의".equals(mainCategory)) {
                return false; // 특정 조건에 의해 조합이 유효하지 않음
            }
        }
        return true; // 모든 조건을 통과하면 유효
    }
}
