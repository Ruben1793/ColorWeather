package com.aldominium.colorweather;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.aldominium.colorweather.Adapters.HourlyWeatherAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HourlyWeatherActivity extends Activity {

    @BindView(R.id.hourlyListView) ListView hourlyListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_weather);
        ButterKnife.bind(this);

        Hour hour = new Hour();
        hour.setTitle("11:00pm");
        hour.setWeatherDescription("There is a storm!");

        Hour hour2 = new Hour();
        hour2.setTitle("12:00pm");
        hour2.setWeatherDescription("There iss still a storm!");

        ArrayList<Hour> hours = new ArrayList<>();
        hours.add(hour);
        hours.add(hour2);

        HourlyWeatherAdapter hourlyWeatherAdapter = new HourlyWeatherAdapter(this, hours);
        hourlyListView.setAdapter(hourlyWeatherAdapter);
    }
}
