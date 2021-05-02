package com.example.busse;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.busse.viewmodel.VehicleLocationViewModel;
import com.example.busse.viewmodel.viewmodelfactory.VehicleLocationViewModelFactory;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private VehicleLocationViewModel vehicleLocationViewModel;
    Disposable disposable;
    private Double deviceLatitude;
    private Double deviceLongitude;
    private Double vehicleLatitude;
    private Double vehicleLongitude;
    private Double stopPointLatitude;
    private Double stopPointLongitude;

    @SuppressLint("VisibleForTests")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        stopPointLatitude = bundle.getDouble("stopPointLatitude");
        stopPointLongitude = bundle.getDouble("stopPointLongitude");
        String vehicleRef = bundle.getString("vehicleRef");
        String followedLine = bundle.getString("line");
        String journeyStart = bundle.getString("stopPointName");
        String journeyEnd = bundle.getString("destination");
        fusedLocationProviderClient = new FusedLocationProviderClient(this);

        setContentView(R.layout.activity_maps);
        TextView followedLineTextView = findViewById(R.id.text_view_following_line);
        TextView journeyTextView = findViewById(R.id.text_view_journey);
        followedLineTextView.setText(getString(R.string.activity_maps_following_line, followedLine));
        journeyTextView.setText(getString(R.string.activity_maps_journey, journeyStart, journeyEnd));

        // Create view model for vehicle location
        vehicleLocationViewModel = new ViewModelProvider(this, new VehicleLocationViewModelFactory(this.getApplication(), vehicleRef))
                .get(VehicleLocationViewModel.class);

        vehicleLocationViewModel.getVehicleLocation().observe(this, location -> {
            vehicleLatitude = location.getLatitude();
            vehicleLongitude = location.getLongitude();
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
            if (!success)
                Toast.makeText(this, "Maps style load failed", Toast.LENGTH_SHORT).show();
        } catch (Resources.NotFoundException exception) {
            exception.printStackTrace();
        }
        this.googleMap = googleMap;

        final LatLng stopPointPosition = new LatLng(stopPointLatitude, stopPointLongitude);
        final Marker stopPointMarker = this.googleMap.addMarker(new MarkerOptions()
                .position(stopPointPosition)
                .title("Pysäkki")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.stop_point_icon_hdpi_24)));
        Marker vehicleMarker = this.googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0.0d, 0.0d))
                .title("Linja-auto")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.vehicle_icon_hdpi_24)));
        Marker deviceMarker = this.googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0.0d, 0.0d))
                .title("Sinun sijaintisi")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.device_icon_hdpi_24)));

        List<PatternItem> pattern = Arrays.asList(new Dash(30), new Gap(20));
        Polyline polyLine = this.googleMap.addPolyline(new PolylineOptions()
                .width(5)
                .color(Color.rgb(33, 136, 254)));
        polyLine.setPattern(pattern);

        this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stopPointPosition, 10f));

        AtomicBoolean cameraSetupDone = new AtomicBoolean(false);
        disposable = Observable
            .interval(0, 2000, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(aLong -> {
                getDeviceLocation();
                LatLng vehiclePosition = null;
                LatLng devicePosition = null;
                if (vehicleLatitude != null && vehicleLongitude != null) {
                    vehiclePosition = new LatLng(vehicleLatitude, vehicleLongitude);
                    vehicleMarker.setPosition(vehiclePosition);
                }
                if (deviceLatitude != null && deviceLongitude != null) {
                    devicePosition = new LatLng(deviceLatitude, deviceLongitude);
                    deviceMarker.setPosition(devicePosition);
                }
                if (vehiclePosition != null && devicePosition != null) {
                    ArrayList<LatLng> polyLinePoints = new ArrayList<>();
                    polyLinePoints.add(devicePosition);
                    polyLinePoints.add(stopPointPosition);
                    polyLinePoints.add(vehiclePosition);
                    polyLine.setPoints(polyLinePoints);
                }
                if (!cameraSetupDone.get() && vehiclePosition != null && devicePosition != null) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(stopPointPosition);
                    builder.include(vehiclePosition);
                    builder.include(devicePosition);
                    this.googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 200));
                    cameraSetupDone.set(true);
                }
            });
    }

    private void getDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, 200);
            return;
        }

        Task<Location> getLocationTask = fusedLocationProviderClient.getLastLocation(); // TODO: use GPS provider instead?
        getLocationTask.addOnCompleteListener(task -> {
            deviceLatitude = task.getResult().getLatitude();
            deviceLongitude = task.getResult().getLongitude();
        });
    }

    @Override
    protected void onDestroy() {
        if (disposable != null)
            disposable.dispose();
        super.onDestroy();
    }
}