package com.metropolis.robot.app.util;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;

import java.util.List;

public class PolylineUtils {

    public static List<LatLng> getPolyline(String polyline) {
        try {
            return PolylineEncoding.decode(polyline);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid polyline");
        }
    }

}
