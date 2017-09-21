package com.cvapplication.kelevnor.mylocations.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.cvapplication.kelevnor.mylocations.R;

/**
 * Created by kelevnor on 9/20/17.
 */

public class MarkersClass {
    public static Drawable GREEN_MARKER(Context context){
        return context.getResources().getDrawable(R.drawable.ic_location_on_green_48dp);
    }
    public static Drawable BLUE_MARKER(Context context){
        return context.getResources().getDrawable(R.drawable.ic_location_on_blue_48dp);
    }
}
