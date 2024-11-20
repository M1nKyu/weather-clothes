package com.software_engineering.weather_clothes.enums;

public class WeatherStatusEnums {
    // 날씨 상태
    public enum SkyCondition{
        CLEAR(0, 2),
        MOSTLY_CLEAR(3, 5),
        MOSTLY_CLOUDY(6, 8),
        CLOUDY(9, 10);

        private final int min;
        private final int max;

        SkyCondition(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public static SkyCondition from(int value) {
            for (SkyCondition condition : values()) {
                if (value >= condition.min && value <= condition.max) {
                    return condition;
                }
            }
            throw new IllegalArgumentException("잘못된 SKY 값 :" + value);
        }

    }
    // 풍속
    public enum WindSpeed{
        LIGHT(0, 4),
        LITTLE_STRONG(4, 9),
        STRONG(9, 14),
        VERY_STRONG(14, 60);

        private final int min;
        private final int max;

        WindSpeed(int min, int max) {
            this.min = min;
            this.max = max;
        }

        public static WindSpeed from(double value) {
            for (WindSpeed strength : values()) {
                if (value >= strength.min && value < strength.max) {
                    return strength;
                }
            }
            throw new IllegalArgumentException("잘못된 WSD 값 :" + value);
        }
    }
    // 강수형태
    public enum PrecipitationType{
        NONE(0),
        RAIN(1),
        RAIN_SNOW(2),
        SNOW(3),
        RAIN_DROP(5),
        RAIN_SNOWFLAKE(6),
        SNOWFLAKE(7); // 눈날림

        private final int value;

        PrecipitationType(int value) {
            this.value = value;
        }

        public static PrecipitationType from(int value) {
            for (PrecipitationType type : values()) {
                if (type.value == value) {
                    return type;
                }
            }
            throw new IllegalArgumentException("잘못된 PTY 값 :" + value);
        }
    }

    public enum RainfallOneHour {
        NONE(0, 1),
        LIGHT(1, 3),
        NORMAL(3, 15),
        STRONG(15, 30),
        VERY_STRONG(30, 200);

        private final double min;
        private final double max;

        RainfallOneHour (double min, double max) {
            this.min = min;
            this.max = max;
        }

        public static RainfallOneHour from(double value) {
            for (RainfallOneHour intensity : values()) {
                if (value >= intensity.min && value < intensity.max) {
                    return intensity;
                }
            }
            throw new IllegalArgumentException("잘못된 RN1 값 :" + value);
        }
    }

}
