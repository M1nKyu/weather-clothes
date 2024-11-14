package com.software_engineering.weather_clothes.controller;

import com.software_engineering.weather_clothes.service.CrwalingClothingProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClothingProductController {

    @Autowired
    private CrwalingClothingProductService crwalingClothingProductService;

    @GetMapping("/scrape-clothing-products")
    public String scrapeAndSaveClothingProducts() {
        try {
            // 크롤링 후 DB에 저장
            crwalingClothingProductService.process();  // 크롤링 메서드 호출
            return "크롤링 완료 및 DB 저장됨!";
        } catch (Exception e) {
            // 예외 처리
            return "크롤링 중 오류가 발생했습니다: " + e.getMessage();
        }
    }
}
