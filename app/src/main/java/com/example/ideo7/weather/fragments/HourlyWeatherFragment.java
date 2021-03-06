package com.example.ideo7.weather.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ideo7.weather.R;
import com.example.ideo7.weather.adapter.HourlyWeatherHourlyFragmentAdapter;
import com.example.ideo7.weather.api.OpenWeather;
import com.example.ideo7.weather.api.ServiceGenerator;
import com.example.ideo7.weather.model.Convert;
import com.example.ideo7.weather.model.ForecastHourlyResponse;
import com.example.ideo7.weather.model.HourlyWeather;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HourlyWeatherFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.title)
    TextView title;
    ArrayList<HourlyWeather> hourlyWeathers;
    HourlyWeatherHourlyFragmentAdapter hourlyWeatherAdapter;
    private SharedPreferences sharedPreferences;
    private IntentFilter intentFilter = new IntentFilter("menu");
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            sharedPreferences = getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);
            hourlyWeathers = new ArrayList<>();
            RecyclerView.LayoutManager hourlyLayoutManager = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(hourlyLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            hourlyWeatherAdapter = new HourlyWeatherHourlyFragmentAdapter(hourlyWeathers);
            recyclerView.setAdapter(hourlyWeatherAdapter);

            if (sharedPreferences.getString("city", null) != null) {
                getForecast(sharedPreferences.getString("city", null));
            } else {
                Toast.makeText(getContext(), getResources().getString(R.string.emptyCity), Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hourly_weather, container, false);
        ButterKnife.bind(this, v);
        setHasOptionsMenu(true);
        sharedPreferences = getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);
        hourlyWeathers = new ArrayList<>();
        RecyclerView.LayoutManager hourlyLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(hourlyLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        hourlyWeatherAdapter = new HourlyWeatherHourlyFragmentAdapter(hourlyWeathers);
        recyclerView.setAdapter(hourlyWeatherAdapter);

        if (sharedPreferences.getString("city", null) != null) {
            getForecast(sharedPreferences.getString("city", null));
        } else {
            Toast.makeText(getContext(), getContext().getString(R.string.emptyCity), Toast.LENGTH_SHORT).show();
        }
        return v;
    }

    public void getForecast(String city) {
        OpenWeather openWeather = ServiceGenerator.createService(OpenWeather.class);
        Call<ForecastHourlyResponse> call = openWeather.getForecastAll(city, getResources().getString(R.string.appid), getResources().getString(R.string.units), Convert.getlang());
        call.enqueue(new Callback<ForecastHourlyResponse>() {
            @Override
            public void onResponse(@NonNull Call<ForecastHourlyResponse> call, @NonNull Response<ForecastHourlyResponse> response) {
                if (response.body() != null && response.body().getList() != null && response.body().getCity().getName()!=null && response.body().getCity().getCountry()!=null) {
                    ArrayList<HourlyWeather> hws = (ArrayList<HourlyWeather>) response.body().getList();
                    for (HourlyWeather hw : hws) {
                        hourlyWeathers.add(hw);
                    }
                    hourlyWeatherAdapter.notifyDataSetChanged();

                    title.setText(String.format(getString(R.string.hourlyWeatherAndForecastsIn), response.body().getCity().getName() + "," + response.body().getCity().getCountry()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForecastHourlyResponse> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(broadcastReceiver, intentFilter);
    }


    @Override
    public void onPause() {
        getActivity().unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

}

