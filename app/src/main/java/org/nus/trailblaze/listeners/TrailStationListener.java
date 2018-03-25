package org.nus.trailblaze.listeners;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by plasmashadow on 3/25/18.
 */

public class TrailStationListener implements TrailBlazeMapTraits{

    private Context context;

    public TrailStationListener(Context context){
        this.context = context;
    }

    @Override
    public String resolveFromPoint(LatLng latLng){
        try{
            Geocoder geocoder = new Geocoder(this.context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if(!addresses.isEmpty()){
                String addr = addresses.get(0).getFeatureName();
                return addr;
            }
            return null;
        }
        catch (IOException e){
            return null;
        }
    }
}
