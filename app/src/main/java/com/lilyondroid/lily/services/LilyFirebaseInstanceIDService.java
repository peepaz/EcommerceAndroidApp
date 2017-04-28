package com.lilyondroid.lily.services;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.lilyondroid.lily.Config;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by jason on 24/04/2017.
 */

public class LilyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    
    String TAG = "ptasdevz";
    
    @Override
    public void onTokenRefresh() {
        
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed Token: " + refreshedToken);
        
        
        sendRegistrationToServer(refreshedToken);

    }

    private void sendRegistrationToServer(String refreshedToken) {

        OkHttpClient client = Config.getOkHttpClient();
        MediaType mediaType = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW");
        RequestBody body = RequestBody.create(mediaType, "------WebKitFormBoundary7MA4YWxkTrZu0gW\r\nContent-Disposition: form-data; name=\"test\"\r\n\r\ntest\r\n------WebKitFormBoundary7MA4YWxkTrZu0gW--");
        Request request = new Request.Builder()
                .url("http://iid.googleapis.com/iid/v1/" + refreshedToken + "/rel/topics/coupon")
                .post(body)
                .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundary7MA4YWxkTrZu0gW")
                .addHeader("authorization", Config.FCM_API_KEY)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                Toast.makeText(LilyFirebaseInstanceIDService.this, "Failed to registered to lily's Messaging Service", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"Failed to Register to FCM");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d(TAG,"Successfully registered to FCM");


            }
        });

    }
}
