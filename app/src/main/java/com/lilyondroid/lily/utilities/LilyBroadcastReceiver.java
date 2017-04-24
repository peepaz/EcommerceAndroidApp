package com.lilyondroid.lily.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.lilyondroid.lily.activities.MainActivity;

/**
 * Created by jason on 23/04/2017.
 */

public class LilyBroadcastReceiver extends BroadcastReceiver {

    String TAG = "ptasdevz";

    public  LilyBroadcastReceiver(){

    }
    @Override
    public void onReceive(Context context, Intent intent) {


        String action = intent.getAction();

        switch (action){

           case MainActivity.BROADCAST_ACTION :

               break;
           case MainActivity.EXTENDED_DATA_STATUS:

               //Update Main Activity
               LilyObserverable.getInstance().updateValue(intent);

//               Toast.makeText(context,"got data froem intent",Toast.LENGTH_SHORT).show();

               break;

       }
    }
}
