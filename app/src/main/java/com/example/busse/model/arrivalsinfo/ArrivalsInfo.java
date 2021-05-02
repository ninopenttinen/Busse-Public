package com.example.busse.model.arrivalsinfo;

public class ArrivalsInfo implements Comparable<ArrivalsInfo> {
    private String stopPointName;
    private String line;
    private String arrivalTime;
    private String departureTime;
    private Integer timeUntilArrival;
    private String destination;
    private Integer distanceToStopPoint;
    private Double stopPointLatitude;
    private Double stopPointLongitude;
    private String vehicleRef;

    public ArrivalsInfo() {
    }

    public ArrivalsInfo(String stopPointName, String line, String arrivalTime, String departureTime, String destination, Integer distanceToStopPoint, Double stopPointLatitude, Double stopPointLongitude, String vehicleRef) {
        this.stopPointName = stopPointName;
        this.line = line;
        this.arrivalTime = arrivalTime;
        this.departureTime = departureTime;
        this.destination = destination;
        this.distanceToStopPoint = distanceToStopPoint;
        this.stopPointLatitude = stopPointLatitude;
        this.stopPointLongitude = stopPointLongitude;
        this.vehicleRef = vehicleRef;
    }

    public void setStopPointName(String stopPointName) {
        this.stopPointName = stopPointName;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public void setTimeUntilArrival(Integer timeUntilArrival) {
        this.timeUntilArrival = timeUntilArrival;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDistanceToStopPoint(Integer distanceToStopPoint) {
        this.distanceToStopPoint = distanceToStopPoint;
    }

    public void setStopPointLatitude(Double stopPointLatitude) {
        this.stopPointLatitude = stopPointLatitude;
    }

    public void setStopPointLongitude(Double stopPointLongitude) {
        this.stopPointLongitude = stopPointLongitude;
    }

    public void setVehicleRef(String vehicleRef) {
        this.vehicleRef = vehicleRef;
    }

    public String getStopPointName() {
        return stopPointName;
    }

    public String getLine() {
        return line;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public Integer getTimeUntilArrival() {
        return timeUntilArrival;
    }

    public String getDestination() {
        return destination;
    }

    public Integer getDistanceToStopPoint() {
        return distanceToStopPoint;
    }

    public Double getStopPointLatitude() {
        return stopPointLatitude;
    }

    public Double getStopPointLongitude() {
        return stopPointLongitude;
    }

    public String getVehicleRef() {
        return vehicleRef;
    }

    @Override
    public int compareTo(ArrivalsInfo arrival) {
        return this.timeUntilArrival.compareTo(arrival.timeUntilArrival);
    }
}
