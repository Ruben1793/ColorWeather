package com.aldominium.colorweather;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.aldominium.colorweather.Adapters.MinutelyWeatherAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MinutelyWeatherActivity extends Activity {

    @BindView(R.id.minutelyRecyclerView) RecyclerView minutelyRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minutely_weather);
        ButterKnife.bind(this);

        ArrayList<Minute> minutes = new ArrayList<>();
        Minute minute = new Minute();
        minute.setTitle("17:55");
        minute.setRainProbability("99%");

        Minute minute2 = new Minute();
        minute2.setTitle("20:55");
        minute2.setRainProbability("99%");

        Minute minute3 = new Minute();
        minute3.setTitle("20:55");
        minute3.setRainProbability("99%");

        minutes.add(minute);
        minutes.add(minute2);
        minutes.add(minute3);

        MinutelyWeatherAdapter minutelyWeatherAdapter = new MinutelyWeatherAdapter(this, minutes);
        minutelyRecyclerView.setAdapter(minutelyWeatherAdapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        minutelyRecyclerView.setLayoutManager(layoutManager);

        minutelyRecyclerView.setHasFixedSize(true);
    }
}
