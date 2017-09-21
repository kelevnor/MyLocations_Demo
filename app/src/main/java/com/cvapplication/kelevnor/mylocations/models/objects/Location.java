
package com.cvapplication.kelevnor.mylocations.models.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Location {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("marker_color")
    @Expose
    private String markerColor;
    @SerializedName("location_latitude")
    @Expose
    private Double locationLatitude;
    @SerializedName("location_longitude")
    @Expose
    private Double locationLongitude;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public Double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(Double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }
    public Double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(Double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }
    public String getMarkerColor() {
        return markerColor;
    }
    public void setMarkerColor(String markerColor) {
        this.markerColor = markerColor;
    }
}
