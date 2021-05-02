package com.example.busse.viewmodel;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.busse.model.arrivalsinfo.ArrivalsInfo;
import com.example.busse.model.stoppoint.StopPoint;
import com.example.busse.model.vehicleactivity.VehicleActivity;
import com.example.busse.repository.DeviceLocationRepository;
import com.example.busse.repository.StopPointRepository;
import com.example.busse.repository.VehicleActivityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

public class ArrivalsViewModel extends AndroidViewModel {

    private final StopPointRepository stopPointRepository;
    private final VehicleActivityRepository vehicleActivityRepository;
    private final DeviceLocationRepository deviceLocationRepository;
    private Disposable disposable;
    private LiveData<List<StopPoint>> allStopPoints;
    private LiveData<List<VehicleActivity>> vehicleActivity;
    private LiveData<Location> deviceLocation;
    private MutableLiveData<List<ArrivalsInfo>> arrivalsInfo;

    public ArrivalsViewModel(@NonNull Application application) {
        super(application);
        stopPointRepository = new StopPointRepository();
        vehicleActivityRepository = new VehicleActivityRepository();
        deviceLocationRepository = new DeviceLocationRepository(application);
        arrivalsInfo = new MutableLiveData<>();

        stopPointRepository.getStopPointsFromApi();
        arrivalsInfoUpdater();
    }

    private void arrivalsInfoUpdater() {
        disposable = Observable
            .interval(0, 3000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(aLong -> {
                allStopPoints = stopPointRepository.getAllStopPoints();
                vehicleActivity = vehicleActivityRepository.getVehicleActivity();
                deviceLocation = deviceLocationRepository.getDeviceLocation();
                try {
                    setArrivalsInfo();
                } catch (Exception e) {
                    System.out.println("Exception: " + e);
                }
            });
    }

    private void setArrivalsInfo() {
        if (allStopPoints.getValue() == null || vehicleActivity.getValue() == null)
            return;

        double userLatitude = 0.0d;
        double userLongitude = 0.0d;
        if (deviceLocation.getValue() != null) {
            userLatitude = deviceLocation.getValue().getLatitude();
            userLongitude = deviceLocation.getValue().getLongitude();
        }
        List<StopPoint> nearbyStopPoints = new ArrayList<>();

        // Get all stop points within 5km of the user
        for (StopPoint stopPoint : allStopPoints.getValue()) {
            float[] distance = new float[1];
            Location.distanceBetween(userLatitude, userLongitude, stopPoint.getLatitude(), stopPoint.getLongitude(), distance);
            if (distance[0] < 5000.0f) {
                nearbyStopPoints.add(stopPoint);
            }
        }

        List<ArrivalsInfo> newArrivalsInfoList = new ArrayList<>();

        for (VehicleActivity vehicle : vehicleActivity.getValue()) {
            ArrivalsInfo newArrivalsInfo = new ArrivalsInfo();
            float closestDistanceToDevice = 5000.0f;

            // Find the closest stop point
            for (VehicleActivity.VehicleStopPoint vehicleStopPoint : vehicle.getVehicleStopPoints()) {
                for (StopPoint nearbyStopPoint : nearbyStopPoints) {
                    if (nearbyStopPoint.getShortName().equals(vehicleStopPoint.getStopPointShortName())) {
                        float[] distanceToDevice = new float[1];
                        Location.distanceBetween(userLatitude, userLongitude, nearbyStopPoint.getLatitude(), nearbyStopPoint.getLongitude(), distanceToDevice);

                        if (distanceToDevice[0] < closestDistanceToDevice) {
                            closestDistanceToDevice = distanceToDevice[0];
                            newArrivalsInfo.setDistanceToStopPoint((int) distanceToDevice[0]);
                            newArrivalsInfo.setArrivalTime(vehicleStopPoint.getExpectedArrivalTime());
                            newArrivalsInfo.setDepartureTime(vehicleStopPoint.getExpectedDepartureTime());
                            newArrivalsInfo.setStopPointName(nearbyStopPoint.getName());
                            newArrivalsInfo.setStopPointLatitude(nearbyStopPoint.getLatitude());
                            newArrivalsInfo.setStopPointLongitude(nearbyStopPoint.getLongitude());
                        }
                    }
                }
            }
            if (newArrivalsInfo.getStopPointName() != null) {
                // Find destination stop point name
                for (StopPoint stopPoint : allStopPoints.getValue()) {
                    if (vehicle.getDestinationShortName().equals(stopPoint.getShortName())) {
                        newArrivalsInfo.setDestination(stopPoint.getName());
                        break;
                    }
                }
                // get vehicle line number and reference
                newArrivalsInfo.setLine(vehicle.getLine());
                newArrivalsInfo.setVehicleRef(vehicle.getVehicleRef());
                newArrivalsInfoList.add(newArrivalsInfo);
            }
        }
        arrivalsInfo.postValue(newArrivalsInfoList);
    }

    public LiveData<List<ArrivalsInfo>> getArrivalsInfo() {
        return arrivalsInfo;
    }

    public void startGPS() {
        deviceLocationRepository.startGPS();
    }

    @Override
    protected void onCleared() {
        if (disposable != null)
            disposable.dispose();
        super.onCleared();
    }
}
