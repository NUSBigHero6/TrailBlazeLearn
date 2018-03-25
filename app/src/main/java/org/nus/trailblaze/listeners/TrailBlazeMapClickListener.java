package org.nus.trailblaze.listeners;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.nus.trailblaze.models.Location;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by plasmashadow on 3/25/18.
 */

public class TrailBlazeMapClickListener implements GoogleMap.OnMapLongClickListener {

    private GoogleMap map;
    private TrailBlazeMapTraits tlistener;

    public TrailBlazeMapClickListener(GoogleMap map, TrailBlazeMapTraits tlistener){
        this.map = map;
        this.tlistener = tlistener;
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        MarkerOptions option = new MarkerOptions();
        option.position(latLng);
        try{
            String markerTitle = this.tlistener.resolveFromPoint(latLng);
            this.map.addMarker(option).setTitle(markerTitle);
        }
        catch (Exception e){
            Log.d("[Location/Debug]", e.toString());
        }
    }
}
