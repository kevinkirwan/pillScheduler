package com.kevinkirwansoftware.capsule.throwaway.weather;

public class Minutely {
    public int dt;
    public int precipitation;

    public Minutely(int dt, int precipitation) {
        this.dt = dt;
        this.precipitation = precipitation;
    }

    public int getDt() {
        return dt;
    }

    public int getPrecipitation() {
        return precipitation;
    }
}
