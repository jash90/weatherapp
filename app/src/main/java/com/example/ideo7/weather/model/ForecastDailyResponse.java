package com.example.ideo7.weather.model;

import java.util.ArrayList;


public class ForecastDailyResponse {
    private City city;
    private Integer cod;
    private Double message;
    private Integer cnt;
    private ArrayList<DailyWeather> list;

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public Double getMessage() {
        return message;
    }

    public void setMessage(Double message) {
        this.message = message;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public ArrayList<DailyWeather> getList() {
        return list;
    }

    public void setList(ArrayList<DailyWeather> list) {
        this.list = list;
    }

    public ForecastDailyResponse() {
    }

    public ForecastDailyResponse(City city, Integer cod, Double message, Integer cnt, ArrayList<DailyWeather> list) {
        this.city = city;
        this.cod = cod;
        this.message = message;
        this.cnt = cnt;
        this.list = list;
    }

    @Override
    public String toString() {
        return "ForecastDailyResponse{" +
                "city=" + city +
                ", cod=" + cod +
                ", message=" + message +
                ", cnt=" + cnt +
                ", list=" + list +
                '}';
    }
}
