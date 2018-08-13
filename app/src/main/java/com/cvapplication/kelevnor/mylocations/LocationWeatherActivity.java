package com.cvapplication.kelevnor.mylocations;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.cvapplication.kelevnor.mylocations.ADAPTER.ADAPTER_WeatherItem;
import com.cvapplication.kelevnor.mylocations.MODELS.locations_list.Location;
import com.cvapplication.kelevnor.mylocations.MODELS.weather.ConsolidatedWeather;
import com.cvapplication.kelevnor.mylocations.MODELS.weather.Weather;
import com.cvapplication.kelevnor.mylocations.MODELS.weather.WeatherWrap;
import com.cvapplication.kelevnor.mylocations.REST.PullWeatherData;
import com.cvapplication.kelevnor.mylocations.REST.REST_getWeather;
import com.google.gson.Gson;

import java.util.List;


public class LocationWeatherActivity extends AppCompatActivity implements ADAPTER_WeatherItem.onItemClickListener {
    android.support.v7.widget.RecyclerView lv;
    Weather weatherReturned = new Weather();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_weather);

        Intent i  = getIntent();
        String woeid = i.getStringExtra("woeid");

        Log.e("woeid", woeid);
        lv = findViewById(R.id.rv_weatherlog);

        REST_getWeather getWeather =  new REST_getWeather(woeid);
        getWeather.setOnResultListener(getWeatherAsync);
        getWeather.execute();

        //Retrofit approach, was getting an error and did not want to lose more time
//        new PullWeatherData(woeid, pullWeather);

    }

    PullWeatherData.OnAsyncResult pullWeather= new PullWeatherData.OnAsyncResult() {

        @Override
        public void onResultSuccess(int resultCode, Weather weather) {

            Log.e("Success", String.valueOf(weather.getConsolidatedWeather().size()));

            ADAPTER_WeatherItem listAdapter = new ADAPTER_WeatherItem(LocationWeatherActivity.this, weather.getConsolidatedWeather(), LocationWeatherActivity.this);
            lv.setAdapter(listAdapter);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(LocationWeatherActivity.this, LinearLayoutManager.VERTICAL, false);
            lv.setLayoutManager(mLayoutManager);
            lv.setItemAnimator(new DefaultItemAnimator());
            }

        @Override
        public void onResultFail(int resultCode, String errorResult) {
            Log.e("FAIL_RESPONSE", errorResult);
        }
    };

    REST_getWeather.OnAsyncResult getWeatherAsync= new REST_getWeather.OnAsyncResult() {

        @Override
        public void onResultSuccess(int resultCode, String result) {

            Log.e("Success", result);
            Gson gson = new Gson();
            Weather temp = gson.fromJson(result, Weather.class);
            ADAPTER_WeatherItem listAdapter = new ADAPTER_WeatherItem(LocationWeatherActivity.this, temp.getConsolidatedWeather(), LocationWeatherActivity.this);
            lv.setAdapter(listAdapter);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(LocationWeatherActivity.this, LinearLayoutManager.VERTICAL, false);
            lv.setLayoutManager(mLayoutManager);
            lv.setItemAnimator(new DefaultItemAnimator());
        }

        @Override
        public void onResultFail(int resultCode, String errorResult) {
            Log.e("Fail", errorResult);
        }
    };

    @Override
    public void onItemClick(Location location) {

    }
}
