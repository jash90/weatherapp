package com.example.ideo7.weather.Model;

/**
 * Created by ideo7 on 17.07.2017.
 */

public class Snow {
    private Integer last3h;

    public Integer getLast3h() {
        return last3h;
    }

    public void setLast3h(Integer last3h) {
        this.last3h = last3h;
    }

    public Snow() {
    }

    public Snow(Integer last3h) {
        this.last3h = last3h;
    }

    @Override
    public String toString() {
        return "Snow{" +
                "last3h=" + last3h +
                '}';
    }
}
