package com.software_engineering.weather_clothes.service;

import com.software_engineering.weather_clothes.model.ClothingProduct;
import com.software_engineering.weather_clothes.repository.ClothingProductRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CrwalingClothingProductService {

    @Value("${chrome.driver.path}") // application.properties에 경로 저장 필요
    private String chromeDriverPath;

    @Autowired
    private ClothingProductRepository clothingProductRepository;

    private WebDriver driver;
    private static final String url = "https://www.musinsa.com/main/musinsa/ranking";

    public void process() {
        // 크롬 드라이버 세팅 (드라이버 설치 경로 입력)
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        // 브라우저 선택
        driver = new ChromeDriver(options);

        getDataList();

        // 탭 닫기
        driver.close();
        // 브라우저 닫기
        driver.quit();
    }

    public void getDataList() {
        driver.get(url);

        // 페이지가 로드될 때까지 기다리기 위한 Thread.sleep 적용
        try {
            Thread.sleep(5000);  // 5초 대기 (적절히 조정 가능)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 스크롤을 내려서 계속 로드되는 상품을 크롤링
        JavascriptExecutor js = (JavascriptExecutor) driver;

        int totalProducts = 0;
        int productRank = 1;  // 상품 순위

        while (totalProducts < 5) {  // 300개의 상품을 목표로 크롤링
            try {
                // 행 번호 계산: (순위 - 1) / 3 + 2
                int rowNum = ((productRank - 1) / 3) + 2;

                // 상품 번호 계산: (순위 - 1) % 3 + 1
                int colNum = (productRank - 1) % 3 + 1;

                // 상품의 XPath 동적으로 생성
                String productXpath = String.format("/html/body/div[1]/div/main/article/div[%d]/div[%d]", rowNum, colNum);

                // 상품 요소 찾기
                WebElement productElement = driver.findElement(By.xpath(productXpath));

                // 상품 ID 추출 (data-item-id)
                String productId = productElement.getAttribute("data-item-id");

                // 상품 URL 추출
                WebElement productLinkElement = productElement.findElement(By.xpath(".//div[1]/a"));
                String productLink = productLinkElement.getAttribute("href");

                // 상품 이미지 URL 추출
                WebElement productImageElement = productElement.findElement(By.xpath(".//div[1]/a/div/img"));
                String productImageUrl = productImageElement.getAttribute("src");

                // 브랜드명 추출
                String brand = productElement.getAttribute("data-item-brand");

                // 가격 추출
                String price = productElement.getAttribute("data-original-price");

                // 상품 카테고리 추출 (상품 상세 페이지로 이동하여 카테고리 값을 추출)
                String category = getCategoryFromProductPage(productLink);

                // ClothingProduct 객체 생성
                ClothingProduct clothingProduct = new ClothingProduct(productId, price, productLink, brand, productImageUrl, category);

                // DB에 저장
                clothingProductRepository.save(clothingProduct);

                // 크롤링된 데이터 출력
                System.out.println("상품 ID: " + productId + ", 가격: " + price + ", 브랜드: " + brand + ", 링크: " + productLink + ", 카테고리: " + category);

                // 순위를 증가시켜가며 계속 크롤링
                productRank++;

                // 총 크롤링된 상품 수 갱신
                totalProducts = productRank - 1;

                // 스크롤을 내려서 더 많은 항목 로드
                js.executeScript("window.scrollTo(0, document.body.scrollHeight);");

                // 잠시 기다려서 새로운 항목이 로드되도록
                try {
                    Thread.sleep(3000);  // 3초 대기 (적절히 조정)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (NoSuchElementException | StaleElementReferenceException e) {
                // 상품 요소를 찾지 못하거나 페이지가 새로 고쳐졌을 경우 예외 처리
                System.out.println("상품 요소를 찾을 수 없습니다. 예외: " + e.getMessage());
            } catch (Exception e) {
                // 다른 예외 처리
                System.out.println("알 수 없는 오류가 발생했습니다: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // 상세 페이지에서 카테고리 정보를 추출하는 메서드
    public String getCategoryFromProductPage(String productUrl) {
        try {
            driver.get(productUrl);

            // 페이지가 로드될 때까지 기다리기 위한 Thread.sleep 적용
            Thread.sleep(3000);  // 3초 대기

            // 카테고리 추출
            WebElement categoryElement = driver.findElement(By.xpath("//*[@id='root']/div[1]/div[2]/div[3]/div/span[3]/a[1]"));
            String category = categoryElement.getAttribute("data-category-name");

            // 상품 상세 페이지에서 카테고리 추출 후, 랭킹 페이지로 돌아가기
            driver.navigate().back();

            return category;

        } catch (NoSuchElementException | InterruptedException e) {
            System.out.println("카테고리를 찾을 수 없습니다. 예외: " + e.getMessage());
            return null;
        }
    }
}
