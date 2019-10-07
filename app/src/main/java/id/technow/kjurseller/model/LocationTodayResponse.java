package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Afyad Kafa on 1/28/2019.
 */

public class LocationTodayResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("location")
    @Expose
    private ArrayList<LocationToday> locationToday = null;

    public LocationTodayResponse(String status, ArrayList<LocationToday> locationToday) {
        this.status = status;
        this.locationToday = locationToday;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<LocationToday> getLocationToday() {
        return locationToday;
    }
}
