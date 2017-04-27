package com.lilyondroid.lily.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lilyondroid.lily.LilyApplication;
import com.lilyondroid.lily.customfonts.LilyEditText;
import com.lilyondroid.lily.customfonts.LilyTextView;
import com.lilyondroid.lily.fragments.FragmentHome;
import com.lilyondroid.lily.services.LilyFirebaseMessaging;
import com.lilyondroid.lily.services.LilyGPSLocationService;
import com.lilyondroid.lily.utilities.LilyObserverable;
import com.onesignal.OneSignal;
import com.lilyondroid.lily.R;
import com.lilyondroid.lily.analytics.Analytics;
import com.lilyondroid.lily.utilities.DBHelper;
import com.lilyondroid.lily.utilities.utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends AppCompatActivity {

    static DBHelper dbhelper;
    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    static final String TAG = "MainActivity";
    private Dialog myDialog;
    private LilyEditText username;
    private LilyEditText password;
    private boolean isLoggedIn = false;

    //Views that dynamically changed depnending on logged in status
    private LilyTextView drawerUserEmail;
    private LilyTextView drawerUsername;
    private LilyTextView drawerSignin;
    private String Tag = "ptasdevz";
    private final int userValidationRequestCode = 1234;
    private String userToken;
    private ImageView profileViewOpen;
    private ImageView profileViewClose;
    private MenuItem drawerLogout;

    public static final double RADIUS_FROM_DEL_POINT_IN_METERS = 8046.72; //5 MILES

    public static final String BROADCAST_ACTION =
            "com.lilyondroid.lily.BROADCAST";

    public static final String EXTENDED_DATA_STATUS =
            "com.lilyondroid.lily.STATUS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
//            @Override
//            public void idsAvailable(String userId, String registrationId) {
//                String text = "OneSignal UserID:\n" + userId + "\n\n";
//
//                if (registrationId != null) {
//                    text += "Google Registration Id:\n" + registrationId;
//                    Toast.makeText(MainActivity.this, "subscribed for push", Toast.LENGTH_LONG).show();
//                }
//                else{
//
//                    text += "Google Registration Id:\nCould not subscribe for push";
//                }
//
//                TextView textView = (TextView) findViewById(R.id.debug_view);
//                textView.setText(text);
//            }
//        });





        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.main_drawer);
//        View mNavigationViewHeader = mNavigationView.getHeaderView(R.layout.nav_drawer_header_sign_in);
//        drawerSignin = (LilyTextView) mNavigationViewHeader.findViewById(R.id.drawer_sign_in);


        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
//        mFragmentTransaction.replace(R.id.frame_container, new FragmentHome()).commit();
        mFragmentTransaction.replace(R.id.frame_container, new FragmentHome()).commit();


        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                menuItem.setChecked(true);
                //mDrawerLayout.closeDrawers();
                //setTitle(menuItem.getTitle());

                if (menuItem.getItemId() == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }

                if (menuItem.getItemId() == R.id.categories) {
                    startActivity(new Intent(getApplicationContext(), ActivityCategory.class));
                }
                if (menuItem.getItemId() == R.id.cart) {
                    startActivity(new Intent(getApplicationContext(), ActivityCart.class));
                }

                if (menuItem.getItemId() == R.id.checkout) {
                    startActivity(new Intent(getApplicationContext(), ActivityCheckout.class));
                }

                if (menuItem.getItemId() == R.id.profile) {
                    startActivity(new Intent(getApplicationContext(), ActivityProfile.class));
                }


                if (menuItem.getItemId() == R.id.about) {
                    startActivity(new Intent(getApplicationContext(), ActivityAbout.class));
                }

                if (menuItem.getItemId() == R.id.settings) {
                    startActivity(new Intent(getApplicationContext(), ActivitySettings.class));
                }

                if (menuItem.getItemId() == R.id.notification) {
                    startActivity(new Intent(getApplicationContext(), ActivityNotification.class));
                }

                return false;
            }



        });

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
//
//        mDrawerLayout.setDrawerListener(mDrawerToggle);
//
//        mDrawerToggle.syncState();

        // init analytics tracker
        ((Analytics) getApplication()).getTracker();

        // checking internet connection
        if (!utils.isNetworkAvailable(MainActivity.this)) {
            Toast.makeText(MainActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        dbhelper = new DBHelper(this);

        // create database
        try {
            dbhelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        // then, the database will be open to use
        try {
            dbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        if (dbhelper.isPreviousDataExist()) {
            showAlertDialog();
        }

        isStoragePermissionGranted();

    }

    // show activity_confirm dialog to ask user to delete previous order or not
    void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.confirm);
        builder.setMessage(getString(R.string.db_exist_alert));
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.option_yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                // delete order data when yes button clicked
                dbhelper.deleteAllData();
                dbhelper.close();

            }
        });

        builder.setNegativeButton(getString(R.string.option_no), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                // close dialog when no button clicked
                dbhelper.close();
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            dbhelper.deleteAllData();
            dbhelper.close();
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == this.userValidationRequestCode) {
            if (resultCode == RESULT_OK) {
                try {
                    String loginDataStr = data.getData().toString();
                    JSONObject loginDataJsonObj = new JSONObject(loginDataStr);
                    this.userToken = loginDataJsonObj.getString("token");

                    Toast.makeText(MainActivity.this, this.userToken, Toast.LENGTH_LONG).show();

                    updateDrawer(loginDataJsonObj);

                } catch (JSONException e) {
                    Log.d(Tag, e.getMessage());
                }
            }
        }
    }

    private void updateDrawer(JSONObject userLoginData) throws JSONException {

        RelativeLayout navHeader =   (RelativeLayout) findViewById(R.id.nav_header);
        navHeader.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,500));
        View headerSignedIn =  mNavigationView.findViewById(R.id.header_signed_in);
        View headerSignIn =  mNavigationView.findViewById(R.id.header_sign_in);
        LilyTextView signedInName  = (LilyTextView) findViewById(R.id.signed_in_name);
        LilyTextView signedInEmail  = (LilyTextView) findViewById(R.id.signed_in_user_email);

        String userEmail = userLoginData.getString("user_email");
        String name = userLoginData.getString("user_name");
        String nameSentenceCase =  name.toUpperCase().substring(0,1) + name.substring(1);

        //update text views on sigined in navigation headder
        signedInEmail.setText(userEmail);
        signedInName.setText(nameSentenceCase);

        //Swap navigation headers views.
        headerSignedIn.setVisibility(View.VISIBLE);
        headerSignIn.setVisibility(View.GONE);


