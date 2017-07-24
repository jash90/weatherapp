package com.example.ideo7.weather.Activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ideo7.weather.Fragments.ChartFragment;
import com.example.ideo7.weather.Fragments.DailyWeatherFragment;
import com.example.ideo7.weather.Fragments.HourlyWeatherFragment;
import com.example.ideo7.weather.Fragments.MainWeatherFragment;
import com.example.ideo7.weather.Model.City;
import com.example.ideo7.weather.Model.Clouds;
import com.example.ideo7.weather.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.yanzm.mth.MaterialTabHost;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsActivity extends AppCompatActivity {
    @BindView(R.id.tabHost) MaterialTabHost tabHost;
    @BindView(R.id.viewPager) ViewPager viewPager;
    @BindView(R.id.toolbar) Toolbar toolbar;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.menu,null));
        }
        else{
            toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.menu));
        }
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                sharedEditor.putString("city",item.toString());
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
        sharedPreferences = getSharedPreferences("PREF",MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();

        Intent intent = getIntent();
        sharedEditor.putString("city",intent.getStringExtra("city")+","+intent.getStringExtra("country"));
        sharedEditor.commit();
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(sharedPreferences.getString("city",null));
        Gson gson = new Gson();
        String favorites = intent.getStringExtra("favorites");
        ArrayList<String> lista = gson.fromJson(favorites,String.class.getGenericSuperclass());
        Log.d("s",lista.toString());
        Log.d("s",favorites);
        for(String s: lista){
            menu.add(s);
        }
        getSupportActionBar().hide();
        //setTitle(intent.getStringExtra("city")+","+intent.getStringExtra("country"));

        tabHost.setType(MaterialTabHost.Type.FullScreenWidth);
//        tabHost.setType(MaterialTabHost.Type.Centered);
//        tabHost.setType(MaterialTabHost.Type.LeftOffset);

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

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position)
            {
                case 0: return new MainWeatherFragment();
                case 1: return new DailyWeatherFragment();
                case 2: return new HourlyWeatherFragment();
                case 3: return new ChartFragment();
                default: return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
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

