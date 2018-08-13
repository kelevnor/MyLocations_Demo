package com.cvapplication.kelevnor.mylocations.REST;

import com.cvapplication.kelevnor.mylocations.MODELS.locations_list.Location;
import com.cvapplication.kelevnor.mylocations.Utils.Config;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by kelevnor on 2/7/18.
 */

public class PullLocationQueryData {

    OnAsyncResult onAsyncResult;


    public PullLocationQueryData(String query, final OnAsyncResult onAsyncResult){
        this.onAsyncResult = onAsyncResult;

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.metaweatherApi)
                .addConverterFactory(GsonConverterFactory.create()).build();
        PullMarketCapData marketCapData = retrofit.create(PullMarketCapData.class);

        Call<List<Location>> call= marketCapData.getCapData(query);
        call.enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                List<Location> capList = response.body();
                onAsyncResult.onResultSuccess(1, capList);
            }
            @Override
            public void onFailure(Call<List<Location>> call, Throwable t){
                onAsyncResult.onResultFail(0, t.getMessage());
            }
        });

    }

    //This is our Interface for the RetrofitCall
    public interface PullMarketCapData{
        @GET("api/location/search/")
        Call<List<Location>> getCapData(@Query("query") String  query);
    }

    //This is our Interface for listener on Activity
    public interface OnAsyncResult {
        void onResultSuccess(int resultCode, List<Location> locations);
        void onResultFail(int resultCode, String errorMessage);
    }
}

