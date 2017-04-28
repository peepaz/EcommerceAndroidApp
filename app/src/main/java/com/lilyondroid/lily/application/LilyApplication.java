package com.lilyondroid.lily.application;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.ToggleButton;

import com.lilyondroid.lily.utilities.GridViewItem;

import java.util.ArrayList;

/**
 * Created by jason on 24/04/2017.
 */

public class LilyApplication extends Application {

    public static final double RADIUS_FROM_DEL_POINT_IN_METERS = 8046.72; //5 MILES

    public static final String BROADCAST_ACTION =
            "com.lilyondroid.lily.BROADCAST";

    public static final String EXTENDED_DATA_STATUS =
            "com.lilyondroid.lily.STATUS";

    //Products of the last seen view
    public static ArrayList<GridViewItem> productList = new ArrayList<>();

    public static ArrayList<String> titleList = new ArrayList<>();
    public static ArrayList<String> contentList = new ArrayList<>();
    public static ArrayList<String> expDateList = new ArrayList<>();
    public static ArrayList<String> recvDatelist = new ArrayList<>();

}
