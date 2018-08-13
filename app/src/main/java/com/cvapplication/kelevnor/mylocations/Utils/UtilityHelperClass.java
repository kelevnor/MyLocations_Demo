package com.cvapplication.kelevnor.mylocations.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.cvapplication.kelevnor.mylocations.MODELS.locations_list.Location;
import com.cvapplication.kelevnor.mylocations.MODELS.locations_list.LocationWrap;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Marios Sifalakis on 8/27/17.
 */

public class UtilityHelperClass {

    public static void saveLocationListInSharedPreferences(List<Location> loc, Context con){
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        LocationWrap locationWrap = new LocationWrap();
        locationWrap.setLocations(loc);
        String json = gson.toJson(locationWrap);
        prefsEditor.putString("locations", json);
        prefsEditor.commit();
    }

    public static List<Location> getLocationListFromSharedPreferences(Context con){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(con);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("locations", "");
        LocationWrap temp = gson.fromJson(json, LocationWrap.class);
        List<Location> tempLocations = temp.getLocations();
        return tempLocations;
    }

    //Save String in SharedPreferences
    public static void saveStringInPreference(String intName, String intValue, Context con) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(con);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(intName, intValue);
        editor.commit();
    }
    //Get String from SharedPreferences
    public static String getStringFromPreference(String variable_name, Context con) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(con);
        String mapTypeString = preferences.getString(variable_name, "NA");

        return mapTypeString;
    }

    //Method to detect internet availability
    public static boolean isNetworkAvailable(Context context) {

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {

            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                //Read byte from input stream

                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;

                //Write byte from output stream
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

    public static Double roundToDecimalPlaces(double value, int decimalPlaces)
    {
        Double shift = Math.pow(10,decimalPlaces);
        return Math.round(value*shift)/shift;
    }
}
