package com.cvapplication.kelevnor.mylocations.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cvapplication.kelevnor.mylocations.R;
import com.cvapplication.kelevnor.mylocations.Utils.ImageLoader;
import com.cvapplication.kelevnor.mylocations.models.objects.Location;
import java.util.ArrayList;
/**
 * Created by mariossifalakis on 4/6/16.
 */
public class ADAPTER_LocationList extends BaseAdapter {
    private ArrayList<Location> searchArrayList;

    Activity act;
    private LayoutInflater mInflater;
    private ImageLoader imageLoader;

    public ADAPTER_LocationList(Activity act, ArrayList<Location> results) {
        this.searchArrayList = results;
        mInflater = LayoutInflater.from(act.getApplicationContext());
        this.act = act;
        this.imageLoader = new ImageLoader(act.getApplicationContext());

    }

    public int getCount() {
        return searchArrayList.size();
    }

    public Location getItem(int position) {
        return searchArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("WrongConstant")
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        holder = new ViewHolder();
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_locations, null);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name = (TextView) convertView.findViewById(R.id.tv_name);
        holder.subname = (TextView) convertView.findViewById(R.id.tv_subname);
        holder.imageMarker = (ImageView) convertView.findViewById(R.id.iv_pin);

        return convertView;
    }

    static class ViewHolder {
        TextView name;
        TextView subname;
        ImageView imageMarker;
    }
}

