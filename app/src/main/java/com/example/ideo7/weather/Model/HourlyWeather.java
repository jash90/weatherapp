package com.example.ideo7.weather.Model;

import com.example.ideo7.weather.Model.Clouds;
import com.example.ideo7.weather.Model.Rain;
import com.example.ideo7.weather.Model.Snow;
import com.example.ideo7.weather.Model.Temp;
import com.example.ideo7.weather.Model.Weather;
import com.example.ideo7.weather.Model.Wind;

import java.util.List;

/**
 * Created by ideo7 on 18.07.2017.
 */

public class HourlyWeather {
    private Integer dt;
    private Main main;
    private List<Weather> weather;
    private Clouds clouds;
    private Wind wind;
    private Rain rain;
    private Snow snow;
    private Sys pod;
    private String dt_txt;

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Rain getRain() {
        return rain;
    }

    public void setRain(Rain rain) {
        this.rain = rain;
    }

    public Snow getSnow() {
        return snow;
    }

    public void setSnow(Snow snow) {
        this.snow = snow;
    }

    public Sys getPod() {
        return pod;
    }

    public void setPod(Sys pod) {
        this.pod = pod;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }

    public HourlyWeather() {
    }

    public HourlyWeather(Integer dt, Main main, List<Weather> weather, Clouds clouds, Wind wind, Rain rain, Snow snow, Sys pod, String dt_txt) {
        this.dt = dt;
        this.main = main;
        this.weather = weather;
        this.clouds = clouds;
        this.wind = wind;
        this.rain = rain;
        this.snow = snow;
        this.pod = pod;
        this.dt_txt = dt_txt;
    }

    @Override
    public String toString() {
        return "HourlyWeather{" +
                "dt=" + dt +
                ", main=" + main +
                ", weather=" + weather +
                ", clouds=" + clouds +
                ", wind=" + wind +
                ", rain=" + rain +
                ", snow=" + snow +
                ", pod=" + pod +
                ", dt_txt='" + dt_txt + '\'' +
                '}';
    }
}

