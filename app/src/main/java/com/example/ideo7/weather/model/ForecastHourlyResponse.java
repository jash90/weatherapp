package com.example.ideo7.weather.model;

import java.util.List;

/**
 * Created by ideo7 on 18.07.2017.
 */

public class ForecastHourlyResponse {
    private Integer cod;
    private Double message;
    private Integer cnt;
    private List<HourlyWeather> list;
    private City city;
    private String country;

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

    public List<HourlyWeather> getList() {
        return list;
    }

    public void setList(List<HourlyWeather> list) {
        this.list = list;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ForecastHourlyResponse() {
    }

    public ForecastHourlyResponse(Integer cod, Double message, Integer cnt, List<HourlyWeather> list, City city, String country) {
        this.cod = cod;
        this.message = message;
        this.cnt = cnt;
        this.list = list;
        this.city = city;
        this.country = country;
    }

    @Override
    public String toString() {
        return "ForecastHourlyResponse{" +
                "cod=" + cod +
                ", message=" + message +
                ", cnt=" + cnt +
                ", list=" + list +
                ", city=" + city +
                ", country='" + country + '\'' +
                '}';
    }
}
