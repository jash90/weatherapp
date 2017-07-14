package com.example.ideo7.weather.Model;

/**
 * Created by ideo7 on 14.07.2017.
 */

class Clouds {
   private Integer clouds;

    public Integer getClouds() {
        return clouds;
    }

    public void setClouds(Integer clouds) {
        this.clouds = clouds;
    }

    public Clouds() {
    }

    public Clouds(Integer clouds) {
        this.clouds = clouds;
    }

    @Override
    public String toString() {
        return "Clouds{" +
                "clouds=" + clouds +
                '}';
    }
}
