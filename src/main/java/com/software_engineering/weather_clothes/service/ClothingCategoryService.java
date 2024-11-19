package com.software_engineering.weather_clothes.service;

import com.software_engineering.weather_clothes.model.ClothingCategory;
import com.software_engineering.weather_clothes.model.ClothingProduct;
import com.software_engineering.weather_clothes.model.Weather;
import com.software_engineering.weather_clothes.repository.ClothingCategoryRepository;
import com.software_engineering.weather_clothes.repository.ClothingProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClothingCategoryService {

    private final ClothingProductRepository clothingProductRepository;
    private final ClothingCategoryRepository clothingCategoryRepository;

    public ClothingCategoryService(ClothingProductRepository clothingProductRepository, ClothingCategoryRepository clothingCategoryRepository){
        this.clothingProductRepository = clothingProductRepository;
        this.clothingCategoryRepository = clothingCategoryRepository;
    }

    /**
     * 온도 값에 따른 여러 옷 카테고리를 추천합니다.
     *
     * @param weather
     * @return 옷 카테고리별 추천 리스트
     */
    public Map<String, List<ClothingCategory>> getClothingCategory(Weather weather){

        Map<String, List<ClothingCategory>> recommendations = new LinkedHashMap<>();

        // 상의, 바지, 아우터, 기타 카테고리 초기화
        recommendations.put("상의", new ArrayList<>());
        recommendations.put("바지", new ArrayList<>());
        recommendations.put("아우터", new ArrayList<>());
        recommendations.put("기타", new ArrayList<>());

        double temp = weather.getT1h();

        // 28도 이상
        if(temp >= 28){
            // 상의
            addClothingCategory(recommendations, "상의", "001001"); // 반소매 티셔츠
            addClothingCategory(recommendations, "상의", "001011"); // 민소매 티셔츠

            // 바지
            addClothingCategory(recommendations, "바지", "003009"); // 숏 팬츠

            // 원피스/스커트
            addClothingCategory(recommendations, "기타", "100001"); // 미니원피스
            addClothingCategory(recommendations, "기타", "100002"); // 미디원피스
            addClothingCategory(recommendations, "기타", "100004"); // 미니스커트
            addClothingCategory(recommendations, "기타", "100005"); // 미디스커트
        }
        // 23도 ~ 27도
        else if (temp >= 23) {
            // 상의
            addClothingCategory(recommendations, "상의", "001001"); // 반소매 티셔츠
            addClothingCategory(recommendations, "상의", "001011"); // 민소매 티셔츠

            // 바지
            addClothingCategory(recommendations, "바지", "003002"); // 데님 팬츠
            addClothingCategory(recommendations, "바지", "003007"); // 코튼 팬츠
            addClothingCategory(recommendations, "바지", "003008"); // 슈트 팬츠/슬랙스
            addClothingCategory(recommendations, "바지", "003009"); // 숏 팬츠

            // 원피스/스커트
            addClothingCategory(recommendations, "기타", "100001"); // 미니원피스
            addClothingCategory(recommendations, "기타", "100002"); // 미디원피스
            addClothingCategory(recommendations, "기타", "100003"); // 맥시원피스
            addClothingCategory(recommendations, "기타", "100004"); // 미니스커트
            addClothingCategory(recommendations, "기타", "100005"); // 미디스커트
            addClothingCategory(recommendations, "기타", "100006"); // 롱스커트

        // 20도 ~ 22도
        } else if (temp >= 20) {
            addClothingCategory(recommendations, "아우터", "002022"); // 후드 집업
            addClothingCategory(recommendations, "아우터", "002006"); // 나일론/코치 재킷

            // 상의
            addClothingCategory(recommendations, "상의", "001001"); // 반소매 티셔츠
            addClothingCategory(recommendations, "상의", "001003"); // 피케/카라 티셔츠
            addClothingCategory(recommendations, "상의", "001002"); // 셔츠 블라우스
            addClothingCategory(recommendations, "상의", "001010"); // 긴소매 티셔츠

            // 바지
            addClothingCategory(recommendations, "바지", "003002"); // 데님 팬츠
            addClothingCategory(recommendations, "바지", "003007"); // 코튼 팬츠
            addClothingCategory(recommendations, "바지", "003008"); // 슈트 팬츠/슬랙스

        // 16도 ~ 19도
        } else if (temp >= 16) {
            // 아우터
            addClothingCategory(recommendations, "아우터", "002021"); // 카디건
            addClothingCategory(recommendations, "아우터", "002021"); // 베스트
            addClothingCategory(recommendations, "아우터", "002006"); // 나일론/코치재킷
            addClothingCategory(recommendations, "아우터", "002002"); // 라이더스 재킷
            addClothingCategory(recommendations, "아우터", "002004"); // 스타디움 재킷
            addClothingCategory(recommendations, "아우터", "002017"); // 트러커 재킷
            addClothingCategory(recommendations, "아우터", "002022"); // 후드 집업

            // 상의
            addClothingCategory(recommendations, "상의", "001005"); // 맨투맨/스웨트
            addClothingCategory(recommendations, "상의", "001004"); // 후드/티셔츠
            addClothingCategory(recommendations, "상의", "001006"); // 니트/스웨터
            addClothingCategory(recommendations, "상의", "001010"); // 긴소매 티셔츠

            // 바지
            addClothingCategory(recommendations, "바지", "003002"); // 데님 팬츠
            addClothingCategory(recommendations, "바지", "003007"); // 코튼 팬츠
            addClothingCategory(recommendations, "바지", "003008"); // 슈트 팬츠/슬랙스


        // 11도 ~ 15도
        } else if (temp >= 11) {
            // 아우터
            addClothingCategory(recommendations, "아우터", "002002"); // 라이더스 재킷
            addClothingCategory(recommendations, "아우터", "002004"); // 스타디움 재킷
            addClothingCategory(recommendations, "아우터", "002017"); // 트러커 재킷
            addClothingCategory(recommendations, "아우터", "002022"); // 후드 집업
            addClothingCategory(recommendations, "아우터", "002014"); // 사파리/헌팅 재킷
            addClothingCategory(recommendations, "아우터", "002008"); // 환절기 코트

            // 상의
            addClothingCategory(recommendations, "상의", "001005"); // 맨투맨/스웨트
            addClothingCategory(recommendations, "상의", "001002"); // 셔츠/블라우스
            addClothingCategory(recommendations, "상의", "001004"); // 후드/티셔츠
            addClothingCategory(recommendations, "상의", "001006"); // 니트/스웨터
            addClothingCategory(recommendations, "상의", "001010"); // 긴소매 티셔츠

            // 바지
            addClothingCategory(recommendations, "바지", "003002"); // 데님 팬츠
            addClothingCategory(recommendations, "바지", "003007"); // 코튼 팬츠
            addClothingCategory(recommendations, "바지", "003008"); // 슈트 팬츠/슬랙스


        // 5도 ~ 10도
        } else if (temp >= 5) {
            // 아우터
            addClothingCategory(recommendations, "아우터", "002023"); // 플리스/뽀글이
            addClothingCategory(recommendations, "아우터", "002007"); // 겨울 싱글 코트
            addClothingCategory(recommendations, "아우터", "002024"); // 겨울 더블 코트
            addClothingCategory(recommendations, "아우터", "002013"); // 롱패딩/헤비 아우터
            addClothingCategory(recommendations, "아우터", "002012"); // 숏패딩/헤비 아우터
            addClothingCategory(recommendations, "아우터", "002016"); // 패딩 베스트

            // 상의
            addClothingCategory(recommendations, "상의", "001004"); // 후드/티셔츠
            addClothingCategory(recommendations, "상의", "001006"); // 니트/스웨터
            addClothingCategory(recommendations, "상의", "001005"); // 맨투맨/스웨트

            // 바지
            addClothingCategory(recommendations, "바지", "003002"); // 데님 팬츠
            addClothingCategory(recommendations, "바지", "003007"); // 코튼 팬츠
            addClothingCategory(recommendations, "바지", "003008"); // 슈트 팬츠/슬랙스

            // 기타
            addClothingCategory(recommendations, "기타", "101008"); // 머플러

        // 4도 이하
        } else  {
            // 아우터
            addClothingCategory(recommendations, "아우터", "002023"); // 플리스/뽀글이
            addClothingCategory(recommendations, "아우터", "002007"); // 겨울 싱글 코트
            addClothingCategory(recommendations, "아우터", "002024"); // 겨울 더블 코트
            addClothingCategory(recommendations, "아우터", "002013"); // 롱패딩/헤비 아우터
            addClothingCategory(recommendations, "아우터", "002012"); // 숏패딩/헤비 아우터
            addClothingCategory(recommendations, "아우터", "002016"); // 패딩 베스트

            // 상의
            addClothingCategory(recommendations, "상의", "001004"); // 후드/티셔츠
            addClothingCategory(recommendations, "상의", "001006"); // 니트/스웨터
            addClothingCategory(recommendations, "상의", "001005"); // 맨투맨/스웨트

            // 바지
            addClothingCategory(recommendations, "바지", "003002"); // 데님 팬츠
            addClothingCategory(recommendations, "바지", "003007"); // 코튼 팬츠
            addClothingCategory(recommendations, "바지", "003008"); // 슈트 팬츠/슬랙스

            // 기타
            addClothingCategory(recommendations, "기타", "101008"); // 머플러
            addClothingCategory(recommendations, "기타", "101004007"); // 장갑

        }
        recommendations.entrySet().removeIf(entry -> entry.getValue().isEmpty());

        return recommendations;
    }

    private void addClothingCategory(Map<String, List<ClothingCategory>> recommendations,
                                     String category, String categoryId) {
        ClothingCategory clothingCategory = clothingCategoryRepository
                .findByCategoryId(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found: " + categoryId));

        recommendations.get(category).add(clothingCategory);
    }

    @Transactional
    public void syncCategories(){
        // ClothingProduct에서 고유한 카테고리 정보 추출
        List<ClothingProduct> products = clothingProductRepository.findAll();

        // categoryId와 categoryName을 기준으로 중복 제거
        List<ClothingCategory> categories = products.stream()
                .map(product -> {
                    ClothingCategory category = new ClothingCategory();
                    category.setCategoryId(product.getCategoryId());
                    category.setCategoryName(product.getCategoryName());
                    return category;
                })
                .distinct() // Lombok의 @Data가 equals와 hashCode를 제공하므로 동작함
                .filter(category -> category.getCategoryId() != null && category.getCategoryName() != null)
                .filter(category -> !clothingCategoryRepository.existsByCategoryId(category.getCategoryId()))
                .collect(Collectors.toList());

        // 새로운 카테고리들 저장
        if (!categories.isEmpty()) {
            clothingCategoryRepository.saveAll(categories);
        }
    }

    public Map<String, Map<String, List<ClothingProduct>>> getClothingProductsFromCategories(
            Map<String, List<ClothingCategory>> clothingCategories) {

        Map<String, Map<String, List<ClothingProduct>>> result = new LinkedHashMap<>();

        // 각 대분류(상의, 바지, 아우터, 기타)별로 처리
        for (Map.Entry<String, List<ClothingCategory>> entry : clothingCategories.entrySet()) {
            String mainCategory = entry.getKey();
            List<ClothingCategory> categories = entry.getValue();

            // 대분류별 맵 초기화
            Map<String, List<ClothingProduct>> categoryProducts = new LinkedHashMap<>();

            // 각 카테고리별로 상품 조회 및 정렬
            for (ClothingCategory category : categories) {
                // 해당 카테고리의 상품들을 likes 내림차순으로 최대 10개 조회
                List<ClothingProduct> products = clothingProductRepository
                        .findByCategoryId(category.getCategoryId())
                        .stream()
                        .sorted((p1, p2) -> {
                            // null 체크 및 예외 처리
                            try {
                                int likes1 = p1.getLikes() != null ? Integer.parseInt(p1.getLikes()) : 0;
                                int likes2 = p2.getLikes() != null ? Integer.parseInt(p2.getLikes()) : 0;
                                return Integer.compare(likes2, likes1); // 내림차순
                            } catch (NumberFormatException e) {
                                // 숫자로 변환할 수 없는 경우 0으로 처리
                                return 0;
                            }
                        })
                        .limit(10)
                        .collect(Collectors.toList());

                // 결과가 있는 경우만 맵에 추가
                if (!products.isEmpty()) {
                    categoryProducts.put(category.getCategoryName(), products);
                }
            }

            // 해당 대분류에 상품이 있는 경우만 결과 맵에 추가
            if (!categoryProducts.isEmpty()) {
                result.put(mainCategory, categoryProducts);
            }
        }
        return result;
    }
}
