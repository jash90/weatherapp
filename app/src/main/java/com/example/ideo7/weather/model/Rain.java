package com.example.ideo7.weather.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ideo7 on 14.07.2017.
 */

public class Rain {
    @SerializedName("3h")
    private Double last3h;

    public Double getLast3h() {
        return last3h;
    }

    public void setLast3h(Double last3h) {
        this.last3h = last3h;
    }

    public Rain() {
    }

    public Rain(Double last3h) {
        this.last3h = last3h;
    }

    @Override
    public String toString() {
        return "Rain{" +
                "last3h=" + last3h +
                '}';
    }
}
