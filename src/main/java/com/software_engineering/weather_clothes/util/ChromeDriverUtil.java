package com.software_engineering.weather_clothes.util;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeDriverUtil {
    public static WebDriver initializeDriver(String chromeDriverPath) {
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*"); // 원격 리소스 허용
//        options.addArguments("--headless"); // 헤드리스 모드 (UI 없이 실행)
        options.addArguments("--no-sandbox"); // 샌드박스 비활성화 (권한 문제 해결)
        options.addArguments("--disable-dev-shm-usage"); // /dev/shm 파티션 문제 해결
        options.addArguments("--disable-gpu"); // GPU 가속 비활성화 (필요 없을 경우)
        options.addArguments("--window-size=1920,1080"); // 화면 크기 설정
        options.addArguments("--disable-extensions"); // 불필요한 확장 프로그램 비활성화
        options.addArguments("--disable-popup-blocking"); // 팝업 차단 비활성화
        options.addArguments("--disable-infobars"); // 정보 표시줄 비활성화
        options.addArguments("--enable-automation"); // 자동화 플래그 설정

        return new ChromeDriver(options);
    }
}
