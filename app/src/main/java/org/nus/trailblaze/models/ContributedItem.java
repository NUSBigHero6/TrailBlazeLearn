package org.nus.trailblaze.models;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by plasmashadow on 3/11/18.
 */

public class ContributedItem extends Item {
    private String description;
    private String learningTrailId;

    public ContributedItem() {}

    public ContributedItem(String id, User user, Date createdDate, File file, String description) {
        super(id, user, createdDate, file);
        this.description = description;
    }
    public ContributedItem(String id, User user,String description)
    {
        super(id,user);
        this.description=description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLearningTrailId() {
        return learningTrailId;
    }

    public void setLearningTrailId(String learningTrailId) {
        this.learningTrailId = learningTrailId;
    }

    // Storing the Movie data to Parcel object
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
        dest.writeString(getDescription());
        dest.writeString(getLearningTrailId());
        dest.writeParcelable(getFile(), flags);
    }

    /**
     * Retrieving Movie data from Parcel object
     * This constructor is invoked by the method createFromParcel(Parcel source) of
     * the object CREATOR
     **/
    private ContributedItem(Parcel in){
        this.setId(in.readString());
        this.setDescription(in.readString());
        this.setLearningTrailId(in.readString());
        this.setFile((File)in.readParcelable(File.class.getClassLoader()));
    }

    public static final Parcelable.Creator<ContributedItem> CREATOR = new Parcelable.Creator<ContributedItem>() {
        @Override
        public ContributedItem createFromParcel(Parcel source) {
            return new ContributedItem(source);
        }

        @Override
        public ContributedItem[] newArray(int size) {
            return new ContributedItem[size];
        }
    };
}

