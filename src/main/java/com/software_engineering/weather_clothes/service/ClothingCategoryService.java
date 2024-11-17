package com.software_engineering.weather_clothes.service;

import com.software_engineering.weather_clothes.model.ClothingCategory;
import com.software_engineering.weather_clothes.model.ClothingProduct;
import com.software_engineering.weather_clothes.model.Weather;
import com.software_engineering.weather_clothes.repository.ClothingCategoryRepository;
import com.software_engineering.weather_clothes.repository.ClothingProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ClothingCategoryService {

    private final ClothingProductRepository clothingProductRepository;

    public ClothingCategoryService(ClothingProductRepository clothingProductRepository){
        this.clothingProductRepository = clothingProductRepository;
    }

    /**
     * 온도 값에 따른 여러 옷 카테고리를 추천합니다.
     *
     * @param weather
     * @return 옷 카테고리별 추천 리스트
     */
    public Map<String, List<String>> getClothingCategoryId(Weather weather){

        Map<String, List<String>> recommendations = new HashMap<>();

        // 상의, 바지, 아우터, 기타 카테고리 초기화
        recommendations.put("상의", new ArrayList<>());
        recommendations.put("바지", new ArrayList<>());
        recommendations.put("아우터", new ArrayList<>());
        recommendations.put("기타", new ArrayList<>());

        double temp = weather.getT1h();

        // 28도 이상
        if(temp >= 28){
            // 상의
            recommendations.get("상의").add("001001"); // 반소매 티셔츠
            recommendations.get("상의").add("001011"); // 민소매 티셔츠

            // 바지
            recommendations.get("바지").add("003009"); // 숏 팬츠

            // 원피스/스커트
            recommendations.get("기타").add("100001"); // 미니원피스
            recommendations.get("기타").add("100002"); // 미디원피스
            recommendations.get("기타").add("100004"); // 미니스커트
            recommendations.get("기타").add("100005"); // 미디스커트
        }
        // 23도 ~ 27도
        else if (temp >= 23) {
            // 상의
            recommendations.get("상의").add("001001"); // 반소매 티셔츠
            recommendations.get("상의").add("001011"); // 민소매 티셔츠

            // 바지
            recommendations.get("바지").add("003002"); // 데님 팬츠
            recommendations.get("바지").add("003007"); // 코튼 팬츠
            recommendations.get("바지").add("003008"); // 슈트 팬츠/슬랙스
            recommendations.get("바지").add("003009"); // 숏 팬츠

            // 원피스/스커트
            recommendations.get("기타").add("100001"); // 미니원피스
            recommendations.get("기타").add("100002"); // 미디원피스
            recommendations.get("기타").add("100003"); // 맥시원피스
            recommendations.get("기타").add("100004"); // 미니스커트
            recommendations.get("기타").add("100005"); // 미디스커트
            recommendations.get("기타").add("100006"); // 롱스커트

            // 20도 ~ 22도
        } else if (temp >= 20) {
            recommendations.get("아우터").add("002022"); // 후드 집업
            recommendations.get("아우터").add("002006"); // 나일론/코치 재킷

            // 상의
            recommendations.get("상의").add("001001"); // 반소매 티셔츠
            recommendations.get("상의").add("001003"); // 피케/카라 티셔츠
            recommendations.get("상의").add("001002"); // 셔츠 블라우스
            recommendations.get("상의").add("001010"); // 긴소매 티셔츠

            // 바지
            recommendations.get("바지").add("003002"); // 데님 팬츠
            recommendations.get("바지").add("003007"); // 코튼 팬츠
            recommendations.get("바지").add("003008"); // 슈트 팬츠/슬랙스

            // 16도 ~ 19도
        } else if (temp >= 16) {
            // 아우터
            recommendations.get("아우터").add("002021"); // 카디건
            recommendations.get("아우터").add("002021"); // 베스트
            recommendations.get("아우터").add("002006"); // 나일론/코치재킷
            recommendations.get("아우터").add("002002"); // 라이더스 재킷
            recommendations.get("아우터").add("002004"); // 스타디움 재킷
            recommendations.get("아우터").add("002017"); // 트러커 재킷
            recommendations.get("아우터").add("002022"); // 후드 집업

            // 상의
            recommendations.get("상의").add("001005"); // 맨투맨/스웨트
            recommendations.get("상의").add("001004"); // 후드/티셔츠
            recommendations.get("상의").add("001006"); // 니트/스웨터
            recommendations.get("상의").add("001010"); // 긴소매 티셔츠

            // 바지
            recommendations.get("바지").add("003002"); // 데님 팬츠
            recommendations.get("바지").add("003007"); // 코튼 팬츠
            recommendations.get("바지").add("003008"); // 슈트 팬츠/슬랙스

            // 11도 ~ 15도
        } else if (temp >= 11) {
            // 아우터
            recommendations.get("아우터").add("002002"); // 라이더스 재킷
            recommendations.get("아우터").add("002004"); // 스타디움 재킷
            recommendations.get("아우터").add("002017"); // 트러커 재킷
            recommendations.get("아우터").add("002022"); // 후드 집업
            recommendations.get("아우터").add("002014"); // 사파리/헌팅 재킷
            recommendations.get("아우터").add("002008"); // 환절기 코트

            // 상의
            recommendations.get("상의").add("001005"); // 맨투맨/스웨트
            recommendations.get("상의").add("001002"); // 셔츠/블라우스
            recommendations.get("상의").add("001004"); // 후드/티셔츠
            recommendations.get("상의").add("001006"); // 니트/스웨터
            recommendations.get("상의").add("001010"); // 긴소매 티셔츠

            // 바지
            recommendations.get("바지").add("003002"); // 데님 팬츠
            recommendations.get("바지").add("003007"); // 코튼 팬츠
            recommendations.get("바지").add("003008"); // 슈트 팬츠/슬랙스

            // 5도 ~ 10도
        } else if (temp >= 5) {
            // 아우터
            recommendations.get("아우터").add("002023"); // 플리스/뽀글이
            recommendations.get("아우터").add("002007"); // 겨울 싱글 코트
            recommendations.get("아우터").add("002024"); // 겨울 더블 코트
            recommendations.get("아우터").add("002013"); // 롱패딩/헤비 아우터
            recommendations.get("아우터").add("002012"); // 숏패딩/헤비 아우터
            recommendations.get("아우터").add("002016"); // 패딩 베스트

            // 상의
            recommendations.get("상의").add("001004"); // 후드/티셔츠
            recommendations.get("상의").add("001006"); // 니트/스웨터
            recommendations.get("상의").add("001005"); // 맨투맨/스웨트

            // 바지
            recommendations.get("바지").add("003002"); // 데님 팬츠
            recommendations.get("바지").add("003007"); // 코튼 팬츠
            recommendations.get("바지").add("003008"); // 슈트 팬츠/슬랙스

            // 기타
            recommendations.get("기타").add("101008"); // 머플러

            // 4도 이하
        } else  {
            // 아우터
            recommendations.get("아우터").add("002023"); // 플리스/뽀글이
            recommendations.get("아우터").add("002007"); // 겨울 싱글 코트
            recommendations.get("아우터").add("002024"); // 겨울 더블 코트
            recommendations.get("아우터").add("002013"); // 롱패딩/헤비 아우터
            recommendations.get("아우터").add("002012"); // 숏패딩/헤비 아우터
            recommendations.get("아우터").add("002016"); // 패딩 베스트

            // 상의
            recommendations.get("상의").add("001004"); // 후드/티셔츠
            recommendations.get("상의").add("001006"); // 니트/스웨터
            recommendations.get("상의").add("001005"); // 맨투맨/스웨트

            // 바지
            recommendations.get("바지").add("003002"); // 데님 팬츠
            recommendations.get("바지").add("003007"); // 코튼 팬츠
            recommendations.get("바지").add("003008"); // 슈트 팬츠/슬랙스

            // 기타
            recommendations.get("기타").add("101008"); // 머플러
            recommendations.get("기타").add("101004007"); // 장갑
        }
        recommendations.entrySet().removeIf(entry -> entry.getValue().isEmpty());

        return recommendations;
    }
}
