package com.software_engineering.weather_clothes;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WeatherClothesApplication {

        public static void main(String[] args) {
                Dotenv dotenv = Dotenv.configure()
                        .directory("/home/ubuntu/weather-clothes")  // .env 파일이 있는 디렉토리 경로를 지정
                        .load();
                // 환경 변수를 시스템에 등록
                dotenv.entries().forEach(entry ->
                                System.setProperty(entry.getKey(), entry.getValue())
                );
		
                SpringApplication.run(WeatherClothesApplication.class, args);
        }
}
