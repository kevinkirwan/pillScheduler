package com.kevinkirwansoftware.capsule.throwaway.weather;

public class FeelsLike {
    public double day;
    public double night;
    public double eve;
    public double morn;

    public FeelsLike(double day, double night, double eve, double morn) {
        this.day = day;
        this.night = night;
        this.eve = eve;
        this.morn = morn;
    }

    public double getDay() {
        return day;
    }

    public double getNight() {
        return night;
    }

    public double getEve() {
        return eve;
    }

    public double getMorn() {
        return morn;
    }
}
