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
import com.example.ideo7.weather.Model.WeatherResponse;
import com.example.ideo7.weather.R;
import com.example.ideo7.weather.RespondeAdapter;
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
    private ArrayList<WeatherResponse> weatherResponses;
    private RespondeAdapter respondeAdapter;
    private ArrayList<String> citys;
    private ArrayList<String> favoritescitys;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        weatherResponses = new ArrayList<>();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().isEmpty() ) {
                    if (!citys.contains(editText.getText().toString())) {
                        searchWeather(editText.getText().toString());
                    } else {
                        Toast.makeText(v.getContext(),"City is on the list.",Toast.LENGTH_SHORT).show();}
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
            favoritescitys.addAll(arrayList);
            for (String city : favoritescitys){
                searchWeather(city);
                citys.add(city);
            }
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        respondeAdapter = new RespondeAdapter(weatherResponses,favoritescitys);
        recyclerView.setAdapter(respondeAdapter);
    }

    public void searchWeather(String city)
    {
        OpenWeather openWeather = ServiceGenerator.createService(OpenWeather.class);
        Call<WeatherResponse> call = openWeather.getWeather(city,getResources().getString(R.string.appid),getResources().getString(R.string.units));
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, retrofit2.Response<WeatherResponse> response) {
                if (response.isSuccessful()){
                    weatherResponses.add(response.body());
                    respondeAdapter.notifyDataSetChanged();
                    recyclerView.refreshDrawableState();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
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
