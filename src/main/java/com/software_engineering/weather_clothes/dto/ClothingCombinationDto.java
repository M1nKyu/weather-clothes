package com.software_engineering.weather_clothes.dto;
import com.software_engineering.weather_clothes.model.ClothingCategory;


public class ClothingCombinationDto {
    private String mainCategory;
    private ClothingCategory clothingCategory;
    private String color;

    // 생성자
    public ClothingCombinationDto(String mainCategory, ClothingCategory clothingCategory, String color) {
        this.mainCategory = mainCategory;
        this.clothingCategory = clothingCategory;
        this.color = color;
    }

    // Getter와 Setter
    public String getMainCategory() {
        return mainCategory;
    }

    public void setMainCategory(String mainCategory) {
        this.mainCategory = mainCategory;
    }

    public ClothingCategory getClothingCategory() {
        return clothingCategory;
    }

    public void setClothingCategory(ClothingCategory clothingCategory) {
        this.clothingCategory = clothingCategory;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
