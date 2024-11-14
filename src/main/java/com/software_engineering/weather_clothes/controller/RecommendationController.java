package com.software_engineering.weather_clothes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import lombok.RequiredArgsConstructor;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;




@Controller
@RequiredArgsConstructor
public class RecommendationController {

    @GetMapping("/clothing/detail")
    public String showClothingDetail(@RequestParam String type, Model model) {
        WebDriver driver = null;
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
            
            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            
            String searchUrl = "https://bymono.com/shopSearch/search.html?query=" + 
                              URLEncoder.encode(type, StandardCharsets.UTF_8);
            
            System.out.println("Requesting URL: " + searchUrl);
            driver.get(searchUrl);
            
            // 페이지 로�� 대기
            Thread.sleep(3000);
            
            // 상품 목록 가져오기 (수정된 선택자)
            List<WebElement> items = driver.findElements(By.cssSelector("li[id^='anchorBoxId_']"));
            System.out.println("Found " + items.size() + " items");
            
            List<Map<String, String>> products = new ArrayList<>();
            
            for (WebElement item : items) {
                try {
                    Map<String, String> product = new HashMap<>();
                    
                    // 상품명
                    WebElement nameElement = item.findElement(By.cssSelector(".prd_nm.name span#productName_" + 
                        item.getAttribute("id").replace("anchorBoxId_", "")));
                    product.put("name", nameElement.getText().trim());
                    
                    // 가격 (할인가)
                    WebElement priceElement = item.findElement(By.cssSelector("li[rel='할인판매가']"));
                    product.put("price", priceElement.getText().trim());
                    
                    // 브랜드
                    try {
                        WebElement brandElement = item.findElement(By.cssSelector(".brand_nm"));
                        product.put("brand", brandElement.getText().trim());
                    } catch (Exception e) {
                        product.put("brand", "BYMONO");
                    }
                    
                    // 상품 URL
                    WebElement linkElement = item.findElement(By.cssSelector(".prdImg a"));
                    String productUrl = linkElement.getAttribute("href");
                    if (!productUrl.startsWith("http")) {
                        productUrl = "https://bymono.com" + productUrl;
                    }
                    product.put("productUrl", productUrl);
                    
                    // 이미지 URL
                    WebElement imgElement = item.findElement(By.cssSelector(".prdImg img"));
                    String imageUrl = imgElement.getAttribute("src");
                    if (!imageUrl.startsWith("http")) {
                        imageUrl = "https://bymono.com" + imageUrl;
                    }
                    product.put("imageUrl", imageUrl);
                    
                    // 리뷰 수
                    try {
                        WebElement reviewElement = item.findElement(By.cssSelector(".snap_review_count"));
                        product.put("reviews", reviewElement.getText().trim());
                    } catch (Exception e) {
                        product.put("reviews", "리뷰 0");
                    }
                    
                    products.add(product);
                    System.out.println("\nProduct found:");
                    product.forEach((k, v) -> System.out.println(k + ": " + v));
                    
                    if (products.size() >= 5) break;
                    
                } catch (Exception e) {
                    System.out.println("Error processing item: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            model.addAttribute("products", products);
            model.addAttribute("searchTerm", type);
            
        } catch (Exception e) {
            System.out.println("Error fetching data: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "상품 정보를 가져오는데 실패했습니다: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }

        return "recommendation/recommendation";
    }
} 