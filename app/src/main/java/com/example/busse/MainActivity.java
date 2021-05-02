package com.example.busse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.busse.adapter.ArrivalsAdapter;
import com.example.busse.common.Utility;
import com.example.busse.model.arrivalsinfo.ArrivalsInfo;
import com.example.busse.viewmodel.ArrivalsViewModel;
import com.google.android.material.slider.Slider;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrivalsViewModel arrivalsViewModel;
    private int maxDistance = 1000;
    private int maxWaitTime = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView distanceTextView = findViewById(R.id.distanceTextView);
        TextView timeTextView = findViewById(R.id.timeTextView);
        distanceTextView.setText(getString(R.string.activity_main_search_distance_value, String.valueOf(maxDistance)));
        timeTextView.setText(getString(R.string.activity_main_wait_time_value, String.valueOf(maxWaitTime)));

        Slider searchDistanceSlider = findViewById(R.id.searchDistanceSlider);
        Slider waitTimeSlider = findViewById(R.id.waitTimeSlider);
        searchDistanceSlider.setValue(1000);
        waitTimeSlider.setValue(15);

        searchDistanceSlider.addOnChangeListener((slider, value, fromUser) -> {
            maxDistance = (int) value;
            String formattedString = getString(R.string.activity_main_search_distance_value, String.valueOf(maxDistance));
            distanceTextView.setText(formattedString);
        });
        waitTimeSlider.addOnChangeListener((slider, value, fromUser) -> {
            maxWaitTime = (int) value;
            String formattedString = getString(R.string.activity_main_wait_time_value, String.valueOf(maxWaitTime));
            timeTextView.setText(formattedString);
        });

        // Create recycler view
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Create adapter
        ArrivalsAdapter adapter = new ArrivalsAdapter();
        recyclerView.setAdapter(adapter);

        // Create view model
        arrivalsViewModel = new ViewModelProvider(this, ViewModelProvider
                .AndroidViewModelFactory
                .getInstance(this.getApplication()))
                .get(ArrivalsViewModel.class);

        arrivalsViewModel.getArrivalsInfo().observe(this, arrivalsInfo ->
                filterResults(adapter, arrivalsInfo)
        );

        requestLocationPermission();
    }

    // Filter the arrivalsInfo and set the adapter
    private void filterResults(ArrivalsAdapter adapter, List<ArrivalsInfo> arrivalsInfo) {
        Iterator<ArrivalsInfo> iterator = arrivalsInfo.iterator();
        while (iterator.hasNext()) {
            ArrivalsInfo arrival = iterator.next();
            Date now = Calendar.getInstance().getTime();
            Date arrivalTime = new Date();

            if (arrival.getArrivalTime() != null) {
                arrivalTime = Utility.formatDateTime(arrival.getArrivalTime(), "yyyy-MM-dd'T'HH:mm:ssXXX");
            } else if (arrival.getDepartureTime() != null) {
                arrivalTime = Utility.formatDateTime(arrival.getDepartureTime(), "yyyy-MM-dd'T'HH:mm:ssXXX");
            }

            arrival.setTimeUntilArrival(Utility.timeDifferenceInMinutes(now, arrivalTime));
            if (arrival.getTimeUntilArrival() > maxWaitTime ||
                    arrival.getDistanceToStopPoint() > maxDistance) {
                iterator.remove();
            }
        }

        if (arrivalsInfo.isEmpty()) {
            findViewById(R.id.no_arrivals_message).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.no_arrivals_message).setVisibility(View.GONE);
        }
        Collections.sort(arrivalsInfo);
        adapter.setArrivals(arrivalsInfo);
    }

    // Request permissions
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION
        }, 0);
    }

    // This method is called from ActivityCompat.requestPermissions, when user has granted/denied the asked permissions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        arrivalsViewModel.startGPS();
    }
}