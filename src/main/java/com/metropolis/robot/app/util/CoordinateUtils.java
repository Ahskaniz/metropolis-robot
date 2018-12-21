package com.metropolis.robot.app.util;

import com.google.maps.model.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

// After trying my own implementation decided to use LatLngTool
// as it was taking too long to implement and was giving so many errors
public class CoordinateUtils {

    public double distanceInMeters(LatLng from, LatLng to) {
        return LatLngTool.distance(
                new com.javadocmd.simplelatlng.LatLng(from.lat, from.lng),
                new com.javadocmd.simplelatlng.LatLng(to.lat, to.lng),
                LengthUnit.METER);
    }

    public LatLng nextPoint(LatLng from, LatLng to, int meters) {
        com.javadocmd.simplelatlng.LatLng travel = LatLngTool.travel(
                new com.javadocmd.simplelatlng.LatLng(from.lat, from.lng),
                LatLngTool.initialBearing(
                        new com.javadocmd.simplelatlng.LatLng(from.lat, from.lng),
                        new com.javadocmd.simplelatlng.LatLng(to.lat, to.lng)
                ),
                meters,
                LengthUnit.METER
        );

        return new LatLng(travel.getLatitude(), travel.getLongitude());
    }

}
