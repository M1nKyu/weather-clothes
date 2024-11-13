package com.software_engineering.weather_clothes.util.date;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class CookieUtil {
    /**
     * 요청에서 쿠키 값을 추출하여 nx, ny 값을 반환합니다.
     *
     * @param request HTTP 요청 객체
     * @return [nx, ny] 값을 포함한 배열
     */
    public static String[] getNxNyFromCookies(HttpServletRequest request) {
        String nx = null;
        String ny = null;

        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("userNx".equals(cookie.getName())) {
                    nx = cookie.getValue();
                }
                else if ("userNy".equals(cookie.getName())) {
                    ny = cookie.getValue();
                }
            }
        }

//        System.out.println("nx: " + nx);
//        System.out.println("ny: " + ny);
        return new String[] { nx, ny };
    }
}
