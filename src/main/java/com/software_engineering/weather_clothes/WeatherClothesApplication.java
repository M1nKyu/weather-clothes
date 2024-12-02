package com.software_engineering.weather_clothes;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableAsync
public class WeatherClothesApplication {

        public static void main(String[] args) {
                Dotenv dotenv = Dotenv.load();  // .env 파일 로드
                // 환경 변수를 시스템에 등록
                dotenv.entries().forEach(entry ->
                        System.setProperty(entry.getKey(), entry.getValue())
                );

                SpringApplication.run(WeatherClothesApplication.class, args);
        }
}