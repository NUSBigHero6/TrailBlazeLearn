package org.nus.trailblaze.listeners;

import java.util.List;
import android.location.Address;
import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by plasmashadow on 3/25/18.
 */

public interface TrailBlazeMapTraits {
    String resolveFromPoint(LatLng point);
}
