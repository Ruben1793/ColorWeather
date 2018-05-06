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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DATA = "data";
    public static final String CURRENTLY = "currently";
    public static final String SUMMARY = "summary";
    public static final String TIMEZONE = "timezone";
    public static final String HOURLY = "hourly";
    public static final String TIME = "time";
    public static final String DAILY = "daily";
    public static final String PRECIP_PROBABILITY = "precipProbability";
    public static final String TEMPERATURE = "temperature";
    public static final String TEMPERATURE_MAX = "temperatureMax";
    public static final String TEMPERATURE_MIN = "temperatureMin";

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

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.darksky.net/forecast/797573bdd40bc15d7f0536c8b663d042/37.8267,-122.4233?units=si";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                           // ArrayList<Day> days = getDailyWeatherFromJson(response);
                            ArrayList<Hour> hours = getHourlyWeaterFromJson(response);

                            for (Hour hour : hours) {
                                Log.d(TAG, hour.getTitle());
                                Log.d(TAG, hour.getWeatherDescription());
                            }

                            CurrentWeather currentWeather = getCurrentWeatherFromJson(response);
                            iconImageView.setImageDrawable(currentWeather.getIconDrawableResource());
                            descriptionTextView.setText(currentWeather.getDescription());
                            currentTempTextView.setText(currentWeather.getCurrentTemperature());
                            highestTempTextView.setText(String.format("H: %s°", currentWeather.getHighestTemperature()));
                            lowestTempTextView.setText(String.format("L: %s°", currentWeather.getLowestTemperature()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    private CurrentWeather getCurrentWeatherFromJson(String json) throws JSONException{
        JSONObject jsonObject = new JSONObject(json);
        JSONObject jsonWithCurrentWeather = jsonObject.getJSONObject(CURRENTLY);
        JSONObject jsonWithDailyWeather = jsonObject.getJSONObject(DAILY);
        JSONArray jsonWithDailyWeatherData = jsonWithDailyWeather.getJSONArray(DATA);
        JSONObject jsonWithTodayData = jsonWithDailyWeatherData.getJSONObject(0);

        String summary = jsonWithCurrentWeather.getString(SUMMARY);
        String icon = jsonWithCurrentWeather.getString("icon");
        String temperature = Math.round(jsonWithCurrentWeather.getDouble(TEMPERATURE)) + "";
        String maxTemperature = Math.round(jsonWithTodayData.getDouble(TEMPERATURE_MAX)) + "";
        String minTemperature = Math.round(jsonWithTodayData.getDouble(TEMPERATURE_MIN)) + "";

        CurrentWeather currentWeather = new CurrentWeather(MainActivity.this);
        currentWeather.setDescription(summary);
        currentWeather.setIconImage(icon);
        currentWeather.setCurrentTemperature(temperature);
        currentWeather.setHighestTemperature(maxTemperature);
        currentWeather.setLowestTemperature(minTemperature);

        return currentWeather;
    }

    private ArrayList<Day> getDailyWeatherFromJson(String json) throws JSONException{
        DateFormat dateFormat = new SimpleDateFormat("EEEE");
        ArrayList<Day> days = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);
        JSONObject jsonWithDailyWeather = jsonObject.getJSONObject(DAILY);
        JSONArray jsonWithDailyWeatherData = jsonWithDailyWeather.getJSONArray(DATA);

        for (int i = 0; i < jsonWithDailyWeatherData.length(); i++) {
            Day day = new Day();
            JSONObject jsonWithDayData = jsonWithDailyWeatherData.getJSONObject(i);

            String rainProbability = "Rain Probability: " + jsonWithDayData.getDouble(PRECIP_PROBABILITY) * 100 + "%";
            String description = jsonWithDayData.getString(SUMMARY);
            String dayName = dateFormat.format(jsonWithDayData.getDouble(TIME) * 1000);

            day.setDayName(dayName);
            day.setRainProbability(rainProbability);
            day.setWeatherDescription(description);

            days.add(day);
        }
        return days;
    }

    public ArrayList<Hour> getHourlyWeaterFromJson(String json) throws JSONException {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        ArrayList<Hour> hours = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);

        String timezone = jsonObject.getString(TIMEZONE);
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));

        JSONObject jsonWithHourlyWeather = jsonObject.getJSONObject(HOURLY);
        JSONArray jsonWithHourlyWeatherData = jsonWithHourlyWeather.getJSONArray(DATA);

        for (int i = 0; i < jsonWithHourlyWeatherData.length(); i++) {
            Hour hour = new Hour();
            JSONObject jsonWithHourData = jsonWithHourlyWeatherData.getJSONObject(i);

            String title = dateFormat.format(jsonWithHourData.getDouble(TIME) * 1000);
            String description = jsonWithHourData.getString(SUMMARY);

            hour.setTitle(title);
            hour.setWeatherDescription(description);

            hours.add(hour);
        }

        return hours;
    }
    
}
