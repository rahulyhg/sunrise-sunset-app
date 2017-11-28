package com.destro13.sunrisesunsetapp.mvp.model;

public class City {
    private String name;
    private double latitude;
    private double longitude;
    private String sunrise;
    private String sunset;
    private String timeZone;

    public City(String name,
                double latitude,
                double longitude,
                String sunrise,
                String sunset,
                String timeZone) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.timeZone = timeZone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
}
