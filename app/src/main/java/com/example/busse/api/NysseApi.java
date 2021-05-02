package com.example.busse.api;

import com.example.busse.model.stoppoint.StopPointJsonResponse;
import com.example.busse.model.vehicleactivity.VehicleActivityJsonResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NysseApi {
    @GET("/journeys/api/1/vehicle-activity")
    Call<VehicleActivityJsonResponse> refreshVehicleActivity();

    @GET("/journeys/api/1/stop-points")
    Call<StopPointJsonResponse> getStopPointsFromApi();
}
