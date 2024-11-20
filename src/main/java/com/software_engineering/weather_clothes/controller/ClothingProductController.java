package com.software_engineering.weather_clothes.controller;

import com.software_engineering.weather_clothes.service.ClothingCategoryService;
import com.software_engineering.weather_clothes.service.ClothingProductService;
import com.software_engineering.weather_clothes.service.CrawlingClothingProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

@RestController
public class ClothingProductController {

    private CrawlingClothingProductService crwalingClothingProductService;
    private ClothingProductService clothingProductService;
    private ClothingCategoryService clothingCategoryService;

    public ClothingProductController(CrawlingClothingProductService crwalingClothingProductService, ClothingProductService clothingProductService, ClothingCategoryService clothingCategoryService){
        this.crwalingClothingProductService = crwalingClothingProductService;
        this.clothingProductService = clothingProductService;
        this.clothingCategoryService = clothingCategoryService;
    }

    @GetMapping("/scrape-clothing-products")
    public String scrapeAndSaveClothingProducts() {

        Logger logger = Logger.getLogger(ClothingProductController.class.getName()); // Logger 선언

        try {
            logger.info("크롤링 시작함");
            crwalingClothingProductService.process();
            logger.info("크롤링 성공함");
        } catch (Exception e) {
            logger.severe("크롤링 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return "크롤링 중 오류 발생: " + e.getMessage();
        }

        try {
            logger.info("좋아요 포맷 변환 시작함");
            clothingProductService.updateLikesForAllProducts();
            logger.info("좋아요 포맷 변환 완료함 ");
        } catch (Exception e) {
            logger.severe("좋아요 변환 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return "좋아요 변환 중 오류 발생: " + e.getMessage();
        }

        try {
            logger.info("카테고리 동기화 시작함");
            clothingCategoryService.syncCategories();
            logger.info("카테고리 동기화 성공함");
        } catch (Exception e) {
            logger.severe("카테고리 동기화 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return "카테고리 동기화 중 오류 발생: " + e.getMessage();
        }

        logger.info("크롤링 완료 / 좋아요 변환 완료 / 카테고리 업데이트 완료");
        return "크롤링 완료 / 좋아요 변환 완료 / 카테고리 업데이트 완료";
    }
}
