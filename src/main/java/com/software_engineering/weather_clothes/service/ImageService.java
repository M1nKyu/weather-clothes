package com.software_engineering.weather_clothes.service;

import com.software_engineering.weather_clothes.enums.WeatherStatusEnums.*;
import com.software_engineering.weather_clothes.model.Weather;
import com.software_engineering.weather_clothes.util.DateTimeUtil;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    public String selectWeatherIcon(Weather weather){
        int t1hValue = weather.getT1h();
        int skyValue = weather.getSky();
        int ptyValue = weather.getPty();
        boolean isDaytime = DateTimeUtil.isDaytime(weather.getFcstTime());

        SkyCondition sky = SkyCondition.from(skyValue);
        PrecipitationType pty = PrecipitationType.from(ptyValue);

        String icon = "";

        // 강수 형태가 없는 경우
        if(pty == PrecipitationType.NONE) {
            switch(sky){
                case CLEAR -> icon = "clear";
                case MOSTLY_CLEAR -> icon = "partly-cloudy";
                case MOSTLY_CLOUDY -> icon = "overcast";
                case CLOUDY -> icon = "extreme";
            }
        }
        else if(pty == PrecipitationType.RAIN || pty == PrecipitationType.RAIN_DROP){
            icon = "rain";
        }
        else if(pty == PrecipitationType.SNOW || pty == PrecipitationType.SNOWFLAKE){
            icon = "snow";
        }
        else{
            if(t1hValue > 0){
                icon = "rain";
            }
            else{
                icon = "snow";
            }
        }
        return "/images/weather-icons/" + (isDaytime ? "day/" : "night/") + icon + ".svg";
    }

    public String selectBackgroundImage(Weather weather){

        String backgroundImage = "";

        int ptyValue = weather.getPty();
        boolean isDaytime = DateTimeUtil.isDaytime(weather.getFcstTime());

        PrecipitationType pty = PrecipitationType.from(ptyValue);

        if(pty == PrecipitationType.NONE){
            if(isDaytime)
                backgroundImage = "/images/background/day.png";
            else
                backgroundImage = "/images/background/night.png";
        }
        else {
            backgroundImage = "/images/background/rainSnow.png";
        }

        return backgroundImage;
    }
}
