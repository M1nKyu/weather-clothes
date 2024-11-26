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
        // 비 또는 비/눈 형태일 경우
        else if(t1hValue >= 0 && isRainOrRainSnow(pty)) {
            icon = "rain";
        }
        // 눈 또는 눈/비 형태일 경우
        else if(t1hValue < 0 && isSnowOrSnowRain(pty)) {
            icon = "snow";
        }

        return "/images/weather-icons/" + (isDaytime ? "day/" : "night/") + icon + ".svg";
    }

    private boolean isRainOrRainSnow(PrecipitationType pty) {
        return pty == PrecipitationType.RAIN || pty == PrecipitationType.RAIN_DROP ||
                pty == PrecipitationType.RAIN_SNOWFLAKE || pty == PrecipitationType.RAIN_SNOW;
    }

    private boolean isSnowOrSnowRain(PrecipitationType pty) {
        return pty == PrecipitationType.SNOW || pty == PrecipitationType.SNOWFLAKE ||
                pty == PrecipitationType.RAIN_SNOWFLAKE;
    }

    public String selectBackgroundImage(Weather weather){

        String backgroundImage = "";

        int ptyValue = weather.getPty();
        boolean isDaytime = DateTimeUtil.isDaytime(weather.getFcstTime());

        PrecipitationType pty = PrecipitationType.from(ptyValue);

        if(pty == PrecipitationType.NONE){
            if(isDaytime)
                backgroundImage = "/images/background/day1.png";
            else
                backgroundImage = "/images/background/day1.png";
        }
        else {
            backgroundImage = "/image/background/rainSnow.png";
        }

        return backgroundImage;
    }
}
