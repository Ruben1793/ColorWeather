package com.aldominium.colorweather;

import android.app.ListActivity;
import android.os.Bundle;

import com.aldominium.colorweather.Adapters.DailyWeatherAdapter;

import java.util.ArrayList;

public class DailyWeatherActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_weather);

        ArrayList<Day> days = getIntent().getParcelableArrayListExtra(MainActivity.DAYS_ARRAY_LIST);

        DailyWeatherAdapter dailyWeatherAdapter = new DailyWeatherAdapter(this, days);
        setListAdapter(dailyWeatherAdapter);
    }
}
