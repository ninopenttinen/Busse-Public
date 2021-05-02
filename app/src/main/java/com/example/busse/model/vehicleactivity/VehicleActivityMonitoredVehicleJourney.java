package com.example.busse.model.vehicleactivity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleActivityMonitoredVehicleJourney {
    @SerializedName("vehicleLocation")
    private VehicleActivityVehicleLocation vehicleLocation;

    @SerializedName("vehicleRef")
    private String vehicleRef;

    @SerializedName("journeyPatternRef")
    private String line;

    @SerializedName("destinationShortName")
    private String destinationShortName;

    @SerializedName("bearing")
    private Double bearing;

    @SerializedName("onwardCalls")
    private List<VehicleActivityOnwardCalls> onwardCalls;

    public VehicleActivityVehicleLocation getVehicleLocation() {
        return vehicleLocation;
    }

    public String getVehicleRef() {
        return vehicleRef;
    }

    public String getLine() {
        return line;
    }

    public String getDestinationShortName() {
        return destinationShortName;
    }

    public Double getBearing() {
        return bearing;
    }

    public List<VehicleActivityOnwardCalls> getOnwardCalls() {
        return onwardCalls;
    }
}
