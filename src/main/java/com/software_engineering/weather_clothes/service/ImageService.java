package com.software_engineering.weather_clothes.service;

import com.software_engineering.weather_clothes.model.Weather;
import com.software_engineering.weather_clothes.util.date.DateTimeUtil;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    public String selectWeatherIcon(Weather weather){
        int sky = weather.getSky();
        int pty = weather.getPty();
        boolean isDaytime = DateTimeUtil.isDaytime(weather.getFcstTime());

        String icon = "";

        if(pty == 0){
            if(sky >= 0 && sky <= 5){
                icon =  "clear";
            }
            else if(sky >= 6 && sky <= 8){
                icon = "partly_cloudy";
            }
            else if(sky >= 9 && sky <= 10){
                icon = "cloudy";
            }
            else {
                icon = "clear";
            }
        }
        else if(pty == 1 || pty == 2 || pty == 5 || pty == 6){
            icon = "rain";
        }
        else if(pty == 3 || pty == 7){
            icon = "snow";
        }

        return "/images/weather-icons/" + (isDaytime ? "day/" : "night/") + icon + ".svg";
    }
}
