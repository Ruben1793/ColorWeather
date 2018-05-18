package com.aldominium.colorweather;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.aldominium.colorweather.Adapters.HourlyWeatherAdapter;
import com.aldominium.colorweather.Models.Hour;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HourlyWeatherActivity extends Activity {

    @BindView(R.id.hourlyListView) ListView hourlyListView;
    @BindView(R.id.hourlyNoDataTextView) TextView NoDataTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hourly_weather);
        ButterKnife.bind(this);

        ArrayList<Hour> hours = getIntent().getParcelableArrayListExtra(MainActivity.HOURS_ARRAY_LIST);

        HourlyWeatherAdapter hourlyWeatherAdapter = new HourlyWeatherAdapter(this, hours);
        hourlyListView.setAdapter(hourlyWeatherAdapter);

        hourlyListView.setEmptyView(NoDataTextView);
    }
}
