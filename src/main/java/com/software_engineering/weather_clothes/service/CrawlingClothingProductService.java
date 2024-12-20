package com.software_engineering.weather_clothes.service;

import com.software_engineering.weather_clothes.sheduler.ClothingProductScheduler;
import com.software_engineering.weather_clothes.model.ClothingProduct;
import com.software_engineering.weather_clothes.repository.ClothingProductRepository;
import com.software_engineering.weather_clothes.util.ChromeDriverUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Service
public class CrawlingClothingProductService {
    Logger logger = Logger.getLogger(ClothingProductScheduler.class.getName()); // Logger 선언

    @Async
    public CompletableFuture<Void> processAsync() throws InterruptedException {
        process();
        return CompletableFuture.completedFuture(null);
    }

    @Value("${chrome.driver.path}")
    private String chromeDriverPath;

    @Autowired
    private ClothingProductRepository clothingProductRepository;

    private WebDriver driver;

    private static final String BASE_URL = "https://www.musinsa.com/category/";

    // 카테고리 ID 리스트
    private static final List<String> categories = List.of(
            "001001", "001011", "001003", "001002", "001010", "001005", "001004", "001006", // 상의
            "003009", "003002", "003007", "003008", // 바지
            "002022", "002006", "002021", "002002", "002004", "002017", "002023", "002007", "002024", "002013", "002012", "002016", "002014", "002008", // 아우터
            "101008", "101004007" // 기타 (장갑, 머플러 등)
    );


    public void process() {
        // 크롬드라이버 초기화
        driver = ChromeDriverUtil.initializeDriver(chromeDriverPath);

        try {
            for (String categoryId : categories) {
                String categoryUrl = BASE_URL + categoryId + "?gf=A&sortCode=SALE_ONE_WEEK_COUNT";
                getDataList(categoryUrl, categoryId);
            }
        } catch (Exception e) {
            logger.info("크롤링 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (driver != null) {
                driver.quit(); // 드라이버 종료
            }
            logger.info("크롤링 작업이 완료되었습니다.");
        }
    }


    public void getDataList(String categoryUrl, String categoryId) throws InterruptedException {
        driver.get(categoryUrl);

        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            Thread.sleep(5000);  // 페이지 로딩 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 먼저, 새로운 경로에서 요소 존재 여부를 확인
        WebElement categoryElement;
        try {
            // //*[@id="commonLayoutContents"]/div[1]/section/div[2]/div 가 존재하면
            WebElement possibleCategoryContainer = driver.findElement(By.xpath("//*[@id='commonLayoutContents']/div[1]/section/div[2]/div"));

            // 해당 경로에서 카테고리명 추출
            categoryElement = possibleCategoryContainer.findElement(By.xpath(".//span[@class='text-body_14px_semi text-black font-pretendard']"));
        } catch (Exception e) {
            // 해당 경로가 없으면, 기존 경로로 카테고리명 추출
            categoryElement = driver.findElement(By.xpath("//*[@id='commonLayoutContents']/div[1]/section/div/div//span[@class='text-body_14px_semi text-black font-pretendard']"));
        }

        String categoryName = categoryElement.getText().trim();  // 카테고리명 추출


        // 먼저 한 줄의 높이를 계산하기 위해 첫 번째 상품을 찾음
        WebElement firstProduct = driver.findElement(By.xpath("//*[@id='commonLayoutContents']/div[3]/div/div/div/div[2]/div/div[1]"));

        int productHeight = firstProduct.getSize().getHeight();  // 상품 한 줄의 높이를 측정

        int totalProducts = 0;
        int row = 1;
        int maxRow = 10;

        while (totalProducts < 18) {
            for(int col = 1; col <= 3; col++){
                try {
                    // 상품의 XPath 계산
                    String productXpath = String.format("//*[@id='commonLayoutContents']/div[3]/div/div/div/div[%d]/div/div[%d]", row, col);

                    // 요소가 나타날 때까지 기다림
                    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                    WebElement productElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(productXpath)));

                    // 상품 정보 추출
                    String productId = productElement.findElement(By.xpath(".//a")).getAttribute("data-item-id");
                    String productLink = productElement.findElement(By.xpath(".//a")).getAttribute("href");
                    String productImageUrl = productElement.findElement(By.xpath(".//a/div/img")).getAttribute("src");

                    // 좋아요 정보 추출
                    String likes;
                    try {
                        likes = productElement.findElement(By.xpath(".//div[2]/div[1]/span")).getText();

                        if (likes.equals("남성") || likes.equals("여성")) { // 좋아요수가 0이면 "남성", "여성" 문구가 추출되는 경우가 있음
                            likes = "0";
                        }
                    } catch (Exception e) {
                        likes = "0";
                    }

                    // 마지막 업데이트 시간 (현재 시간 기준)
                    String lastUpdated = getCurrentTimeFormatted();

                    Optional<ClothingProduct> existingProduct = clothingProductRepository.findByProductId(productId);
                    ClothingProduct clothingProduct = existingProduct.orElse(new ClothingProduct());
                    clothingProduct.setProductId(productId);
                    clothingProduct.setLink(productLink);
                    clothingProduct.setImageUrl(productImageUrl);
                    clothingProduct.setCategoryId(categoryId);
                    clothingProduct.setCategoryName(categoryName);  
                    clothingProduct.setLikes(likes);
                    clothingProduct.setLastUpdated(lastUpdated);

                    // DB에 저장
                    clothingProductRepository.save(clothingProduct);

                    // 데이터 출력 (디버깅용)
                    logger.info("ID: " + productId + ", 링크: " + productLink + ", 카테고리: " + categoryId + ", 이미지: " + productImageUrl + ", 좋아요: " + likes);
                    totalProducts++;

                } catch (Exception e) {
                    logger.info("크롤링 오류 발생: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            row++;
            if (row > maxRow) {
                row = 1;
            }

            js.executeScript("window.scrollBy(0, " + productHeight + ");");
            Thread.sleep(2000);
        }
    }


    // 현재 시간을 YYMMDDHHmm 형식으로 반환
    private String getCurrentTimeFormatted() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");
        return now.format(formatter);
    }
}
