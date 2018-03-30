package org.nus.trailblaze.models;

/**
 * Created by wengweichen on 12/3/18.
 */

public class Location {
    private Float longitude;
    private Float latitude;
    private String name;

    public  Location(Float logitude,Float latitude,String name)
    {
        this.longitude=logitude;
        this.latitude=latitude;
        this.name=name;

    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Float getLatitude() {
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
