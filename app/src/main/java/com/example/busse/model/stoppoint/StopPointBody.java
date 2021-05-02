package com.example.busse.model.stoppoint;

import com.google.gson.annotations.SerializedName;

public class StopPointBody {
    @SerializedName("url")
    private String stopPointRef;

    @SerializedName("location")
    private String stopPointLocation;

    @SerializedName("name")
    private String stopPointName;

    @SerializedName("shortName")
    private String stopPointShortName;

    public String getStopPointRef() {
        return stopPointRef;
    }

    public String getStopPointLocation() {
        return stopPointLocation;
    }

    public String getStopPointName() {
        return stopPointName;
    }

    public String getStopPointShortName() {
        return stopPointShortName;
    }
}
