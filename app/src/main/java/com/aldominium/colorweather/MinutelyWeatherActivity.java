package com.aldominium.colorweather;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aldominium.colorweather.Adapters.MinutelyWeatherAdapter;
import com.aldominium.colorweather.Models.Minute;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MinutelyWeatherActivity extends Activity {

    @BindView(R.id.minutelyRecyclerView) RecyclerView minutelyRecyclerView;
    @BindView(R.id.minutelyNoDataTextView) TextView minutelyNoDataTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minutely_weather);
        ButterKnife.bind(this);

        ArrayList<Minute> minutes = getIntent().getParcelableArrayListExtra(MainActivity.MINUTES_ARRAY_LIST);

        if (minutes != null && !minutes.isEmpty()){
            minutelyRecyclerView.setVisibility(View.VISIBLE);
            minutelyNoDataTextView.setVisibility(View.GONE);

            MinutelyWeatherAdapter minutelyWeatherAdapter = new MinutelyWeatherAdapter(this, minutes);
            minutelyRecyclerView.setAdapter(minutelyWeatherAdapter);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            minutelyRecyclerView.setLayoutManager(layoutManager);

            minutelyRecyclerView.setHasFixedSize(true);
        }else{
            minutelyRecyclerView.setVisibility(View.GONE);
            minutelyNoDataTextView.setVisibility(View.VISIBLE);
        }

    }
}
