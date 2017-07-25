package com.example.ideo7.weather.model;

/**
 * Created by ideo7 on 17.07.2017.
 */

public class Snow {
    private Double last3h;

    public Double getLast3h() {
        return last3h;
    }

    public void setLast3h(Double last3h) {
        this.last3h = last3h;
    }

    public Snow() {
    }

    public Snow(Double last3h) {
        this.last3h = last3h;
    }

    @Override
    public String toString() {
        return "Snow{" +
                "last3h=" + last3h +
                '}';
    }
}
