package com.lilyondroid.lily.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.lilyondroid.lily.LilyApplication;
import com.lilyondroid.lily.R;
import com.lilyondroid.lily.activities.ActivityNotification;
import com.lilyondroid.lily.activities.MainActivity;
import com.lilyondroid.lily.utilities.LilyObserverable;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import static com.lilyondroid.lily.activities.MainActivity.BROADCAST_ACTION;
import static com.lilyondroid.lily.activities.MainActivity.RADIUS_FROM_DEL_POINT_IN_METERS;

/**
 * Created by jason on 24/04/2017.
 */

public class LilyFirebaseMessaging extends FirebaseMessagingService implements Observer{

    private String title = "";
    private Date expDate;
    private Date now;

    String TAG = "ptasdevz";
    private String code = "";
    private String simpleDate;

    public  LilyFirebaseMessaging(){
        super();
        LilyObserverable.getInstance().addObserver(this);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "from: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0){
            Log.d(TAG, "Message Payload: " + remoteMessage.getData());

            Map<String,String> data = remoteMessage.getData();


            code = data.get("code");
            title = data.get("title");
            String lat = data.get("lat");
            String lng = data.get("lng");
            String timestampExp = data.get("exp");
            if (!timestampExp.equalsIgnoreCase("undefine") && timestampExp != null){
                Timestamp timestamp = new Timestamp(Long.parseLong(timestampExp));
                expDate = new Date(timestamp.getTime());
                now = new Date(System.currentTimeMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy' 'HH:mm");
                simpleDate = simpleDateFormat.format(expDate);

                LilyApplication.titleList.add(this.title);
                LilyApplication.contentList.add("Coupon Code: " + this.code);
                LilyApplication.recvDatelist.add(this.now.toString());
                LilyApplication.expDateList.add("Exp: "+this.simpleDate);
                Log.d(TAG, "Date : " + expDate.toString() + " " + now);



            }

            if ((lat != null && !lat.equalsIgnoreCase("undefined"))
                    && (lng != null) && !lng.equalsIgnoreCase("undefine")) {

                if (now.getTime() <= expDate.getTime()) {

                    Intent mServiceIntent = new Intent(this, LilyGPSLocationService.class);

                    String storeLocationLat = lat;
                    String storeLocationLon = lng;

                    Bundle storeInfo = new Bundle();
                    storeInfo.putString("lat", storeLocationLat);
                    storeInfo.putString("lon", storeLocationLon);
                    storeInfo.putString("unitidx", "0");

                    mServiceIntent.putExtra(BROADCAST_ACTION, storeInfo);
                    startService(mServiceIntent);
                }
            }

        }

        if (remoteMessage.getNotification() != null){
            Log.d(TAG, "Message notification body: " + remoteMessage.getNotification());



        }

    }

    @Override
    public void onDeletedMessages() {

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void update(Observable o, Object intent) {
        Intent broadcastIntent = (Intent) intent;


        double currDistanceFromDeliveryPoint = broadcastIntent.getDoubleExtra("distance",0.0);

        Toast.makeText(this,"distance from outlet: " + currDistanceFromDeliveryPoint,Toast.LENGTH_SHORT).show();

        if (currDistanceFromDeliveryPoint <= RADIUS_FROM_DEL_POINT_IN_METERS){

                //Create notification
                Intent messageIntent = new Intent(this, LilyFirebaseMessaging.class);
                PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), messageIntent, 0);

                Notification noti = new Notification.Builder(this)
                        .setContentTitle(this.title)
                        .setContentText("Coupon code: " + this.code + "  Expires: "+simpleDate).setSmallIcon(R.drawable.ic_lily)
                        .setContentIntent(pIntent)
//                    .addAction(android.R.drawable.ic_menu_call, "Call", pIntent)
//                    .addAction(R.drawable.ic_lily, "More", pIntent)
                        .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                        .setTicker(this.title)
                        .setColor(getResources().getColor(R.color.ColorPrimary)).build();
//                    .addAction(R.drawable.ic_lily, "And more", pIntent).build();
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // hide the notification after its selected
                noti.flags |= Notification.FLAG_AUTO_CANCEL;

                notificationManager.notify(0, noti);

            Intent notificationIntent = new Intent(this, ActivityNotification.class);
            notificationIntent.setAction(Intent.ACTION_MAIN);
            notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);



        }

    }

//    private static NotificationCompat.Builder buildNotificationCommon(Context _context, ....) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(_context)
//                .setWhen(System.currentTimeMillis());
//        //Vibration
//        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
//
//        //LED
//        builder.setLights(Color.RED, 3000, 3000);
//
//        //Ton
//        builder.setSound(Uri.parse("uri://sadfasdfasdf.mp3"));
//
//        return builder;
//    }
}
