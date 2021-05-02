package com.example.busse.model.vehicleactivity;

import com.google.gson.annotations.SerializedName;

public class VehicleActivityBody {
    @SerializedName("monitoredVehicleJourney")
    private VehicleActivityMonitoredVehicleJourney monitoredVehicleJourney;

    public VehicleActivityMonitoredVehicleJourney getMonitoredVehicleJourney() {
        return monitoredVehicleJourney;
    }
}
