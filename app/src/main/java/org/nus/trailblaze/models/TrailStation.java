package org.nus.trailblaze.models;

/**
 * Created by plasmashadow on 3/11/18.
 */

public class TrailStation {
    private String id;
    private Location location;
    private String name;
    private String instruction;

    public TrailStation() {};

    public TrailStation(String id, Location location, String name, String instruction) {
        this.id = id;
        this.location = location;
        this.name = name;
        this.instruction = instruction;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

}
