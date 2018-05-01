package com.aldominium.colorweather;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.iconImageView) ImageView iconImageView;
    @BindView(R.id.descriptionTextView) TextView descriptionTextView;
    @BindView(R.id.currentTempTextView) TextView currentTempTextView;
    @BindView(R.id.highestTempTextView) TextView highestTempTextView;
    @BindView(R.id.lowestTempTextView) TextView lowestTempTextView;

    @BindDrawable(R.drawable.clear_night) Drawable clearNight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        CurrentWeather currentWeather = new CurrentWeather(MainActivity.this);
        currentWeather.setIconImage("cloudy");
        currentWeather.setDescription("Sunny Day");
        currentWeather.setCurrentTemperature("19");
        currentWeather.setHighestTemperature("H: 25");
        currentWeather.setLowestTemperature("L: 10");
        
        iconImageView.setImageDrawable(currentWeather.getIconDrawableResource());
        descriptionTextView.setText(currentWeather.getDescription());
        currentTempTextView.setText(currentWeather.getCurrentTemperature());
        highestTempTextView.setText(currentWeather.getHighestTemperature());
        lowestTempTextView.setText(currentWeather.getLowestTemperature());

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://www.google.com";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,"Response is: " + response.substring(0, 500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @OnClick(R.id.dailyWeatherTextView)
    public void dailyWeatherClick(){
        Intent dailyActivityIntent = new Intent(MainActivity.this, DailyWeatherActivity.class);
        startActivity(dailyActivityIntent);
    }

    @OnClick(R.id.hourlyWeatherTextView)
    public void hourlyWeatherClick(){
        Intent hourlyActivityIntent = new Intent(MainActivity.this, HourlyWeatherActivity.class);
        startActivity(hourlyActivityIntent);
    }

    @OnClick(R.id.minutelyWeatherTextView)
    public void minutelyWeatherClick(){
        Intent minutelyActivityIntent = new Intent(MainActivity.this, MinutelyWeatherActivity.class);
        startActivity(minutelyActivityIntent);
    }
}
