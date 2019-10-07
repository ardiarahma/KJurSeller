package id.technow.kjurseller.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Afyad Kafa on 1/28/2019.
 */

public class LocationResponse {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("location")
    @Expose
    private ArrayList<Location> location = null;

    public LocationResponse(String status, ArrayList<Location> location) {
        this.status = status;
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<Location> getLocation() {
        return location;
    }
}
