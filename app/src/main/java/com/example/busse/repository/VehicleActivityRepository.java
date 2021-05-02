package com.example.busse.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.busse.api.NysseApi;
import com.example.busse.model.vehicleactivity.VehicleActivity;
import com.example.busse.model.vehicleactivity.VehicleActivityJsonResponse;
import com.example.busse.model.vehicleactivity.VehicleActivityBody;
import com.example.busse.model.vehicleactivity.VehicleActivityOnwardCalls;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VehicleActivityRepository {
    private static final String URL = "https://data.itsfactory.fi";
    private final NysseApi nysseApi;
    Disposable disposable;
    private MutableLiveData<List<VehicleActivity>> vehicleActivity;

    public VehicleActivityRepository() {
        vehicleActivity = new MutableLiveData<>();
        OkHttpClient client = new OkHttpClient.Builder().build();
        nysseApi = new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NysseApi.class);

        System.out.println(this);
        startRefreshVehicleActivity();
    }

    private void startRefreshVehicleActivity() {
        disposable = Observable
                .interval(0, 3000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> refreshVehicleActivity());
    }

    private void refreshVehicleActivity() {
        nysseApi.refreshVehicleActivity()
                .enqueue(new Callback<VehicleActivityJsonResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<VehicleActivityJsonResponse> call, @NonNull Response<VehicleActivityJsonResponse> response) {
                        if (response.isSuccessful()) {
                            List<VehicleActivity> newListOfVehicleActivity = new ArrayList<>();

                            for (VehicleActivityBody vehicle : response.body().getVehicleActivityBody()) {
                                VehicleActivity newVehicleActivity = new VehicleActivity(
                                        vehicle.getMonitoredVehicleJourney().getLine(),
                                        vehicle.getMonitoredVehicleJourney().getVehicleRef(),
                                        vehicle.getMonitoredVehicleJourney().getVehicleLocation().getLatitude(),
                                        vehicle.getMonitoredVehicleJourney().getVehicleLocation().getLongitude(),
                                        vehicle.getMonitoredVehicleJourney().getBearing(),
                                        vehicle.getMonitoredVehicleJourney().getDestinationShortName()
                                );

                                for (VehicleActivityOnwardCalls onwardCalls : vehicle.getMonitoredVehicleJourney().getOnwardCalls()) {
                                    String stopPointRef = onwardCalls.getStopPointRef();
                                    String stopPointShortName = stopPointRef.substring(stopPointRef.lastIndexOf('/') + 1);

                                    VehicleActivity.VehicleStopPoint newVehicleStopPoint = new VehicleActivity.VehicleStopPoint(
                                            stopPointShortName,
                                            onwardCalls.getExpectedArrivalTime(),
                                            onwardCalls.getExpectedDepartureTime()
                                    );

                                    newVehicleActivity.addVehicleStopPoints(newVehicleStopPoint);
                                }
                                newListOfVehicleActivity.add(newVehicleActivity);
                            }
                            vehicleActivity.postValue(newListOfVehicleActivity);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<VehicleActivityJsonResponse> call, @NonNull Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
    }

    public LiveData<List<VehicleActivity>> getVehicleActivity() {
        return vehicleActivity;
    }

    @Override
    protected void finalize() throws Throwable {
        if (disposable != null)
            disposable.dispose();
        super.finalize();
    }
}
