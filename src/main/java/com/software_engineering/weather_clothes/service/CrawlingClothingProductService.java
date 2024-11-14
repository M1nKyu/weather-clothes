package com.software_engineering.weather_clothes.service;

import com.software_engineering.weather_clothes.model.ClothingProduct;
import com.software_engineering.weather_clothes.repository.ClothingProductRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class CrawlingClothingProductService {

    @Value("${chrome.driver.path}")
    private String chromeDriverPath;

    @Autowired
    private ClothingProductRepository clothingProductRepository;

    private WebDriver driver;

    private static final String BASE_URL = "https://www.musinsa.com/category/";

    // 카테고리 ID 리스트 (샘플)
    private static final List<String> categories = List.of(
             "001006"
    );

    public void process() {
        // 크롬 드라이버 설정
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);

        try {
            for (String categoryId : categories) {
                String categoryUrl = BASE_URL + categoryId + "?gf=A";
                getDataList(categoryUrl, categoryId);
            }
        } catch (Exception e) {
            System.out.println("크롤링 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 크롤링 종료 후 리소스 정리
            if (driver != null) {
                driver.quit();  // 드라이버 종료
            }
            System.out.println("크롤링 작업이 완료되었습니다.");
        }
    }

    public void getDataList(String categoryUrl, String categoryId) {
        driver.get(categoryUrl);

        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            Thread.sleep(5000);  // 페이지 로딩 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int totalProducts = 0;
        int productRank = 1;

        while (totalProducts < 9) {  // 9개 이상의 상품 크롤링 예시
            try {
                // 상품의 XPath 계산 (3개씩 한 줄로 나열된 상품들)
                int rowNum = (productRank - 1) / 3 + 2;  // (1-3 상품 첫 줄, 4-6 상품 둘째 줄 등)
                int colNum = (productRank - 1) % 3 + 1;  // 각 줄의 1번, 2번, 3번 상품

                String productXpath = String.format("//*[@id='commonLayoutContents']/div[3]/div/div/div/div[%d]/div/div[%d]", rowNum, colNum);

                // 요소가 나타날 때까지 기다림
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                WebElement productElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(productXpath)));

                // 상품 정보 추출
                String productId = productElement.findElement(By.xpath(".//a")).getAttribute("data-item-id");
                String productLink = productElement.findElement(By.xpath(".//a")).getAttribute("href");
                String productImageUrl = productElement.findElement(By.xpath(".//a/div/img")).getAttribute("src");

                // 좋아요 정보 추출
                String likes = productElement.findElement(By.xpath(".//div[2]/div/div[2]/div[1]/span")).getText();

                // 마지막 업데이트 시간 (현재 시간 기준)
                String lastUpdated = getCurrentTimeFormatted();

                Optional<ClothingProduct> existingProduct = clothingProductRepository.findByProductId(productId);

                ClothingProduct clothingProduct = existingProduct.orElse(new ClothingProduct());
                clothingProduct.setProductId(productId);
                clothingProduct.setLink(productLink);
                clothingProduct.setImageUrl(productImageUrl);
                clothingProduct.setCategory(categoryId);
                clothingProduct.setLikes(likes);
                clothingProduct.setLastUpdated(lastUpdated);

                clothingProductRepository.save(clothingProduct);

                // 데이터 출력 (디버깅용)
                System.out.println("상품 ID: " + productId + ", 상품링크: " + productLink + ", 카테고리: " + categoryId + ", 이미지: " + productImageUrl + ", 좋아요: " + likes);

                productRank++;
                totalProducts = productRank - 1;

                Thread.sleep(500);

                // 스크롤을 내리지 않음, 페이지에 이미 로드된 상품만 크롤링

            } catch (Exception e) {
                System.out.println("크롤링 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    // 현재 시간을 YYMMDDHHmm 형식으로 반환
    private String getCurrentTimeFormatted() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");
        return now.format(formatter);
    }
}
