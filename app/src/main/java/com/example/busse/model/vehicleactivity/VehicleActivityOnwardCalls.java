package com.example.busse.model.vehicleactivity;

import com.google.gson.annotations.SerializedName;

public class VehicleActivityOnwardCalls {
    @SerializedName("expectedArrivalTime")
    private String expectedArrivalTime;

    @SerializedName("expectedDepartureTime")
    private String expectedDepartureTime;

    @SerializedName("stopPointRef")
    private String stopPointRef;

    public String getExpectedArrivalTime() {
        return expectedArrivalTime;
    }

    public String getExpectedDepartureTime() {
        return expectedDepartureTime;
    }

    public String getStopPointRef() {
        return stopPointRef;
    }
}
