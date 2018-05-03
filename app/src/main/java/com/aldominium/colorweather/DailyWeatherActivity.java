package com.aldominium.colorweather;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.aldominium.colorweather.Adapters.DailyWeatherAdapter;

import java.util.ArrayList;

public class DailyWeatherActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_weather);

        ArrayList<Day> days = new ArrayList<>();
        for (int i=0 ; i<500 ; i++){
            Day day = new Day();
            day.setDayName("Monday");
            day.setWeatherDescription("Its going to rain");
            day.setRainProbability("Rain probability 99%");
            days.add(day);
        }
        DailyWeatherAdapter dailyWeatherAdapter =
                new DailyWeatherAdapter(this, days);
        setListAdapter(dailyWeatherAdapter);
    }
}
