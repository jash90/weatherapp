package com.example.ideo7.weather.Model;

/**
 * Created by ideo7 on 14.07.2017.
 */

class Wind {
    private Double speed;
    private Integer deg;

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getDeg() {
        return deg;
    }

    public void setDeg(Integer deg) {
        this.deg = deg;
    }

    public Wind() {
    }

    public Wind(Double speed, Integer deg) {
        this.speed = speed;
        this.deg = deg;
    }

    @Override
    public String toString() {
        return "Wind{" +
                "speed=" + speed +
                ", deg=" + deg +
                '}';
    }
}
