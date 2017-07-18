package com.example.ideo7.weather.Model;

import com.example.ideo7.weather.Model.City;
import com.example.ideo7.weather.Model.DailyWeather;

import java.util.List;

/**
 * Created by ideo7 on 18.07.2017.
 */

public class ForecastResponse {
    private Integer cod;
    private Double message;
    private Integer cnt;
    private List<DailyWeather> list;

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

    public List<DailyWeather> getList() {
        return list;
    }

    public void setList(List<DailyWeather> list) {
        this.list = list;
    }

    public ForecastResponse() {
    }

    public ForecastResponse(Integer cod, Double message, Integer cnt, List<DailyWeather> list) {
        this.cod = cod;
        this.message = message;
        this.cnt = cnt;
        this.list = list;
    }

    @Override
    public String toString() {
        return "ForecastResponse{" +
                "cod=" + cod +
                ", message=" + message +
                ", cnt=" + cnt +
                ", list=" + list +
                '}';
    }
}
