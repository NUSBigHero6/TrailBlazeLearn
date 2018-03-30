package org.nus.trailblaze.models;

/**
 * Created by plasmashadow on 3/11/18.
 */
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class File implements Parcelable {
    private String id;
    private String name;
    private String url;
    private Float size;
    private Date uploadDate;
    private String mimeType;

    public File() {}
    public File(String id,String name)
    {
        this.id=id;
        this.name=name;
    }

    public File(String id, String name, String url, Float size, Date uploadDate, String mimeType) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.size = size;
        this.uploadDate = uploadDate;
        this.mimeType = mimeType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Float getSize() {
        return size;
    }

    public void setSize(Float size) {
        this.size = size;
    }

    public Date getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(Date uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Storing the Movie data to Parcel object
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getMimeType());
        dest.writeString(getUrl());
        dest.writeString(getName());
    }

    /**
     * Retrieving Movie data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private File(Parcel in){
        this.setMimeType(in.readString());
        this.setUrl(in.readString());
        this.setName(in.readString());
    }

    public static final Parcelable.Creator<File> CREATOR = new Parcelable.Creator<File>() {
        @Override
        public File createFromParcel(Parcel source) {
            return new File(source);
        }

        @Override
        public File[] newArray(int size) {
            return new File[size];
        }
    };
}
