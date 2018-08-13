
package com.cvapplication.kelevnor.mylocations.MODELS.weather;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherWrap {

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    @SerializedName("weather")
    @Expose
    private Weather weather = null;

}
