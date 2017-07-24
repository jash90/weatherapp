package com.example.ideo7.weather.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ideo7.weather.API.OpenWeather;
import com.example.ideo7.weather.API.ServiceGenerator;
import com.example.ideo7.weather.Model.City;
import com.example.ideo7.weather.Model.Convert;
import com.example.ideo7.weather.Model.ForecastDailyResponse;
import com.example.ideo7.weather.Model.ForecastNowWeatherResponse;
import com.example.ideo7.weather.R;
import com.example.ideo7.weather.Adapter.NowWeatherAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.codefalling.recyclerviewswipedismiss.SwipeDismissRecyclerViewTouchListener;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.searchEditText)
    EditText editText;
    @BindView(R.id.searchButton)
    Button button;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
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
                if (!editText.getText().toString().isEmpty()) {
                    searchWeather(editText.getText().toString());
                    editText.requestFocus();
                    Convert.hideSoftKeyboard(MainActivity.this);
                }
            }
        });
        citys = new ArrayList<>();
        favoritescitys = new ArrayList<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedEditor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("json", null);
        if (json != null) {
            ArrayList<String> arrayList = gson.fromJson(json, String.class.getGenericSuperclass());
            for (int i = 0; i < arrayList.size(); i++) {
                searchWeather(arrayList.get(i));
            }
            //citys.addAll(arrayList);
            favoritescitys.addAll(arrayList);
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        nowWeatherAdapter = new NowWeatherAdapter(forecastNowWeatherResponses, favoritescitys);
        recyclerView.setAdapter(nowWeatherAdapter);
        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(
                recyclerView,
                new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        //recyclerView.removeViewAt(position);
                        return true;
                    }

                    @Override
                    public void onDismiss(View view) {
                        String text = ((TextView) view.findViewById(R.id.city)).getText().toString();
                        for (int i = 0; i < forecastNowWeatherResponses.size(); i++) {
                            if (forecastNowWeatherResponses.get(i).getName().equals(text)) {
                                forecastNowWeatherResponses.remove(i);
                                if (((CheckBox) view.findViewById(R.id.checked)).isChecked())
                                    Toast.makeText(getApplicationContext(),R.string.removedFromFavorites, Toast.LENGTH_SHORT).show();
                            }
//                            Log.d("ondissmis", String.valueOf(forecastNowWeatherResponses.get(i).equals(((TextView)view.findViewById(R.id.city)).getText().toString())));
                        }
                        citys.remove(text);
                        favoritescitys.remove(text);
                        nowWeatherAdapter.notifyDataSetChanged();
                        Log.d("ondissmis", forecastNowWeatherResponses.toString());
                        Log.d("ondissmis", ((TextView) view.findViewById(R.id.city)).getText().toString());

                    }
                })
                .setIsVertical(false)
                .setItemTouchCallback(
                        new SwipeDismissRecyclerViewTouchListener.OnItemTouchCallBack() {
                            @Override
                            public void onTouch(int index) {
                                // Do what you want when item be touched
                            }
                        })
                .setItemClickCallback(new SwipeDismissRecyclerViewTouchListener.OnItemClickCallBack() {
                    @Override
                    public void onClick(int position) {
                        Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                        intent.putExtra("city", forecastNowWeatherResponses.get(position).getName());
                        intent.putExtra("country", forecastNowWeatherResponses.get(position).getSys().getCountry());
                        intent.putExtra("idcity", forecastNowWeatherResponses.get(position).getId());
                        Gson gson = new Gson();
                        String favorites = gson.toJson(favoritescitys);
                        sharedEditor.putString("city",forecastNowWeatherResponses.get(position).getName()+","+forecastNowWeatherResponses.get(position).getSys().getCountry());
                        sharedEditor.commit();
                        intent.putExtra("favorites", favorites);
                        startActivity(intent);
                    }
                }).create();
        recyclerView.setOnTouchListener(listener);

    }

    public void searchWeather(Integer city) {
        OpenWeather openWeather = ServiceGenerator.createService(OpenWeather.class);
        Call<ForecastNowWeatherResponse> call = openWeather.getWeather(city, getResources().getString(R.string.appid), getResources().getString(R.string.units), Convert.getlang());
        call.enqueue(new Callback<ForecastNowWeatherResponse>() {
            @Override
            public void onResponse(Call<ForecastNowWeatherResponse> call, retrofit2.Response<ForecastNowWeatherResponse> response) {
                if (response.isSuccessful()) {
                    if (!citys.contains(response.body().getName()+","+response.body().getSys().getCountry())) {
                        forecastNowWeatherResponses.add(0, response.body());
                        nowWeatherAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(),R.string.cityIsOnTheList, Toast.LENGTH_SHORT).show();
                    }
                    citys.add(response.body().getName() + "," + response.body().getSys().getCountry());

                }
            }

            @Override
            public void onFailure(Call<ForecastNowWeatherResponse> call, Throwable t) {
                Log.d("error", t.getLocalizedMessage());
            }
        });
    }

    public void searchWeather(String city) {
        OpenWeather openWeather = ServiceGenerator.createService(OpenWeather.class);
        Call<ForecastNowWeatherResponse> call = openWeather.getWeather(city, getResources().getString(R.string.appid), getResources().getString(R.string.units), Convert.getlang());
        call.enqueue(new Callback<ForecastNowWeatherResponse>() {
            @Override
            public void onResponse(Call<ForecastNowWeatherResponse> call, retrofit2.Response<ForecastNowWeatherResponse> response) {
                if (response.isSuccessful()) {
                    if (!citys.contains(response.body().getName()+","+response.body().getSys().getCountry())) {
                        forecastNowWeatherResponses.add(0, response.body());
                        nowWeatherAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.cityIsOnTheList, Toast.LENGTH_SHORT).show();
                    }
                    citys.add(response.body().getName() + "," + response.body().getSys().getCountry());

                }
            }

            @Override
            public void onFailure(Call<ForecastNowWeatherResponse> call, Throwable t) {
                Log.d("error", t.getLocalizedMessage());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        sharedEditor.putString("json", new Gson().toJson(favoritescitys));
        sharedEditor.commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
