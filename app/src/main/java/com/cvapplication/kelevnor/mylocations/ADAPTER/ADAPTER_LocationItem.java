package com.cvapplication.kelevnor.mylocations.ADAPTER;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cvapplication.kelevnor.mylocations.MODELS.locations_list.Location;
import com.cvapplication.kelevnor.mylocations.R;
import java.util.List;

/**
 * Created by kelevnor on 1/18/18.
 */

public class ADAPTER_LocationItem extends RecyclerView.Adapter<ADAPTER_LocationItem.ViewHolder> {
    private List<Location> searchList;
    onItemClickListener listener;
    Activity act;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;

        TextView textTitle, textTitleValue;
        TextView textType, textTypeValue;
        TextView textCoords, textCoordsValue;
        LinearLayout outer;
        public ViewHolder(LinearLayout v) {
            super(v);
            layout = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ADAPTER_LocationItem(Activity act, List<Location> searchList, onItemClickListener listener) {
        this.searchList = searchList;
        this.act = act;
        this.listener = listener;
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
                .inflate(R.layout.custom_list_item_locations, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.textTitleValue = v.findViewById(R.id.tv_titlevalue);
        vh.textTypeValue = v.findViewById(R.id.tv_typevalue);
        vh.textCoordsValue = v.findViewById(R.id.tv_latlongvalue);
        vh.outer = v.findViewById(R.id.rl_outer);
        vh.textTitle = v.findViewById(R.id.tv_title);
        vh.textType = v.findViewById(R.id.tv_type);
        vh.textCoords = v.findViewById(R.id.tv_latlong);


        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.textTitleValue.setText(searchList.get(position).getTitle());
        holder.textTypeValue.setText(searchList.get(position).getLocationType());
        holder.textCoordsValue.setText(searchList.get(position).getLattLong());

        holder.outer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(searchList.get(position));
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public interface onItemClickListener{
        void onItemClick(Location location);
    }


}