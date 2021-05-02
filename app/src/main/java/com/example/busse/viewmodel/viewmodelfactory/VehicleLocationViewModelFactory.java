package com.example.busse.viewmodel.viewmodelfactory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.busse.viewmodel.VehicleLocationViewModel;

public class VehicleLocationViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    private final String vehicleRef;

    public VehicleLocationViewModelFactory(Application application, String vehicleRef) {
        this.application = application;
        this.vehicleRef = vehicleRef;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new VehicleLocationViewModel(application, vehicleRef);
    }
}
