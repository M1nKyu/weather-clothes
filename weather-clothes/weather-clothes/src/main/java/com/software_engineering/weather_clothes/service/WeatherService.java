package com.software_engineering.weather_clothes.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.software_engineering.weather_clothes.api.WeatherApiClient;
import com.software_engineering.weather_clothes.model.Weather;
import com.software_engineering.weather_clothes.repository.WeatherRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * @param nx 예보 지점 X 좌표
     * @param ny 예보 지점 Y 좌표
     * @throws Exception
     */
    public void fetchAndStoreWeatherData(String nx, String ny) throws Exception {
        String response = weatherApiClient.getWeatherData(nx, ny);
        JsonNode jsonNode = objectMapper.readTree(response);
        Weather weather = parseWeatherData(jsonNode, nx, ny);
        weatherRepository.save(weather);
    }

    /**
     * API로부터 받은 JSON 데이터를 Weather 객체로 변환하여 하나의 로우로 저장.
     * @param jsonNode API 응답 JSON 노드
     * @param nx 예보 지점 X 좌표
     * @param ny 예보 지점 Y 좌표
     * @return Weather 객체
     */
    private Weather parseWeatherData(JsonNode jsonNode, String nx, String ny) {
        Weather weather = new Weather();
        JsonNode items = jsonNode.path("response").path("body").path("items").path("item");

        // 기본 정보 설정
        weather.setBaseDate(items.get(0).path("baseDate").asText());
        weather.setBaseTime(items.get(0).path("baseTime").asText());
        weather.setNx(Integer.parseInt(nx));
        weather.setNy(Integer.parseInt(ny));

        // 카테고리별로 데이터를 매핑하여 하나의 Weather 객체에 설정
        for (JsonNode item : items) {
            String category = item.path("category").asText();
            double value = item.path("obsrValue").asDouble();

            switch (category) {
                case "T1H": weather.setT1h(value); break;
                case "RN1": weather.setRn1(value); break;
                case "UUU": weather.setUuu(value); break;
                case "VVV": weather.setVvv(value); break;
                case "REH": weather.setReh((int) value); break;
                case "PTY": weather.setPty((int) value); break;
                case "VEC": weather.setVec(value); break;
                case "WSD": weather.setWsd(value); break;
            }
        }

        return weather;
    }
}
