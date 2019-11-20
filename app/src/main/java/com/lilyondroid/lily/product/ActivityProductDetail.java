package com.lilyondroid.lily.product;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.lilyondroid.lily.Config;
import com.lilyondroid.lily.R;
import com.lilyondroid.lily.utilities.GridViewItem;
import com.lilyondroid.lily.utilities.PicassoTrustAll;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityProductDetail extends AppCompatActivity {

    private String productId;
    private ImageView imgPreview;
    private TextView txtText;
    private TextView txtSubText;
    private WebView txtDescription;
    private CoordinatorLayout coordinatorLayout;
    private ProgressBar prgLoading;
    private TextView txtAlert;
    String Tag = "ptasdevz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_activity_product_detail);
        }


        Intent intent = getIntent();
        this.productId = intent.getStringExtra("product_id");


        Log.d(Tag, "product_id: " + productId);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("");

        this.imgPreview = (ImageView) findViewById(R.id.imgPreview);
        txtText = (TextView) findViewById(R.id.txtText);
        txtSubText = (TextView) findViewById(R.id.txtSubText);

        //txtDescription = (WebView) findViewById(R.id.txtDescription);
        txtDescription = (WebView) findViewById(R.id.txtDescription);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);

        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        txtAlert = (TextView) findViewById(R.id.txtAlert);


        parseJSONData(); //get data from server

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_product_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case android.R.id.home:
                // app icon in action bar clicked; go home
                this.finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }

    }


    // close database before back to previous page
    @Override
    public void onBackPressed() {

        finish();
    }


    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        // Ignore orientation change to keep activity from restarting
        super.onConfigurationChanged(newConfig);
    }

    //Display product
    private void loadProduct(Context context, GridViewItem product) {

        // TODO Auto-generated method stub
        // when finish parsing, hide progressbar

        Log.d(Tag, "product pos: " + productId);


        String productName = product.getTitle();
        DecimalFormat decimalFormat = new DecimalFormat("From: $###.##");
        String productPrice = decimalFormat.format(product.getPriceLower());
        String productDesc = product.getDescription();
        boolean isInStock = product.isInStock();
        String status = "Out Of Stock";
        if (isInStock) status = "In Stock";

        //prgLoading.setVisibility(View.GONE);
        // if internet connection and data available show data
        // otherwise, show alert text

        coordinatorLayout.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load(product.getImageUrl())
                .error(R.drawable.loading)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,

                                                Target<Drawable> target, boolean isFirstResource) {
                        prgLoading.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource,
                                                   boolean isFirstResource) {
                        prgLoading.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imgPreview);
//        PicassoTrustAll.getInstance(context).load(product.getImageUrl()).
//                placeholder(R.drawable.loading).into(imgPreview, new com.squareup.picasso.Callback() {
//            @Override
//            public void onSuccess() {
//
//                Bitmap bitmap = ((BitmapDrawable) imgPreview.getDrawable()).getBitmap();
//                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
//                    @Override
//                    public void onGenerated(Palette palette) {
//                    }
//                });
//
//            }

//            @Override
//            public void onError() {
//
//            }
//        });


        txtText.setText(productName);
        txtSubText.setText("Price : " + productPrice + " " + "\n" + "Status : " + status + "\n" + "Stock : " + "10");
        txtDescription.loadDataWithBaseURL("", productDesc, "text/html", "UTF-8", "");
        txtDescription.setBackgroundColor(Color.parseColor("#ffffff"));

        txtDescription.getSettings().setDefaultTextEncodingName("UTF-8");
        WebSettings webSettings = txtDescription.getSettings();
        Resources res = getResources();
        int fontSize = res.getInteger(R.integer.font_size);
        webSettings.setDefaultFontSize(fontSize);

    }

    private void parseJSONData() {


        try {

            //Get Image of product from server
            OkHttpClient client = Config.getOkHttpClient();

            Request okRequest = new Request.Builder()
                    .url(Config.LILY_SERVER_API + "/rest/products/id/" + this.productId)
                    .get()
                    .addHeader("authorization", Config.PRODUCT_TOKEN)
                    .build();

            client.newCall(okRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {


                    String data = response.body().string();
                    GridViewItem gridViewItem;
                    try {
                        final JSONObject product = new JSONObject(data);

                        String id = product.getString("id");
                        String desc = product.getString("description");
                        String title = product.getString("name");
                        String imageUrl = product.getString("imgUrl");
                        double priceLowerVal = product.getDouble("lowerPriceRange");
                        double priceUpperVal = product.getDouble("upperPriceRange");
                        boolean isInStock = product.getBoolean("inStock");
                        String image = Config.LILY_SERVER_IP + imageUrl;

                        gridViewItem = new GridViewItem(image, title, priceLowerVal,
                                priceUpperVal, desc, id, isInStock);

                        final GridViewItem finalGridViewItem = gridViewItem;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                prgLoading.setVisibility(View.GONE);

                                loadProduct(getApplicationContext(), finalGridViewItem);

                                // if data available show data on list
                                //otherwise, show alert text
                                if (finalGridViewItem.getId() == null) {
                                    txtAlert.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
