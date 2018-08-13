
package com.cvapplication.kelevnor.mylocations.MODELS.locations_list;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LocationWrap {

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    @SerializedName("locations")
    @Expose
    private List<Location> locations;




}
