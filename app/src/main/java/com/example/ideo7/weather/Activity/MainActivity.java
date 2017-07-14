package com.example.ideo7.weather.Activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ideo7.weather.API.OpenWeather;
import com.example.ideo7.weather.API.ServiceGenerator;
import com.example.ideo7.weather.Model.Responde;
import com.example.ideo7.weather.R;
import com.example.ideo7.weather.RespondeAdapter;

import java.util.ArrayList;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;
    private RecyclerView recyclerView;
    private ArrayList<Responde> respondes;
    private RespondeAdapter respondeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.searchButton);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        respondes = new ArrayList<>();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editText.getText().toString().isEmpty())
                searchWeather(editText.getText().toString());
            }
        });
        searchWeather("London");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        respondeAdapter = new RespondeAdapter(respondes);
        recyclerView.setAdapter(respondeAdapter);


    }
    public void searchWeather(String city)
    {
        OpenWeather openWeather = ServiceGenerator.createService(OpenWeather.class);
        Call<Responde> call = openWeather.getWeather(city,getResources().getString(R.string.appid),getResources().getString(R.string.units));
        call.enqueue(new Callback<Responde>() {
            @Override
            public void onResponse(Call<Responde> call, retrofit2.Response<Responde> response) {
                if (response.isSuccessful()){
                    Log.d("error","succes");
                    respondes.add(response.body());
                    respondeAdapter.notifyDataSetChanged();
                    recyclerView.refreshDrawableState();
                    Log.d("error",respondes.toString());
                }
            }

            @Override
            public void onFailure(Call<Responde> call, Throwable t) {
                Log.d("error",t.getLocalizedMessage());
            }
        });
    }
}
