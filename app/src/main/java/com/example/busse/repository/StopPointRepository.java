package com.example.busse.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.busse.api.NysseApi;
import com.example.busse.model.stoppoint.StopPoint;
import com.example.busse.model.stoppoint.StopPointBody;
import com.example.busse.model.stoppoint.StopPointJsonResponse;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StopPointRepository {
    private static final String URL = "https://data.itsfactory.fi";
    private final NysseApi nysseApi;
    private MutableLiveData<List<StopPoint>> allStopPoints;

    public StopPointRepository() {

        allStopPoints = new MutableLiveData<>();

        OkHttpClient client = new OkHttpClient.Builder().build();
        nysseApi = new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NysseApi.class);
    }

    public void getStopPointsFromApi() {
        nysseApi.getStopPointsFromApi()
                .enqueue(new Callback<StopPointJsonResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<StopPointJsonResponse> call, @NonNull Response<StopPointJsonResponse> response) {
                        if (response.isSuccessful()) {
                            List<StopPoint> newListOfStopPoints = new ArrayList<>();
                            for (StopPointBody stopPoint : response.body().getStopPointBody()) {
                                String[] location = stopPoint.getStopPointLocation().split(",", 2);

                                StopPoint newStopPoint = new StopPoint(
                                        stopPoint.getStopPointShortName(),
                                        stopPoint.getStopPointName(),
                                        Double.parseDouble(location[0]),
                                        Double.parseDouble(location[1])
                                );
                                newListOfStopPoints.add(newStopPoint);
                            }
                            allStopPoints.postValue(newListOfStopPoints);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<StopPointJsonResponse> call, @NonNull Throwable t) {
                        System.out.println(t.getMessage());
                    }
                });
    }

    public LiveData<List<StopPoint>> getAllStopPoints() {
        return allStopPoints;
    }
}

