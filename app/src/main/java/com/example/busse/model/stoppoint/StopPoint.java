package com.example.busse.model.stoppoint;

public class StopPoint {
    private String shortName;
    private String name;
    private Double latitude;
    private Double longitude;

    public StopPoint() {
    }

    public StopPoint(String shortName, String name, Double latitude, Double longitude) {
        this.shortName = shortName;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getShortName() {
        return shortName;
    }

    public String getName() {
        return name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }
}
