package com.lilyondroid.lily.analytics;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.onesignal.OSNotification;
import com.onesignal.OSNotificationAction;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.lilyondroid.lily.R;

import org.json.JSONObject;

public class Analytics extends Application {

    private Tracker mTracker;

    String Tag = "ptasdevz";

    @Override
    public void onCreate() {
        super.onCreate();

        // Logging set to help debug issues, remove before releasing your app.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.WARN);

        OneSignal.startInit(this)
                .setNotificationOpenedHandler(new ExampleNotificationOpenedHandler())
                .setNotificationReceivedHandler(new OneSignalNotificationReceivedHandler())
                .autoPromptLocation(true)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.None)
                .init();
    }

    public synchronized Tracker getTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.setDryRun(!AnalyticsConfig.ANALYTICS);
            mTracker = analytics.newTracker(R.xml.analytics_app_tracker);
        }
        return mTracker;
    }

    private class ExampleNotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {
        /**
         * Callback to implement in your app to handle when a notification is opened from the Android status bar or
         * a new one comes in while the app is running.
         * This method is located in this Application class as an example, you may have any class you wish implement NotificationOpenedHandler and define this method.
         *
         * @param result        The information returned from a notification the user received.
         */

         @Override
        public void notificationOpened(OSNotificationOpenResult result) {
             Toast.makeText(getApplicationContext(),"Notification opened",Toast.LENGTH_SHORT).show();

             String additionalMessage = "";

            OSNotificationAction.ActionType actionType = result.action.type;
            JSONObject additionalData = result.notification.payload.additionalData;
            String message = result.notification.payload.title;

            try {
                if (additionalData != null) {

                    if (additionalData.has("actionSelected"))
                        additionalMessage += "Pressed ButtonID: " + additionalData.getString("actionSelected");

                    additionalMessage = message + "\nFull additionalData:\n" + additionalData.toString();
                }

                Log.d(Tag, "message:\n" + message + "\nadditionalMessage:\n" + additionalMessage);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }


    }

    private class OneSignalNotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {

        String TAG = "ptasdevz";

        @Override
        public void notificationReceived(OSNotification notification) {

//            Toast.makeText(getApplicationContext(),"Notification received",Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Notification Recveivged");


            JSONObject data = notification.payload.additionalData;
            String customKey;

            if (data != null) {
                customKey = data.optString("customkey", null);
                if (customKey != null)
                    Log.i   (TAG, "customkey set with value: " + customKey);
            }
        }

    }

}