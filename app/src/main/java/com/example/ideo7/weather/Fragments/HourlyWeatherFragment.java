package com.example.ideo7.weather.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ideo7.weather.API.OpenWeather;
import com.example.ideo7.weather.API.ServiceGenerator;
import com.example.ideo7.weather.Adapter.HourlyWeatherFragmentAdapter;
import com.example.ideo7.weather.Model.Convert;
import com.example.ideo7.weather.Model.ForecastHourlyResponse;
import com.example.ideo7.weather.Model.HourlyWeather;
import com.example.ideo7.weather.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by ideo7 on 17.07.2017.
 */

public class HourlyWeatherFragment extends Fragment{

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.title) TextView title;
    ArrayList<HourlyWeather> hourlyWeathers;
    HourlyWeatherFragmentAdapter hourlyWeatherAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hourly_weather, container,false);
        ButterKnife.bind(this,v);
        hourlyWeathers= new ArrayList<>();
        RecyclerView.LayoutManager hourlyLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(hourlyLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        hourlyWeatherAdapter = new HourlyWeatherFragmentAdapter(hourlyWeathers);
        recyclerView.setAdapter(hourlyWeatherAdapter);
        Intent intent = getActivity().getIntent();
        Log.d("log2",intent.getStringExtra("city"));

        if (intent.getIntExtra("idcity",0)!=0){
            getForecast(intent.getIntExtra("idcity",0));
        }
        else {
            Toast.makeText(getContext(), "Bad Id City", Toast.LENGTH_SHORT).show();
        }
        return v;
    }
    public void getForecast(Integer city){
        OpenWeather openWeather = ServiceGenerator.createService(OpenWeather.class);
        Call<ForecastHourlyResponse> call = openWeather.getForecastAllId(city,getResources().getString(R.string.appid),getResources().getString(R.string.units), Convert.getlang());
        call.enqueue(new Callback<ForecastHourlyResponse>() {
            @Override
            public void onResponse(Call<ForecastHourlyResponse> call, Response<ForecastHourlyResponse> response) {
                ArrayList<HourlyWeather> hws = (ArrayList<HourlyWeather>) response.body().getList();
                for (HourlyWeather hw : hws)
                    hourlyWeathers.add(hw);

                hourlyWeatherAdapter.notifyDataSetChanged();
                title.setText(String.format("Hourly weather and forecasts in %s,%s",response.body().getCity().getName(),response.body().getCity().getCountry()));

            }

            @Override
            public void onFailure(Call<ForecastHourlyResponse> call, Throwable t) {
                Log.d("log",t.getLocalizedMessage());
            }
        });
    }
}

