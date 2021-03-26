package com.kevinkirwansoftware.capsule.throwaway.weather;

import com.google.gson.annotations.SerializedName;

public class Rain {
    @SerializedName("1h")
    private double h1;

    public Rain(double h1) {
        this.h1 = h1;
    }

    public double getH1() {
        return h1;
    }
}
