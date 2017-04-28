package com.lilyondroid.lily.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.lilyondroid.lily.application.LilyApplication;

/**
 * Responsible for obtaining the current location of the the device and calculate the distance from
 * another location that is given.
 */
public class LilyGPSLocationService extends IntentService {

    private static final String TAG = "ptasdevz";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    public static Location mLastLocation;

    public double storeLat;
    public double storeLon;
    public int unitIndex;

    private final static double[] multipliers = {
            1.0,1.0936133,0.001,0.000621371192
    };

    public LilyGPSLocationService() {
        super("gps_location_service");
    }


    @Override
    public void onCreate() {

        Log.e(TAG, "onCreate");
        initializeLocationManager();

        try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[0]);

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

        super.onCreate();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d(TAG,"start processing intent");

        Bundle data = intent.getBundleExtra(LilyApplication.BROADCAST_ACTION);
        if (data != null){

            storeLat = Double.parseDouble(data.getString("lat"));
            storeLon  = Double.parseDouble(data.getString("lon"));
            unitIndex  = Integer.parseInt(data.getString("unitidx"));

        }

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

            LilyGPSLocationService.mLastLocation = mLastLocation; Toast.makeText(getApplicationContext(), "Current Location: " + "lat: "+ currentLat + " long: " + currentLon
                    ,Toast.LENGTH_SHORT).show();


                   double distance = getDistanceFromDeliveryLocation(currentLat,currentLon,storeLat,storeLon,unitIndex);



            Log.d(TAG, "curr lat: " + currentLat + " curr lon: " + currentLon + " store lat: "
                    + storeLat  + "store lon: " + storeLon + " distance: " + distance + " index" + unitIndex);



            Intent localIntent = new Intent();
            localIntent.putExtra("extra","done");
            localIntent.putExtra("distance",distance);
            localIntent.setAction(LilyApplication.EXTENDED_DATA_STATUS); sendBroadcast(localIntent); //

            stopListening();

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

    /**
     * Get the distance between the current location of the device and the physical location of the delivery.
     * @param currentLat
     * @param currentLon
     * @param storeLat
     * @param storeLon
     * @param unitindex
     * @return The distance in meters,  yard, kilometers miles,
     */
    public double getDistanceFromDeliveryLocation(double currentLat, double currentLon, double storeLat, double storeLon, int unitindex){

        return  calcGeoDistance(currentLat,currentLon,storeLat,storeLon) * multipliers[unitindex];

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

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void stopListening(){

        Log.e(TAG, "onDestroy");
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }

        stopSelf();

    }

}
