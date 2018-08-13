package com.cvapplication.kelevnor.mylocations.Service;

/**
 * Created by kelevnor on 2/7/18.
 * Service to keep updating marketCap data every 20 seconds
 */

import android.app.Service;
import android.content.*;
import android.os.*;
import android.util.Log;

import com.cvapplication.kelevnor.mylocations.REST.PullLocationData;
import com.cvapplication.kelevnor.mylocations.Utils.Config;
import com.cvapplication.kelevnor.mylocations.MODELS.locations_list.Location;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Service_StoreLocation extends Service{
    private Timer mTimer = null;
    OnAsyncResult onAsyncResult;
    String convert = "usd";
    private final IBinder mBinder = new LocalBinder();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mTimer!=null){
            mTimer.cancel();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        if(mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, Config.NOTIFY_UPDATE_INTERVAL);
    }

    public void setOnResultListener(OnAsyncResult onAsyncResult){
        this.onAsyncResult = onAsyncResult;
    }

    public void setFiat(String convert){
        this.convert = convert;
        if(mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, Config.NOTIFY_UPDATE_INTERVAL);
    }

    class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
//            if(UtilityHelper.isInternetAvailableNoContext()){
//                if(Config.availableCoordinates){
//                    new PullData(Config.limit, convert, pullDataListener);
//                }
//                else{
//                    new PullData(Config.limit, convert, pullDataListener);
//                }
//
//            }
//            else{
//                mTimer.cancel();
//                onAsyncResult.onResultInternetFail(0, "INTERNET FAIL");
//            }
        }
    }

    //On result listener for PullData Api Call
    PullLocationData.OnAsyncResult pullDataListener= new PullLocationData.OnAsyncResult() {
        @Override
        public void onResultSuccess(int resultCode, List<Location> coinsList) {

//            Config.coinsList.clear();
//            Config.coinsList.addAll(coinsList);
            onAsyncResult.onResultRefresh(1, "REFRESH SUCCESS");
        }
        @Override
        public void onResultFail(int resultCode, String errorResult) {
            Log.e("FAIL_RESPONSE", errorResult);
            onAsyncResult.onResultServerFail(0, "FAIL SERVER");
        }
    };

    //returns the instance of the service
    public class LocalBinder extends Binder{
        public Service_StoreLocation getServiceInstance(){
            return Service_StoreLocation.this;
        }
    }

    //This is our Interface for listener on Activity
    public interface OnAsyncResult {
        void onResultRefresh(int resultCode, String coinList);
        void onResultInternetFail(int resultCode, String errorMessage);
        void onResultServerFail(int resultCode, String errorMessage);
    }
}