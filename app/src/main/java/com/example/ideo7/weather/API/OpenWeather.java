package com.example.ideo7.weather.API;

import com.example.ideo7.weather.Model.ForecastResponse;
import com.example.ideo7.weather.Model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ideo7 on 14.07.2017.
 */

public interface OpenWeather {

    @GET("weather")
    Call<WeatherResponse> getWeather(@Query("q") String q, @Query("appid") String appid, @Query("units") String units);
    @GET("forecast")
    Call<ForecastResponse> getForecast(@Query("q") String q, @Query("appid") String appid, @Query("units") String units,@Query("cnt") Integer cnt);

}