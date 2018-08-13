package com.cvapplication.kelevnor.mylocations.ADAPTER;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvapplication.kelevnor.mylocations.ImageLoader.ImageLoader;
import com.cvapplication.kelevnor.mylocations.MODELS.locations_list.Location;
import com.cvapplication.kelevnor.mylocations.MODELS.weather.ConsolidatedWeather;
import com.cvapplication.kelevnor.mylocations.MODELS.weather.Weather;
import com.cvapplication.kelevnor.mylocations.R;
import com.cvapplication.kelevnor.mylocations.Utils.Config;
import com.cvapplication.kelevnor.mylocations.Utils.UtilityHelperClass;

import java.util.List;

/**
 * Created by kelevnor on 1/18/18.
 */

public class ADAPTER_WeatherItem extends RecyclerView.Adapter<ADAPTER_WeatherItem.ViewHolder> {
    private List<ConsolidatedWeather> searchList;
    onItemClickListener listener;
    Activity act;
    ImageLoader imageLoader;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;

        ImageView ivWeather;
        TextView title, wind, humidity, temprature;
        LinearLayout outer;
        public ViewHolder(LinearLayout v) {
            super(v);
            layout = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ADAPTER_WeatherItem(Activity act, List<ConsolidatedWeather> searchList, onItemClickListener listener) {
        this.searchList = searchList;
        this.act = act;
        this.listener = listener;
        this.imageLoader = new ImageLoader(act);
//        fontAwesome = Typeface.createFromAsset(act.getAssets(),"fonts/fontawesome-webfont.ttf");
//        openSansRegular = Typeface.createFromAsset(act.getAssets(),"fonts/OpenSans-Regular.ttf");
//        openSansSemiBold = Typeface.createFromAsset(act.getAssets(),"fonts/OpenSans-Semibold.ttf");
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_list_item_weather, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.ivWeather = v.findViewById(R.id.iv_weatherimg);

        vh.title = v.findViewById(R.id.tv_date);
        vh.wind = v.findViewById(R.id.tv_wind);
        vh.humidity = v.findViewById(R.id.tv_humidity);
        vh.temprature = v.findViewById(R.id.tv_temperature);
//        vh.outer = v.findViewById(R.id.rl_outer);
//        vh.textTitle = v.findViewById(R.id.tv_title);
//        vh.textType = v.findViewById(R.id.tv_type);
//        vh.textCoords = v.findViewById(R.id.tv_latlong);


        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.title.setText(searchList.get(position).getApplicableDate());
        holder.wind.setText(String.valueOf(UtilityHelperClass.roundToDecimalPlaces(searchList.get(position).getWindSpeed(), 2)));
        holder.humidity.setText(String.valueOf(searchList.get(position).getHumidity())+" %");
        holder.temprature.setText(UtilityHelperClass.roundToDecimalPlaces(searchList.get(position).getMinTemp(), 1)+ " C < "+UtilityHelperClass.roundToDecimalPlaces(searchList.get(position).getMinTemp(), 1)+" C < "+UtilityHelperClass.roundToDecimalPlaces(searchList.get(position).getMaxTemp(), 1)+" C");

        displayImageForWeather(searchList.get(position).getWeatherStateAbbr(), holder.ivWeather);


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public interface onItemClickListener{
        void onItemClick(Location location);
    }

    private void displayImageForWeather(String weatherAbbreviation, ImageView iv){
        switch (weatherAbbreviation){
            case "sn":
                imageLoader.DisplayImage(Config.snowImg, iv, act);
                break;

            case "sl":
                imageLoader.DisplayImage(Config.sleetImg, iv, act);
                break;

            case "h":
                imageLoader.DisplayImage(Config.hailImg, iv, act);
                break;

            case "t":
                imageLoader.DisplayImage(Config.thunderstormImg, iv, act);
                break;

            case "hr":
                imageLoader.DisplayImage(Config.heavyRainImg, iv, act);
                break;

            case "lr":
                imageLoader.DisplayImage(Config.LightRainImg, iv, act);
                break;

            case "s":
                imageLoader.DisplayImage(Config.showersImg, iv, act);
                break;

            case "hc":
                imageLoader.DisplayImage(Config.heavyCloudImg, iv, act);
                break;

            case "lc":
                imageLoader.DisplayImage(Config.lightCloudImg, iv, act);
                break;

            case "c":
                imageLoader.DisplayImage(Config.clearImg, iv, act);
                break;

        }
    }


}