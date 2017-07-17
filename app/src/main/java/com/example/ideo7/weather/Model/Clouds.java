package com.example.ideo7.weather.Model;

/**
 * Created by ideo7 on 14.07.2017.
 */

public class Clouds {
   private Integer all;

    public Integer getAll() {
        return all;
    }

    public void setAll(Integer all) {
        this.all = all;
    }

    public Clouds() {
    }

    public Clouds(Integer all) {
        this.all = all;
    }

    @Override
    public String toString() {
        return "Clouds{" +
                "all=" + all +
                '}';
    }
}
