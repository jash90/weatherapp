package com.example.ideo7.weather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ideo7.weather.R;
import com.example.ideo7.weather.adapter.NowWeatherAdapter;
import com.example.ideo7.weather.api.OpenWeather;
import com.example.ideo7.weather.api.ServiceGenerator;
import com.example.ideo7.weather.model.Convert;
import com.example.ideo7.weather.model.ForecastNowWeatherResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.StringTokenizer;

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

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sharedEditor = sharedPreferences.edit();

        forecastNowWeatherResponses = new ArrayList<>();
        citys = new ArrayList<>();
        favoritescitys = new ArrayList<>();

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

        Gson gson = new Gson();
        String listFavoritesCitys = sharedPreferences.getString("json", null);
        if (listFavoritesCitys != null) {
            ArrayList<String> arrayList = gson.fromJson(listFavoritesCitys, String.class.getGenericSuperclass());
            for (String s : arrayList) {
                if (!citys.contains(s)) {
                    searchWeather(s);
                }
            }
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
                        return true;
                    }

                    @Override
                    public void onDismiss(View view) {
                        String text = ((TextView) view.findViewById(R.id.city)).getText().toString();
                        StringTokenizer stringTokenizer = new StringTokenizer(text, ",");
                        if (!text.isEmpty()) {
                            for (int i = 0; i < forecastNowWeatherResponses.size(); i++) {
                                if (forecastNowWeatherResponses.get(i).getName().equals(stringTokenizer.nextToken())
                                        && forecastNowWeatherResponses.get(i).getSys().getCountry().equals(stringTokenizer.nextToken())) {

                                    if (((CheckBox) view.findViewById(R.id.checked)).isChecked()) {
                                        Toast.makeText(getApplicationContext(), String.format(getString(R.string.removedFromFavorites), forecastNowWeatherResponses.get(i).getName()), Toast.LENGTH_SHORT).show();
                                    }
                                    text = forecastNowWeatherResponses.get(i).getName() + "," + forecastNowWeatherResponses.get(i).getSys().getCountry();
                                    forecastNowWeatherResponses.remove(i);
                                }
                            }

                            citys.remove(text);
                            favoritescitys.remove(text);
                            nowWeatherAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .setIsVertical(false)
                .setItemClickCallback(new SwipeDismissRecyclerViewTouchListener.OnItemClickCallBack() {
                    @Override
                    public void onClick(int position) {
                        if (position > -1) {
                            Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                            intent.putExtra("city", forecastNowWeatherResponses.get(position).getName());
                            intent.putExtra("country", forecastNowWeatherResponses.get(position).getSys().getCountry());
                            intent.putExtra("idcity", forecastNowWeatherResponses.get(position).getId());

                            Gson gson = new Gson();
                            String favorites = gson.toJson(favoritescitys);
                            sharedEditor.putString("city", forecastNowWeatherResponses.get(position).getName() + "," + forecastNowWeatherResponses.get(position).getSys().getCountry());
                            sharedEditor.apply();
                            sharedEditor.commit();
                            intent.putExtra("favorites", favorites);
                            startActivity(intent);
                        }
                    }
                }).create();
        recyclerView.setOnTouchListener(listener);

    }

    public void searchWeather(String city) {
        OpenWeather openWeather = ServiceGenerator.createService(OpenWeather.class);
        Call<ForecastNowWeatherResponse> call = openWeather.getWeather(city, getResources().getString(R.string.appid), getResources().getString(R.string.units), Convert.getlang());
        call.enqueue(new Callback<ForecastNowWeatherResponse>() {
            @Override
            public void onResponse(@NonNull Call<ForecastNowWeatherResponse> call, @NonNull retrofit2.Response<ForecastNowWeatherResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (!citys.contains(response.body().getName() + "," + response.body().getSys().getCountry())) {
                            forecastNowWeatherResponses.add(0, response.body());
                            nowWeatherAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.cityIsOnTheList, Toast.LENGTH_SHORT).show();
                        }
                        citys.add(response.body().getName() + "," + response.body().getSys().getCountry());

                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ForecastNowWeatherResponse> call, @NonNull Throwable t) {
                Log.d("error", t.getLocalizedMessage());
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        sharedEditor.putString("json", new Gson().toJson(favoritescitys));
        sharedEditor.putString("citys", new Gson().toJson(citys));
        sharedEditor.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sharedEditor.putString("citys", null);
        sharedEditor.commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        Gson gson = new Gson();
        String listCitys = sharedPreferences.getString("citys", null);
        if (listCitys != null) {
            ArrayList<String> arrayList = gson.fromJson(listCitys, String.class.getGenericSuperclass());
            for (int i = 0; i < arrayList.size(); i++) {
                searchWeather(arrayList.get(i));
            }
            citys.addAll(arrayList);
        }

    }
}
