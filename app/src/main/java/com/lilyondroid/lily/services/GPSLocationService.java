package com.lilyondroid.lily.services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.lilyondroid.lily.activities.MainActivity;


/**
 * Created by jason on 22/04/2017.
 */

public class GPSLocationService extends Service {

    private static final String TAG = "ptasdevz";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;

    private final static double[] multipliers = {
            1.0,1.0936133,0.001,0.000621371192
    };


    public GPSLocationService() {

    }


    private class LocationListener implements android.location.LocationListener
    {
        Location mLastLocation;

        public LocationListener(String provider)
        {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);

            double currentLon = location.getLongitude();
            double currentLat = location.getLatitude();

//            MainActivity.currentLocation = location;

            Toast.makeText(getApplicationContext(), "Current Location: " + "lat: "+ currentLat + " long: " + currentLon
                    ,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[] {
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId)
//    {
//        Log.e(TAG, "onStartCommand");
//        super.onStartCommand(intent, flags, startId);
//        return START_STICKY;
//    }

    /**
     * Get the distance between the current location of the device and the physical location of the store.
     * @param currentLat
     * @param currentLon
     * @param storeLat
     * @param storeLon
     * @param unitindex
     * @return The distance in meters,  yard, kilometers miles,
     */
    private double getDistanceLilyOutlet(double currentLat, double currentLon, double storeLat, double storeLon, int unitindex){

       return  calcGeoDistance(currentLat,currentLon,storeLat,storeLon) * multipliers[unitindex];
//        String distanceText = "" + RoundDecimal(distance,2) + " " + unitstrings[unitindex];

//        Toast.makeText(this, distanceText, Toast.LENGTH_SHORT).show();
    }

    /**
     * Calculates the geo graphical distance betwewn two given coordinates.
     * @param startLat
     * @param startLong
     * @param endLat
     * @param endLong
     * @return The distance in meters between two coordinates
     */
    private double calcGeoDistance(final double startLat, final double startLong, final double endLat, final double endLong)
    {
        double distance = 0.0;

        try
        {
            final float[] results = new float[3];

            Location.distanceBetween(startLat, startLong, endLat, endLong, results);

            distance = results[0];
        }
        catch (final Exception ex)
        {
            distance = 0.0;
        }

        return distance;
    }
    @Override
    public void onCreate()
    {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy()
    {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
