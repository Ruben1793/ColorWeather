package com.aldominium.colorweather;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aldominium.colorweather.Models.Day;
import com.aldominium.colorweather.Models.Hour;
import com.aldominium.colorweather.Models.Minute;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

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

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DATA = "data";
    public static final String SUMMARY = "summary";
    public static final String ICON = "icon";
    public static final String TEMPERATURE = "temperature";
    public static final String TEMPERATURE_MAX = "temperatureMax";
    public static final String TEMPERATURE_MIN = "temperatureMin";
    public static final String CURRENTLY = "currently";
    public static final String DAILY = "daily";
    public static final String PRECIP_PROBABILITY = "precipProbability";
    public static final String HOURLY = "hourly";
    public static final String TIME = "time";
    public static final String MINUTELY = "minutely";
    public static final String TIMEZONE = "timezone";
    public static final String DAYS_ARRAY_LIST = "days";
    public static final String HOURS_ARRAY_LIST = "hours";
    public static final String MINUTES_ARRAY_LIST = "minutes_array_list";
    private static final int PERMISSION_ACCESS_FINE_LOCATION = 1;

    ArrayList<Day> days;
    ArrayList<Hour> hours;
    ArrayList<Minute> minutes;

    //@BindView(R.id.dailyWeatherTextView) TextView dailyWeatherTextView;
    @BindView(R.id.iconImageView)
    ImageView iconImageView;
    @BindView(R.id.descriptionTextView)
    TextView descriptionTextView;
    @BindView(R.id.currentTempTextView)
    TextView currentTempTextView;
    @BindView(R.id.highestTempTextView)
    TextView highestTempTextView;
    @BindView(R.id.lowestTempTextView)
    TextView lowestTempTextView;

    @BindDrawable(R.drawable.clear_night)
    Drawable clearNight;

    //String url = "https://api.darksky.net/forecast/797573bdd40bc15d7f0536c8b663d042/37.8267,-122.4233?units=si";

    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    private String mLatitude = "37.8267";
    private String mLongitude = "-122.4233";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode){
            case PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getMyLocation();
                } else {
                    /*ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSION_ACCESS_COARSE_LOCATION);*/
                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getMyLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this, "Connection Lost", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "Fallo la conexion, " + connectionResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.dailyWeatherTextView)
    public void DailyWeatherClick() {
        Intent dailyActivityIntent = new Intent(MainActivity.this, DailyWeatherActivity.class);
        dailyActivityIntent.putParcelableArrayListExtra(DAYS_ARRAY_LIST, days);
        startActivity(dailyActivityIntent);
    }

    @OnClick(R.id.hourlyWeatherTextView)
    public void HourlyWeatherClick() {
        Intent hourlyActivityIntent = new Intent(MainActivity.this, HourlyWeatherActivity.class);
        hourlyActivityIntent.putParcelableArrayListExtra(HOURS_ARRAY_LIST, hours);
        startActivity(hourlyActivityIntent);
    }

    @OnClick(R.id.minutelyWeatherTextView)
    public void MinutelyWeatherClick() {
        Intent minutelyActivityIntent = new Intent(MainActivity.this, MinutelyWeatherActivity.class);
        minutelyActivityIntent.putParcelableArrayListExtra(MINUTES_ARRAY_LIST, minutes);
        startActivity(minutelyActivityIntent);
    }

    @OnClick(R.id.refreshWeatherTextView)
    public void RefreshWeatherClick() {
        getMyLocation();
        Log.d(TAG, "LOCATION: " + mLatitude + " , " + mLongitude);
    }

    public void getWeather() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String foreCastURL = "https://api.darksky.net/forecast";
        String apiKey = "797573bdd40bc15d7f0536c8b663d042";
        String units = "units=si";
        String lan = "lan=en";

        final String urlForeCast = foreCastURL + "/" + apiKey + "/" + mLatitude + "," + mLongitude + "?" + units + "&" + lan;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlForeCast,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d(TAG, "URL: " + urlForeCast);
                            CurrentWeather currentWeather = getCurrentWeatherFromJson(response);

                            days = getDailyWeatherFromJson(response);
                            hours = getHourlyWeatherFromJson(response);
                            minutes = getMinutelyWeatherFromJson(response);

                            //for (Minute minute : minutes) {
                            //    Log.d(TAG,minute.getTitle());
                            //    Log.d(TAG,minute.getRainProbability());
                            //}

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
                Toast.makeText(MainActivity.this, "Connection Error", Toast.LENGTH_LONG).show();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private CurrentWeather getCurrentWeatherFromJson(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);

        JSONObject jsonWithCurrentWeather = jsonObject.getJSONObject(CURRENTLY);
        JSONObject jsonWithDailyWeather = jsonObject.getJSONObject(DAILY);
        JSONArray jsonWithDailyWeatherData = jsonWithDailyWeather.getJSONArray(DATA);
        JSONObject jsonWithTodayData = jsonWithDailyWeatherData.getJSONObject(0);

        String summary = jsonWithCurrentWeather.getString(SUMMARY);
        String icon = jsonWithCurrentWeather.getString(ICON);
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

    private ArrayList<Day> getDailyWeatherFromJson(String json) throws JSONException {
        DateFormat dateFormat = new SimpleDateFormat("EEEE");
        ArrayList<Day> days = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(json);

        String timezone = jsonObject.getString(TIMEZONE);
        dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));

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

    public ArrayList<Hour> getHourlyWeatherFromJson(String json) throws JSONException {
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

    public ArrayList<Minute> getMinutelyWeatherFromJson(String json) throws JSONException {

        if(json == null){
            return null;
        }

        else{
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            ArrayList<Minute> minutes = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(json);

            String timezone = jsonObject.getString(TIMEZONE);
            dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));

            if(jsonObject.isNull(MINUTELY)){
                return null;
            }
            JSONObject jsonWithMinutelyWeather = jsonObject.getJSONObject(MINUTELY);
            JSONArray jsonWithMinutelyWeatherData = jsonWithMinutelyWeather.getJSONArray(DATA);

            for (int i = 0; i < jsonWithMinutelyWeatherData.length(); i++) {
                Minute minute = new Minute();
                JSONObject jsonWithMinuteData = jsonWithMinutelyWeatherData.getJSONObject(i);

                String title = dateFormat.format(jsonWithMinuteData.getDouble(TIME) * 1000);
                String precipProbability = "Rain Probability: " + jsonWithMinuteData.getDouble(PRECIP_PROBABILITY) * 100 + "%";

                minute.setTitle(title);
                minute.setRainProbability(precipProbability);
                minutes.add(minute);
            }
            return minutes;
        }
    }

    private void getMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                /*
                Toast.makeText(MainActivity.this,"Location: " +
                                String.valueOf(mLastLocation.getLatitude()) + ", "
                                + String.valueOf(mLastLocation.getLongitude()),
                        Toast.LENGTH_LONG).show();
                            */
                mLatitude = String.valueOf(mLastLocation.getLatitude());
                mLongitude = String.valueOf(mLastLocation.getLongitude());

                Log.d("DEBUG", "current location: " + String.valueOf(mLastLocation.getLatitude())
                        + ", " + String.valueOf(mLastLocation.getLongitude()));
                getWeather();

            } else {
                Toast.makeText(MainActivity.this, "Need your permission!", Toast.LENGTH_LONG).show();
            }
        }
    }

}
