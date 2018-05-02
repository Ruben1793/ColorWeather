package com.aldominium.colorweather;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class DailyWeatherActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_weather);

        ArrayList<String> daysArray = new ArrayList<>();
        daysArray.add("Lunes");
        daysArray.add("Martes");
        daysArray.add("Miercoles");
        daysArray.add("Jueves");
        daysArray.add("Viernes");
        daysArray.add("Sabado");
        daysArray.add("Domingo");

        ArrayAdapter<String> dailyWeatherAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, daysArray);

        setListAdapter(dailyWeatherAdapter);
    }
}
