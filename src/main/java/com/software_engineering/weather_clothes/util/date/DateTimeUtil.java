package com.software_engineering.weather_clothes.util.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeUtil {

    /**
     * 현재 날짜와 시간에 기반하여 baseDate와 baseTime을 반환.
     *
     * @return baseDate와 baseTime
     */
    public static String[] getBaseDateTime(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");

        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);

        if(minute < 30){
            cal.add(Calendar.HOUR_OF_DAY, -1);
            hour = cal.get(Calendar.HOUR_OF_DAY);
        }

        String baseDate = sdf1.format(cal.getTime());
        String baseTime = String.format("%02d30", hour);

//        System.out.println("baseDate"+baseDate);
//        System.out.println("baseTime"+baseTime);

        return new String[] {baseDate, baseTime};
    }

    /**
     *  전달할 날씨 데이터의 포맷을 변환.
     *  @return 포맷팅된 String (ex: "오전 7시")
     */
    public static String formatTime(String fcstTime) {

        int hour = Integer.parseInt(fcstTime.substring(0, 2));

        String period = hour < 12 ? "오전" : "오후";
        hour = (hour % 12 == 0) ? 12 : (hour % 12); // 0을 12로 변환

        return period + " " + hour + "시";
    }

    /**
     * 주어진 시간대가 낮인지 밤인지를 판별하는 메서드
     * @param fcstTime 예보 시간 (HHmm 형식)
     * @return 낮이면 true, 밤이면 false
     */
    public static boolean isDaytime(String fcstTime){
        int hour = Integer.parseInt(fcstTime.substring(0, 2)); // 시간 부분 추출
        return hour >= 6 && hour < 18;  // 낮 시간대라면 true 반환
    }


    /**
     * 현재 월을 기반으로 계절을 반환.
     *
     * @return 계절 정보 ("spring", "summer", "autumn", "winter")
     */
    public static String getSeason() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;

        if (month >= 3 && month <= 5) {
            return "spring";
        } else if (month >= 6 && month <= 8) {
            return "summer";
        } else if (month >= 9 && month <= 11) {
            return "autumn";
        } else {
            return "winter";
        }
    }
}
