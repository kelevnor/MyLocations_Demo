package com.cvapplication.kelevnor.mylocations.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

import com.cvapplication.kelevnor.mylocations.models.objects.LocationList;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Marios Sifalakis on 8/27/17.
 */

public class UtilityHelperClass {

    public static void saveLocationListInSharedPreferences(LocationList state, Context con){
        SharedPreferences appSharedPrefs = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(state);
        prefsEditor.putString("locations", json);
        prefsEditor.commit();
    }

    public static LocationList getLocationListFromSharedPreferences(Context con){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(con);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("locations", "");
        LocationList locations = gson.fromJson(json, LocationList.class);
        return locations;
    }

    //Save String in SharedPreferences
    public static void saveIntegerInPreference(String intName, Integer intValue, Context con) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(con);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(intName, intValue);
        editor.commit();
    }
    //Get String from SharedPreferences
    public static Integer getIntegerFromPreference(String variable_name, Context con) {
        Integer preference_return;
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(con);
        preference_return = preferences.getInt(variable_name, 0);

        return preference_return;
    }

    //Method to detect internet availability
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
}
