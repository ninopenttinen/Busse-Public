package com.example.busse.model.vehicleactivity;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VehicleActivityJsonResponse {
    @SerializedName("body")
    private List<VehicleActivityBody> vehicleActivityBody;

    public List<VehicleActivityBody> getVehicleActivityBody() {
        return vehicleActivityBody;
    }
}
