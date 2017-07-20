package com.example.ideo7.weather.API;

import com.example.ideo7.weather.Model.ForecastDailyResponse;
import com.example.ideo7.weather.Model.ForecastHourlyResponse;
import com.example.ideo7.weather.Model.ForecastNowWeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ideo7 on 14.07.2017.
 */

public interface OpenWeather {

    @GET("weather")
    Call<ForecastNowWeatherResponse> getWeather(@Query("q") String q, @Query("appid") String appid, @Query("units") String units);
    @GET("forecast")
    Call<ForecastHourlyResponse> getForecast(@Query("q") String q, @Query("appid") String appid, @Query("units") String units, @Query("cnt") Integer cnt);
    @GET("forecast/daily")
    Call<ForecastDailyResponse> getForecastDaily(@Query("q") String q, @Query("appid") String appid, @Query("units") String units);
    @GET("forecast")
    Call<ForecastHourlyResponse> getForecastAll(@Query("q") String q, @Query("appid") String appid, @Query("units") String units);
    @GET("forecast")
    Call<ForecastHourlyResponse> getForecastId(@Query("id") Integer id, @Query("appid") String appid, @Query("units") String units, @Query("cnt") Integer cnt);
    @GET("forecast/daily")
    Call<ForecastDailyResponse> getForecastDailyId(@Query("id") Integer id, @Query("appid") String appid, @Query("units") String units);
    @GET("forecast")
    Call<ForecastHourlyResponse> getForecastAllId(@Query("id") Integer id, @Query("appid") String appid, @Query("units") String units);
}