//        mNavigationView.ad(headerView);


//        mNavigationView.getHeaderView(0).setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        final Menu navMenu = mNavigationView.getMenu();

        drawerSignin = (LilyTextView) findViewById(R.id.drawer_sign_in);
        profileViewOpen = (ImageView) findViewById(R.id.profile_view_open);
        profileViewClose = (ImageView) findViewById(R.id.profile_view_close);
        drawerLogout = (MenuItem) navMenu.findItem(R.id.drawer_logout);




        //Tiggers user signing
        drawerSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(new Intent(getApplicationContext(),ActivityLogin.class), userValidationRequestCode);


            }
        });

        drawerLogout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                logoutDialog();
                return true;
            }
        });


        //Switch drawer view to view profile option and drawerLogout
        profileViewOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                drawerStdView.setVisible(false);
//                drawerProfileView.setVisible(true);
                v.setVisibility(View.GONE);
                profileViewClose.setVisibility(View.VISIBLE);
                navMenu.setGroupVisible(R.id.drawer_std_view_1, false);
                navMenu.setGroupVisible(R.id.drawer_std_view_2, false);
                navMenu.setGroupVisible(R.id.drawer_profile_view, true);


            }
        });

        //Switch drawer view to close profile option and drawerLogout
        profileViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                drawerProfileView.setVisible(false);
//                drawerStdView.setVisible(true);
                v.setVisibility(View.GONE);
                profileViewOpen.setVisibility(View.VISIBLE);
                navMenu.setGroupVisible(R.id.drawer_profile_view, false);
                navMenu.setGroupVisible(R.id.drawer_std_view_1, true);
                navMenu.setGroupVisible(R.id.drawer_std_view_2, true);
            }
        });



        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

//        MenuItem logout_confirmation = (MenuItem) menu.findItem(R.id.action_login);
//        MenuItem drawerLogout = (MenuItem) menu.findItem(R.id.action_logout);
////
//        //Manage logout_confirmation and drawerLogout options in toolbar
//        if (!isLoggedIn){
//
//            drawerLogout.setVisible(false);
//            if (!logout_confirmation.isVisible()) logout_confirmation.setVisible(true);
//        }
//        else {
//            logout_confirmation.setVisible(false);
//            if (!drawerLogout.isVisible()) drawerLogout.setVisible(true);
//
//        }

//        final Menu appMenu = menu;

//        drawerStdView =  menu.findItem(R.id.drawer_std_view);
//        drawerProfileView =menu.findItem(R.id.drawer_profile_view);
//        MenuItem product =  menu.findItem(R.id.product);
//        product.setVisible(false);



        super.onPrepareOptionsMenu(menu);

        return true;


    }


    private void adjustViewsToReflectLogout() {
        final Menu navMenu = mNavigationView.getMenu();

        //Resset drawer items
        profileViewOpen.setVisibility(View.VISIBLE);
        profileViewClose.setVisibility(View.GONE);
        navMenu.setGroupVisible(R.id.drawer_profile_view, false);
        navMenu.setGroupVisible(R.id.drawer_std_view_1, true);
        navMenu.setGroupVisible(R.id.drawer_std_view_2, true);

        RelativeLayout navHeader =   (RelativeLayout) findViewById(R.id.nav_header);
        navHeader.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,220));
        View headerSignedIn =  mNavigationView.findViewById(R.id.header_signed_in);
        View headerSignIn =  mNavigationView.findViewById(R.id.header_sign_in);

        //Swap navigation headers views.
        headerSignedIn.setVisibility(View.GONE);
        headerSignIn.setVisibility(View.VISIBLE);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case android.R.id.home:
                return true;
            case R.id.action_settings:
                startActivity(new Intent(getApplicationContext(), ActivitySettings.class));
                return true;

            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    private void logoutDialog()
    {
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.logout_confirmation);
        myDialog.setCancelable(false);
        Button logoutYes = (Button) myDialog.findViewById(R.id.logout_yes);
        Button logoutNo = (Button) myDialog.findViewById(R.id.logout_no);

        myDialog.show();


        logoutYes.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

                //your logout_confirmation calculation goes here
               adjustViewsToReflectLogout();

               myDialog.hide();

            }
        });
        logoutNo.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {

               myDialog.hide();

            }
        });


    }


}

