package com.lilyondroid.lily.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lilyondroid.lily.application.LilyApplication;
import com.lilyondroid.lily.home.MainActivity;
import com.lilyondroid.lily.services.LilyFirebaseMessaging;

/**
 *
 */

public class LilyBroadcastReceiver extends BroadcastReceiver {

    String TAG = "ptasdevz";

    public  LilyBroadcastReceiver(){

    }
    @Override
    public void onReceive(Context context, Intent intent) {


        String action = intent.getAction();

        switch (action){

           case LilyApplication.BROADCAST_ACTION :

               break;
           case LilyApplication.EXTENDED_DATA_STATUS:

               //Trigger Lily observable to update observers
               LilyObserverable.getInstance().updateValue(intent);

//               Toast.makeText(context,"got data froem intent",Toast.LENGTH_SHORT).show();

               break;

       }
    }
}
