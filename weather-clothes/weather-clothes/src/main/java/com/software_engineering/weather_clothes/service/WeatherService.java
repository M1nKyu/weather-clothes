package com.software_engineering.weather_clothes.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.software_engineering.weather_clothes.api.WeatherApiClient;
import com.software_engineering.weather_clothes.model.Weather;
import com.software_engineering.weather_clothes.repository.WeatherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * WeatherService는 날씨 데이터를 API로부터 받아 파싱하고 DB에 저장.
 */
@Service
public class WeatherService {

    private final WeatherApiClient weatherApiClient;
    private final WeatherRepository weatherRepository;
    private final ObjectMapper objectMapper;

    public WeatherService(WeatherApiClient weatherApiClient, WeatherRepository weatherRepository, ObjectMapper objectMapper) {
        this.weatherApiClient = weatherApiClient;
        this.weatherRepository = weatherRepository;
        this.objectMapper = objectMapper;
    }


    /**
     * 주어진 nx, ny 좌표에 해당하는 날씨 데이터를 API에서 가져와 DB에 저장.
     * 중복되지 않은 데이터만 저장됩니다.
     * @param nx 예보 지점 X 좌표
     * @param ny 예보 지점 Y 좌표
     * @throws Exception
     */
    @Transactional
    public void fetchAndStoreWeatherData(String nx, String ny) throws Exception {
        String response = weatherApiClient.getWeatherData(nx, ny);
        JsonNode jsonNode = objectMapper.readTree(response);
        List<Weather> weatherList = parseWeatherData(jsonNode, nx, ny);

        // 중복되지 않은 데이터만 필터링하여 저장
        List<Weather> newWeatherList = new ArrayList<>();
        for (Weather weather : weatherList) {
            if (!isWeatherDataExists(weather)) {
                newWeatherList.add(weather);
            }
        }

        if (!newWeatherList.isEmpty()) {
            weatherRepository.saveAll(newWeatherList);
        }
    }

    /**
     * 날씨 데이터의 중복 여부를 확인합니다.
     * @param weather 확인할 날씨 데이터
     * @return 중복 여부 (true: 중복, false: 중복 아님)
     */
    private boolean isWeatherDataExists(Weather weather) {
        return weatherRepository.existsByBaseDateAndBaseTimeAndFcstDateAndFcstTimeAndNxAndNy(
                weather.getBaseDate(),
                weather.getBaseTime(),
                weather.getFcstDate(),
                weather.getFcstTime(),
                weather.getNx(),
                weather.getNy()
        );
    }

    /**
     * API로부터 받은 JSON 데이터를 Weather 객체로 변환하여 하나의 로우로 저장.
     * @param jsonNode API 응답 JSON 노드
     * @param nx 예보 지점 X 좌표
     * @param ny 예보 지점 Y 좌표
     * @return Weather 객체
     */
    private List<Weather> parseWeatherData(JsonNode jsonNode, String nx, String ny) {
        List<Weather> weatherList = new ArrayList<>();
        JsonNode items = jsonNode.path("response").path("body").path("items").path("item");

        // 예보 데이터를 카테고리별로 그룹화
        Map<String, Weather> weatherMap = new HashMap<>();

        // items 배열의 각 예보 데이터를 파싱하여 Weather 객체로 변환
        for (JsonNode item : items) {
            String fcstDate = item.path("fcstDate").asText();  // 예보 날짜
            String fcstTime = item.path("fcstTime").asText();  // 예보 시간
            String key = fcstDate + "_" + fcstTime; // 고유 키 생성

            Weather weather = weatherMap.computeIfAbsent(key, k -> new Weather());
            weather.setBaseDate(item.path("baseDate").asText());
            weather.setBaseTime(item.path("baseTime").asText());
            weather.setNx(Integer.parseInt(nx));
            weather.setNy(Integer.parseInt(ny));
            weather.setFcstDate(fcstDate);
            weather.setFcstTime(fcstTime);

            String category = item.path("category").asText();
            double fcstValue = item.path("fcstValue").asDouble();  // 예보 값

            // 카테고리별로 데이터를 매핑하여 Weather 객체에 설정
            switch (category) {
                case "T1H": weather.setT1h(fcstValue); break; // 기온
                case "RN1": weather.setRn1(fcstValue); break; // 1시간 강수량
                case "UUU": weather.setUuu(fcstValue); break; // 동서바람성분
                case "VVV": weather.setVvv(fcstValue); break; // 남북바람성분
                case "REH": weather.setReh((int) fcstValue); break; // 습도
                case "PTY": weather.setPty((int) fcstValue); break; // 강수형태
                case "VEC": weather.setVec(fcstValue); break; // 풍향
                case "WSD": weather.setWsd(fcstValue); break; // 풍속
            }
        }

        // 고유 Weather 객체들을 리스트에 추가
        weatherList.addAll(weatherMap.values());
        return weatherList;
    }

    public List<Weather> getWeatherData(String baseDate, String baseTime, int nx, int ny) {
        return weatherRepository.findTop6ByBaseDateAndBaseTimeAndNxAndNyOrderByFcstDateAscFcstTimeAsc(baseDate, baseTime, nx, ny);
    }

}
