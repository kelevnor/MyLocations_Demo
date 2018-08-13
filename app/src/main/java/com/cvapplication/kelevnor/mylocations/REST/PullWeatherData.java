package com.cvapplication.kelevnor.mylocations.REST;

import android.util.Log;

import com.cvapplication.kelevnor.mylocations.MODELS.weather.Weather;
import com.cvapplication.kelevnor.mylocations.Utils.Config;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kelevnor on 8/13/18.
 * Not used to crash encounter that I did not have time to fix
 * It is a minor issue
 */

public class PullWeatherData{

    OnAsyncResult onAsyncResult;
    String woeid;
    Weather weatherRes = new Weather();

    public PullWeatherData(String woeid, final OnAsyncResult onAsyncResult){
        this.onAsyncResult = onAsyncResult;

        this.woeid = woeid;

        Log.e("",Config.metaweatherApi+"api/location/"+woeid+"/");
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.metaweatherApi)
                .addConverterFactory(GsonConverterFactory.create()).build();
        PullMarketCapData marketCapData = retrofit.create(PullMarketCapData.class);

        Call<Weather> call= marketCapData.getCapData(woeid);
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {

                weatherRes = response.body();
//                Gson gson = new Gson();
//                Weather locationWrap = new Weather();
//                String json = gson.toJson(locationWrap);
//                Log.e("weatherRes", json);

                onAsyncResult.onResultSuccess(1, weatherRes);
            }
            @Override
            public void onFailure(Call<Weather> call, Throwable t){
                onAsyncResult.onResultFail(0, t.getMessage());
            }
        });

    }

    //This is our Interface for the RetrofitCall
    public interface PullMarketCapData{
        @GET("api/location/{woeid}/")
        Call<Weather> getCapData(@Query("woeid") String woeid);
    }

    //This is our Interface for listener on Activity
    public interface OnAsyncResult {
        void onResultSuccess(int resultCode, Weather weather);
        void onResultFail(int resultCode, String errorMessage);
    }

}

