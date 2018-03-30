package org.nus.trailblaze.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.firestore.IgnoreExtraProperties;

/**
 * Created by plasmashadow on 3/11/18.
 */


@IgnoreExtraProperties
public class User implements Parcelable {

    public static final Parcelable.Creator CREATOR = new UserParcelCreator();

    private String id;
    private String name;
    private String email;
//    private String profileImage;

    public User() {}

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
//        this.profileImage = profileImage;
    }

    public User(Parcel in){

        this.id = in.readString();
        this.name = in.readString();
        this.email = in.readString();
//        this.profileImage = in.readString();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public String getPhotoUrl(String profileImage) { return profileImage; }
//
//    public void setPhotoUrl(String profileImage) { this.profileImage = profileImage; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.email);
//        parcel.writeString(this.profileImage);
    }
}
