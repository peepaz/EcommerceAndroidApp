package com.lilyondroid.lily.home;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.lilyondroid.lily.R;
import com.lilyondroid.lily.application.ActivityAbout;
import com.lilyondroid.lily.categories.ActivityCategory;
import com.lilyondroid.lily.authenticate.ActivityLogin;
import com.lilyondroid.lily.notification.ActivityNotification;
import com.lilyondroid.lily.application.ActivitySettings;
import com.lilyondroid.lily.customviews.LilyTextView;
import com.lilyondroid.lily.utilities.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Responsible for displaying  the home screen along with initializing and setting up the navigation
 * drawer.
 */
public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    private Dialog myDialog;
    private LilyTextView drawerSignin;
    private String Tag = "ptasdevz";
    private final int userValidationRequestCode = 1234;
    private String userToken;
    private ImageView profileViewOpen;
    private ImageView profileViewClose;
    private MenuItem drawerLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.main_drawer);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.frame_container, new FragmentHome()).commit();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                menuItem.setChecked(true);

                if (menuItem.getItemId() == R.id.home) {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }

                if (menuItem.getItemId() == R.id.categories) {
                    startActivity(new Intent(getApplicationContext(), ActivityCategory.class));
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


        // checking internet connection
        if (!utils.isNetworkAvailable(MainActivity.this)) {
            Toast.makeText(MainActivity.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onBackPressed() {
        finish();

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

