package com.software_engineering.weather_clothes.sheduler;

import com.software_engineering.weather_clothes.service.ClothingCategoryService;
import com.software_engineering.weather_clothes.service.ClothingProductService;
import com.software_engineering.weather_clothes.service.CrawlingClothingProductService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Component
public class ClothingProductScheduler {

    private CrawlingClothingProductService crwalingClothingProductService;
    private ClothingProductService clothingProductService;
    private ClothingCategoryService clothingCategoryService;

    public ClothingProductScheduler(CrawlingClothingProductService crwalingClothingProductService, ClothingProductService clothingProductService, ClothingCategoryService clothingCategoryService){
        this.crwalingClothingProductService = crwalingClothingProductService;
        this.clothingProductService = clothingProductService;
        this.clothingCategoryService = clothingCategoryService;
    }

    @Scheduled(cron = "0 0 0 * * *")  // 자정마다 실행
    public String scrapeAndSaveClothingProducts() {
        Logger logger = Logger.getLogger(ClothingProductScheduler.class.getName());

        try {
            logger.info("크롤링 시작함");
            CompletableFuture<Void> crawlingTask = crwalingClothingProductService.processAsync();
            crawlingTask.get();  // 비동기 작업이 끝날 때까지 기다림
            logger.info("크롤링 성공함");
        } catch (Exception e) {
            logger.severe("크롤링 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return "크롤링 중 오류 발생: " + e.getMessage();
        }

        try {
            logger.info("좋아요 포맷 변환 시작함");
            CompletableFuture<Void> likesTask = clothingProductService.updateLikesForAllProductsAsync();
            likesTask.get();  // 비동기 작업이 끝날 때까지 기다림
            logger.info("좋아요 포맷 변환 완료함 ");
        } catch (Exception e) {
            logger.severe("좋아요 변환 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
            return "좋아요 변환 중 오류 발생: " + e.getMessage();
        }

        try {
            logger.info("카테고리 동기화 시작함");
            CompletableFuture<Void> categorySyncTask = clothingCategoryService.syncCategoriesAsync();
            categorySyncTask.get();  // 비동기 작업이 끝날 때까지 기다림
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
