package com.lilyondroid.lily.application;

import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;

import com.lilyondroid.lily.R;

public class ActivitySettings extends AppCompatActivity {

    private ToggleButton notificationSwitch;
    private SharedPreferences sharedPreferences;
    String TAG = "ptasdevz";


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_activity_settings);
        }
        sharedPreferences = getPreferences(MODE_PRIVATE);
        boolean notifcationButtonState = sharedPreferences.getBoolean("NOTIFICATION_STATE",false);


        notificationSwitch = (ToggleButton) findViewById(R.id.notification_switch);
        notificationSwitch.setChecked(notifcationButtonState);


        notificationSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences = getPreferences(MODE_PRIVATE);
                boolean buttonState = sharedPreferences.getBoolean("NOTIFICATION_STATE",false);
                ToggleButton tg = (ToggleButton) v;

                if (buttonState){
                    ((ToggleButton) v).setChecked(false);
                    saveButtonState(false);

                }
                else{
                    tg.setChecked(true);
                    saveButtonState(true);

                }

                Log.d(TAG, buttonState + "");


            }
        });



    }

    public void saveButtonState(boolean pressed) {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("NOTIFICATION_STATE", pressed);
        editor.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

}
