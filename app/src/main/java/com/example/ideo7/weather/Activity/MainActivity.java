package com.example.ideo7.weather.Activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ideo7.weather.API.OpenWeather;
import com.example.ideo7.weather.API.ServiceGenerator;
import com.example.ideo7.weather.Model.Convert;
import com.example.ideo7.weather.Model.ForecastNowWeatherResponse;
import com.example.ideo7.weather.R;
import com.example.ideo7.weather.Adapter.NowWeatherAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.searchEditText) EditText editText;
    @BindView(R.id.searchButton) Button button;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    private ArrayList<ForecastNowWeatherResponse> forecastNowWeatherResponses;
    private NowWeatherAdapter nowWeatherAdapter;
    private ArrayList<String> citys;
    private ArrayList<String> favoritescitys;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        forecastNowWeatherResponses = new ArrayList<>();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().isEmpty() ) {
                        searchWeather(editText.getText().toString());
                }
            }
        });
        citys = new ArrayList<>();
        favoritescitys = new ArrayList<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("json", null);
        if (json!=null){
            ArrayList<String> arrayList = gson.fromJson(json, String.class.getGenericSuperclass());
            for (int i=0;i<arrayList.size();i++){
                searchWeather(arrayList.get(i));
            }
            //citys.addAll(arrayList);
            favoritescitys.addAll(arrayList);
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        nowWeatherAdapter = new NowWeatherAdapter(forecastNowWeatherResponses,favoritescitys);
        recyclerView.setAdapter(nowWeatherAdapter);
    }

    public void searchWeather(final String city)
    {
        OpenWeather openWeather = ServiceGenerator.createService(OpenWeather.class);
        Call<ForecastNowWeatherResponse> call = openWeather.getWeather(city,getResources().getString(R.string.appid),getResources().getString(R.string.units), Convert.getlang());
        call.enqueue(new Callback<ForecastNowWeatherResponse>() {
            @Override
            public void onResponse(Call<ForecastNowWeatherResponse> call, retrofit2.Response<ForecastNowWeatherResponse> response) {
                if (response.isSuccessful()){
                    if (!citys.contains(response.body().getName())){
                        forecastNowWeatherResponses.add(response.body());
                        nowWeatherAdapter.notifyDataSetChanged();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"City is on the list.",Toast.LENGTH_SHORT).show();
                    }
                    citys.add(response.body().getName());

                }
            }

            @Override
            public void onFailure(Call<ForecastNowWeatherResponse> call, Throwable t) {
                Log.d("error",t.getLocalizedMessage());
            }
        });
    }
    @Override
    protected void onPause() {
        super.onPause();
        sharedEditor.putString("json",new Gson().toJson(favoritescitys));
        sharedEditor.commit();
    }
}
