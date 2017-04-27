package com.lilyondroid.lily;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.ToggleButton;

import com.lilyondroid.lily.utilities.GridViewItem;

import java.util.ArrayList;

/**
 * Created by jason on 24/04/2017.
 */

public class LilyApplication extends Application {

    //Products of the last seen view
    public static ArrayList<GridViewItem> productList = new ArrayList<>();

    public static ArrayList<String> titleList = new ArrayList<>();
    public static ArrayList<String> contentList = new ArrayList<>();
    public static ArrayList<String> expDateList = new ArrayList<>();
    public static ArrayList<String> recvDatelist = new ArrayList<>();

}
