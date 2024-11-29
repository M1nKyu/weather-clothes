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

    public ClothingCombinationService(ClothingCategoryRepository clothingCategoryRepository) {
        this.clothingCategoryRepository = clothingCategoryRepository;
    }

    // 상의와 하의(바지, 스커트)의 색상 조합
    private static final Map<String, List<String>> TOP_BOTTOM_COLOR_COMBINATIONS = Map.of(
            "#eaeaea", List.of("#000000", "#003666", "#c9c9c9", "#8ab0df", "#e6c18e", "#003666", "#636b2f"), // WHITE
            "#000000", List.of("#000000", "#003666"), // BLACK
            "#e6c18e", List.of("#5b3700", "#c9c9c9", "#000000", "#8ab0df"), // BEIGE
            "#5b3700", List.of("#7a7a7a", "#e6c18e", "#003666", "#000000"), // BROWN
            "#30bf2b", List.of("#8ab0df", "#eaeaea", "#c9c9c9", "#e6c18e", "#000000"), // GREEN
            "#7a7a7a", List.of("#000000", "#e6c18e"), // GRAY
            "#c9c9c9", List.of("#000000", "#e6c18e"), // LIGHT_GRAY
            "#881824", List.of("#7a7a7a"), // WINE
            "#8ab0df", List.of("#105b9c") // LIGHT_BLUE
    );

    private String generateTopBottomColorCombination(String topColor) {
        List<String> possibleColors = TOP_BOTTOM_COLOR_COMBINATIONS.getOrDefault(topColor, Collections.emptyList());
        if (possibleColors.isEmpty()) {
            return "#000000"; // 기본값 BLACK
        }
        return possibleColors.get(new Random().nextInt(possibleColors.size()));
    }

    private String generateColorCombination(String mainCategory, List<ClothingCombinationDto> existingCombinations) {
        Random random = new Random();

        // "아우터"는 독립적인 색상 지정
        if ("아우터".equals(mainCategory)) {
            return "#000000"; // 기본값 BLACK
        }

        // "상의"는 독립적으로 랜덤 색상 선택
        if ("상의".equals(mainCategory)) {
            List<String> topColors = new ArrayList<>(TOP_BOTTOM_COLOR_COMBINATIONS.keySet());
            return topColors.get(random.nextInt(topColors.size()));
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
                return generateTopBottomColorCombination(topColor); // 스커트도 같은 방식으로 조합
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
