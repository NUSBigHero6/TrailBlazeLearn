package org.nus.trailblaze.models;

import java.util.Date;

/**
 * Created by priyanka on 30/3/2018.
 */

public class ThreadPost {
    private String id, message, url, userId;
    private Date createdDate;

    public ThreadPost(){}

    public ThreadPost(String id, Date createdDate, String message, String url, String userId){
        this.id = id;
        this.createdDate = createdDate;
        this.message = message;
        this.url = url;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

//    public String getStationId() { return stationId; }
//    public void setStationId() { this.stationId = stationId; }

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

}
