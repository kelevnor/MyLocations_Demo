package com.cvapplication.kelevnor.mylocations.Utils;

import com.cvapplication.kelevnor.mylocations.MODELS.locations_list.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kelevnor on 8/11/18.
 */

public class Config {

    public static List<Location> storedActivityLog = new ArrayList<>();
    public static List<Location> storedLocations = new ArrayList<>();
    public static Boolean availableCoordinates = true;
    public static Integer NOTIFY_UPDATE_INTERVAL = 60*1000;
    public static String metaweatherApi = "https://www.metaweather.com/";

    //Img urls
    public static String snowImg = "https://www.metaweather.com/static/img/weather/sn.svg";
    public static String sleetImg = "https://www.metaweather.com/static/img/weather/sl.svg";
    public static String hailImg = "https://www.metaweather.com/static/img/weather/h.svg";
    public static String thunderstormImg = "https://www.metaweather.com/static/img/weather/t.svg";
    public static String heavyRainImg = "https://www.metaweather.com/static/img/weather/hr.svg";
    public static String LightRainImg = "https://www.metaweather.com/static/img/weather/lr.svg";
    public static String showersImg = "https://www.metaweather.com/static/img/weather/s.svg";
    public static String heavyCloudImg = "https://www.metaweather.com/static/img/weather/hc.svg";
    public static String lightCloudImg = "https://www.metaweather.com/static/img/weather/lc.svg";
    public static String clearImg = "https://www.metaweather.com/static/img/weather/c.svg";




}



