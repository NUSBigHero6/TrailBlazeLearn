package org.nus.trailblaze.models;

/**
 * Created by wengweichen on 12/3/18.
 */

public class Location {
    private double longitude;
    private double latitude;
    private String name;

    public  Location(double logitude,double latitude,String name)
    {
        this.longitude=logitude;
        this.latitude=latitude;
        this.name=name;

    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
