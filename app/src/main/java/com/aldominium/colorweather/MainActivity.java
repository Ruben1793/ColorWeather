package com.aldominium.colorweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    public static final String TAG = MainActivity.class.getSimpleName();

   private TextView dailyWatherTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dailyWatherTextView = findViewById(R.id.dailyWeatherTextView);

        dailyWatherTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dailyActivityIntent = new Intent(MainActivity.this, DailyWeatherActivity.class);
                startActivity(dailyActivityIntent);
            }
        });
    }
}
