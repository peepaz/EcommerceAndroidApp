package com.lilyondroid.lily.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lilyondroid.lily.LilyApplication;
import com.lilyondroid.lily.R;
import com.lilyondroid.lily.adapters.AdapterList;
import com.lilyondroid.lily.adapters.AdapterListNotification;

import static com.lilyondroid.lily.LilyApplication.contentList;
import static com.lilyondroid.lily.LilyApplication.expDateList;
import static com.lilyondroid.lily.LilyApplication.recvDatelist;
import static com.lilyondroid.lily.LilyApplication.titleList;

public class ActivityNotification extends AppCompatActivity {

    private ListView list;

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
