package com.lilyondroid.lily.notification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.lilyondroid.lily.R;

import static com.lilyondroid.lily.application.LilyApplication.contentList;
import static com.lilyondroid.lily.application.LilyApplication.expDateList;
import static com.lilyondroid.lily.application.LilyApplication.recvDatelist;
import static com.lilyondroid.lily.application.LilyApplication.titleList;

public class ActivityNotification extends AppCompatActivity {

    private ListView list;
    String TAG = "ptasdevz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_activity_notification);
        }


        AdapterListNotification adapter = new AdapterListNotification(this, titleList,contentList,expDateList,recvDatelist);
        list = (ListView) findViewById(R.id.notification_list);
        list.setAdapter(adapter);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
