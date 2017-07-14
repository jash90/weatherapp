package com.example.ideo7.weather.Model;

/**
 * Created by ideo7 on 14.07.2017.
 */

class Rain {
    private Integer last3h;

    public Integer getLast3h() {
        return last3h;
    }

    public void setLast3h(Integer last3h) {
        this.last3h = last3h;
    }

    public Rain() {
    }

    public Rain(Integer last3h) {
        this.last3h = last3h;
    }

    @Override
    public String toString() {
        return "Rain{" +
                "last3h=" + last3h +
                '}';
    }
}
