package com.example.busse.viewmodel;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.busse.model.vehicleactivity.VehicleActivity;
import com.example.busse.repository.VehicleActivityRepository;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

public class VehicleLocationViewModel extends AndroidViewModel {
    private final String vehicleRef;
    private final VehicleActivityRepository vehicleActivityRepository;
    Disposable disposable;
    private LiveData<List<VehicleActivity>> vehicleActivity;
    private MutableLiveData<Location> vehicleLocation;

    public VehicleLocationViewModel(@NonNull Application application, String vehicleRef) {
        super(application);
        this.vehicleRef = vehicleRef;
        vehicleActivityRepository = new VehicleActivityRepository();
        vehicleLocation = new MutableLiveData<>();

        vehicleLocationUpdater();
    }

    private void vehicleLocationUpdater() {
        disposable = Observable
            .interval(0, 2000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(aLong -> setVehicleLocation());
    }

    private void setVehicleLocation() {
        vehicleActivity = vehicleActivityRepository.getVehicleActivity();
        if (vehicleActivity.getValue() == null)
            return;

        Location newVehicleLocation = new Location("");

        for (VehicleActivity vehicle : vehicleActivity.getValue()) {
            if (vehicle.getVehicleRef().equals(vehicleRef)) {
                newVehicleLocation.setLatitude(vehicle.getLatitude());
                newVehicleLocation.setLongitude(vehicle.getLongitude());
                break;
            }
        }

        vehicleLocation.postValue(newVehicleLocation);
    }

    public LiveData<Location> getVehicleLocation() {
        return vehicleLocation;
    }

    @Override
    protected void onCleared() {
        if (disposable != null)
            disposable.dispose();
        super.onCleared();
    }
}
