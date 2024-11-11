package com.software_engineering.weather_clothes.util.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTimeUtil {

    /**
     * 현재 날짜와 시간에 기반하여 baseDate와 baseTime을 반환합니다.
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
     * 현재 월을 기반으로 계절을 반환합니다.
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
