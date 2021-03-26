package com.kevinkirwansoftware.capsule.throwaway.weather;

import java.util.List;

public class Daily {
    public int dt;
    public int sunrise;
    public int sunset;
    public Temp temp;
    public FeelsLike feels_like;
    public int pressure;
    public int humidity;
    public double dew_point;
    public double wind_speed;
    public int wind_deg;
    public List<Weather> weather;
    public int clouds;
    public double pop;
    public double rain;
    public double uvi;

    public Daily(int dt, int sunrise, int sunset, Temp temp, FeelsLike feels_like, int pressure, int humidity, double dew_point, double wind_speed, int wind_deg, List<Weather> weather, int clouds, double pop, double rain, double uvi) {
        this.dt = dt;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.temp = temp;
        this.feels_like = feels_like;
        this.pressure = pressure;
        this.humidity = humidity;
        this.dew_point = dew_point;
        this.wind_speed = wind_speed;
        this.wind_deg = wind_deg;
        this.weather = weather;
        this.clouds = clouds;
        this.pop = pop;
        this.rain = rain;
        this.uvi = uvi;
    }

    public int getDt() {
        return dt;
    }

    public int getSunrise() {
        return sunrise;
    }

    public int getSunset() {
        return sunset;
    }

    public Temp getTemp() {
        return temp;
    }

    public FeelsLike getFeels_like() {
        return feels_like;
    }

    public int getPressure() {
        return pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public double getDew_point() {
        return dew_point;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public int getWind_deg() {
        return wind_deg;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public int getClouds() {
        return clouds;
    }

    public double getPop() {
        return pop;
    }

    public double getRain() {
        return rain;
    }

    public double getUvi() {
        return uvi;
    }
}
