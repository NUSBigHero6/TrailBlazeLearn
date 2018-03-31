package org.nus.trailblaze.models;

/**
 * Created by plasmashadow on 3/11/18.
 */
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Item implements Parcelable {
    private String id;
    private User user;
    private Date createdDate;
    private File file;
    protected String trailStationId;
    protected String learningTrailId;

    public Item(){}
    public Item(String id, User user)
    {
        this.id = id;
        this.user = user;
        this.createdDate = new Date();
    }
    public Item(String id, User user, Date createdDate, File file) {
        this.id = id;
        this.user = user;
        this.createdDate = createdDate;
        this.file = file;
    }
    public  String GetTrailStationId()
    {
        return  this.trailStationId;
    }
    public  void  SetTrailStationId(String trailStationId)
    {
        this.trailStationId=trailStationId;
    }
    public  String GetLearingTrailId()
    {
        return  this.learningTrailId;
    }
    public  void  SetLearingTrailId(String learningTrailId)
    {
        this.learningTrailId=learningTrailId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Storing the Movie data to Parcel object
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
    }

    /**
     * Retrieving Movie data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private Item(Parcel in){
        this.setId(in.readString());
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
