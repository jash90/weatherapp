package com.example.ideo7.weather.api;

import com.example.ideo7.weather.model.ForecastDailyResponse;
import com.example.ideo7.weather.model.ForecastHourlyResponse;
import com.example.ideo7.weather.model.ForecastNowWeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface OpenWeather {

    @GET("weather")
    Call<ForecastNowWeatherResponse> getWeather(@Query("q") String q, @Query("appid") String appid, @Query("units") String units, @Query("lang") String lang);

    @GET("forecast")
    Call<ForecastHourlyResponse> getForecast(@Query("q") String q, @Query("appid") String appid, @Query("units") String units, @Query("cnt") Integer cnt, @Query("lang") String lang);

    @GET("forecast/daily")
    Call<ForecastDailyResponse> getForecastDaily(@Query("q") String q, @Query("appid") String appid, @Query("units") String units, @Query("cnt") Integer cnt, @Query("lang") String lang);

    @GET("forecast/daily")
    Call<ForecastDailyResponse> getForecastDaily(@Query("q") String q, @Query("appid") String appid, @Query("units") String units, @Query("lang") String lang);

    @GET("forecast")
    Call<ForecastHourlyResponse> getForecastAll(@Query("q") String q, @Query("appid") String appid, @Query("units") String units, @Query("lang") String lang);
}