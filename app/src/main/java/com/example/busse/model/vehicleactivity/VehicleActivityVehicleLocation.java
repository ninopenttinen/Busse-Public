package com.example.busse.model.vehicleactivity;

import com.google.gson.annotations.SerializedName;

public class VehicleActivityVehicleLocation {
    @SerializedName("longitude")
    private Double longitude;

    @SerializedName("latitude")
    private Double latitude;

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }
}
