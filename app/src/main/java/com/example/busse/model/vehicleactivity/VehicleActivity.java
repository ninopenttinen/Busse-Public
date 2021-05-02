package com.example.busse.model.vehicleactivity;

import java.util.ArrayList;
import java.util.List;

public class VehicleActivity {
    private String line;
    private String vehicleRef;
    private Double latitude;
    private Double longitude;
    private Double bearing;
    private String destinationShortName;
    private List<VehicleStopPoint> vehicleStopPoints = new ArrayList<>(); // Do this to prevent null pointer

    public VehicleActivity() {}

    public VehicleActivity(String line, String vehicleRef, Double latitude, Double longitude, Double bearing, String destinationShortName) {
        this.line = line;
        this.vehicleRef = vehicleRef;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bearing = bearing;
        this.destinationShortName = destinationShortName;
    }

    public String getLine() {
        return line;
    }

    public String getVehicleRef() {
        return vehicleRef;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getBearing() {
        return bearing;
    }

    public String getDestinationShortName() {
        return destinationShortName;
    }

    public List<VehicleStopPoint> getVehicleStopPoints() {
        return vehicleStopPoints;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setVehicleRef(String vehicleRef) {
        this.vehicleRef = vehicleRef;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setBearing(Double bearing) {
        this.bearing = bearing;
    }

    public void setDestinationShortName(String destinationShortName) {
        this.destinationShortName = destinationShortName;
    }

    public void addVehicleStopPoints(VehicleStopPoint vehicleStopPoint) {
        this.vehicleStopPoints.add(vehicleStopPoint);
    }

    public static class VehicleStopPoint {
        private String stopPointShortName;
        private String expectedArrivalTime;
        private String expectedDepartureTime;

        public VehicleStopPoint() {}

        public VehicleStopPoint(String stopPointShortName, String expectedArrivalTime, String expectedDepartureTime) {
            this.stopPointShortName = stopPointShortName;
            this.expectedArrivalTime = expectedArrivalTime;
            this.expectedDepartureTime = expectedDepartureTime;
        }

        public String getStopPointShortName() {
            return stopPointShortName;
        }

        public String getExpectedArrivalTime() {
            return expectedArrivalTime;
        }

        public String getExpectedDepartureTime() {
            return expectedDepartureTime;
        }

        public void setStopPointShortName(String stopPointShortName) {
            this.stopPointShortName = stopPointShortName;
        }

        public void setExpectedArrivalTime(String expectedArrivalTime) {
            this.expectedArrivalTime = expectedArrivalTime;
        }

        public void setExpectedDepartureTime(String expectedDepartureTime) {
            this.expectedDepartureTime = expectedDepartureTime;
        }
    }
}
