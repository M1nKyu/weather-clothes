package com.software_engineering.weather_clothes.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * WeatherApiClient는 기상청 API와의 통신을 담당하는 클래스입니다.
 * API 요청을 보내고, 응답 데이터를 문자열로 반환합니다.
 */
@Component
public class WeatherApiClient {

    // application.properties 에서 서비스키를 가져옵니다.
    @Value("${weather.api.key}")
    private String serviceKey;

    /**
     * 기상청 API에서 날씨 데이터를 가져오기 위한 HTTP GET 요청을 보냅니다.
     * 전달받은 nx, ny 좌표와 현재 시를 사용하여 날씨 데이터를 조회합니다.
     * @param nx 예보 지점 X 좌표
     * @param ny 예보 지점 Y 좌표
     * @return API 응답 문자열
     * @throws Exception
     */
    public String getWeatherData(String nx, String ny) throws Exception {

        // 현재 날짜와 시간을 요청 변수로 전달하기 위한 변수 today, nowTime
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH00");
        Date now = new Date();
        String today = sdf1.format(now);
        String nowTime = sdf2.format(now);

        // API 요청할 URL을 조합합니다.
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + today);
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + nowTime);
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + nx);
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + ny);

        // 위에서 조합한 url으로 URL 객체를 생성합니다.
        URL url = new URL(urlBuilder.toString());

        // HTTP 요청을 설정합니다.
        HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // 주어진 URL로 네트워크 연결을 열어줍니다.
        conn.setRequestMethod("GET"); // HTTP 요청 방식: GET
        conn.setRequestProperty("Content-type", "application/json"); // 요청 데이터 및 응답 데이터의 형식을 JSON으로 설정합니다.

        // 요청을 전송하고 응답 코드를 받아 응답 데이터를 처리합니다.
        // 요청이 실제로 서버에 전송되는 시점: conn.getResponseCode() 호출시
        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream())); // 성공: 응답 데이터를 읽습니다.
        }
        else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream())); // 실패: 에러 메시지를 읽습니다.
        }

        //  API 응답 데이터를 한 줄씩 읽어서 하나의 문자열로 결합한 후 반환합니다.
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }

        rd.close();
        conn.disconnect();
        return sb.toString(); // 응답 데이터를 문자열 형태로 반환
    }
}
