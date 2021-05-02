package com.example.busse.repository;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class DeviceLocationRepository implements LocationListener {
    private final Context context;
    private MutableLiveData<Location> deviceLocation;

    public DeviceLocationRepository(@NonNull Application application) {
        this.context = application;
        deviceLocation = new MutableLiveData<>();
    }

    public void startGPS() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Location not permitted");
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this); // TODO: Change the minDistance or Time
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        deviceLocation.postValue(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // GPS status has changed (connection -> no connection)
        System.out.println("GPS status changed");
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        System.out.println("Provider enabled");
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        System.out.println("Provider disabled");
    }

    public LiveData<Location> getDeviceLocation() {
        return deviceLocation;
    }
}
