package com.example.ideo7.weather.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.ideo7.weather.R;
import com.example.ideo7.weather.fragments.ChartFragment;
import com.example.ideo7.weather.fragments.DailyWeatherFragment;
import com.example.ideo7.weather.fragments.HourlyWeatherFragment;
import com.example.ideo7.weather.fragments.MainWeatherFragment;
import com.google.gson.Gson;

import net.yanzm.mth.MaterialTabHost;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.tabHost)
    MaterialTabHost tabHost;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor;
    private SectionsPagerAdapter pagerAdapter;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        menu = toolbar.getMenu();

        toolbar.setBackgroundResource(R.color.colorPrimary);
        toolbar.setOverflowIcon(ContextCompat.getDrawable(this,R.drawable.menu));
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sharedEditor.putString("city", item.toString());
                sharedEditor.commit();
                Intent intent = new Intent();
                intent.setAction("menu");
                sendBroadcast(intent);
                toolbar.setTitle(item.toString());
                //finish();
                //startActivity(getIntent());
                return true;
            }
        });

        sharedPreferences = getSharedPreferences("PREF", MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();

        Intent intent = getIntent();
        sharedEditor.putString("city", intent.getStringExtra("city") + "," + intent.getStringExtra("country"));
        sharedEditor.commit();

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(sharedPreferences.getString("city", null));

        Gson gson = new Gson();
        String favorites = intent.getStringExtra("favorites");
        ArrayList<String> lista = gson.fromJson(favorites, String.class.getGenericSuperclass());
        Log.d("s", lista.toString());
        Log.d("s", favorites);
        for (String s : lista) {
            menu.add(s);
        }

        getSupportActionBar().hide();

        tabHost.setType(MaterialTabHost.Type.FullScreenWidth);
        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            tabHost.addTab(pagerAdapter.getPageTitle(i));
        }

        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(tabHost);
        tabHost.setOnTabChangeListener(new MaterialTabHost.OnTabChangeListener() {
            @Override
            public void onTabSelected(int position) {
                viewPager.setCurrentItem(position);
            }
        });

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MainWeatherFragment();
                case 1:
                    return new DailyWeatherFragment();
                case 2:
                    return new HourlyWeatherFragment();
                case 3:
                    return new ChartFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getResources().getString(R.string.main).toUpperCase(l);
                case 1:
                    return getResources().getString(R.string.daily).toUpperCase(l);
                case 2:
                    return getResources().getString(R.string.hourly).toUpperCase(l);
                case 3:
                    return getResources().getString(R.string.chart).toUpperCase(l);
            }
            return null;
        }
    }

}

