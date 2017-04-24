package com.lilyondroid.lily.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.lilyondroid.lily.Config;
import com.lilyondroid.lily.LilyApplication;
import com.lilyondroid.lily.R;
import com.lilyondroid.lily.utilities.DBHelper;
import com.lilyondroid.lily.utilities.GridViewItem;
import com.lilyondroid.lily.utilities.PicassoTrustAll;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.lilyondroid.lily.activities.ActivityMenuList.Menu_image;

public class ActivityProductDetail extends AppCompatActivity {

    private int product_pos;
    private ImageView imgPreview;
    private TextView txtText;
    private TextView txtSubText;
    private WebView txtDescription;
    private CoordinatorLayout coordinatorLayout;
    private ProgressBar prgLoading;
    private TextView txtAlert;
    private DBHelper dbhelper;
    String Tag = "ptasdevz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_1);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_activity_product_detail);
        }


        Intent intent = getIntent();
        this.product_pos = intent.getIntExtra("product_pos",0);


        Log.d(Tag,"product pos: " + product_pos);


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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.btnAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputDialog();
            }
        });

        com.github.clans.fab.FloatingActionButton fab2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.cart);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ActivityCart.class));
            }
        });

        com.github.clans.fab.FloatingActionButton fab3 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.checkout);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ActivityCheckout.class));
            }
        });

        com.github.clans.fab.FloatingActionButton fab4 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.save);
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                (new ActivityMenuDetail.SaveTask(ActivityMenuDetail.this)).execute(Config.ADMIN_PANEL_URL + "/" + Menu_image);
            }
        });

        //imageLoader = new ImageLoader(ActivityMenuDetail.this);
        dbhelper = new DBHelper(this);

        this.loadProduct(this);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
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

            case R.id.buy:
                inputDialog();
                return true;

            case R.id.cart:
                startActivity(new Intent(getApplicationContext(), ActivityCart.class));
                return true;

            case R.id.checkout:
                startActivity(new Intent(getApplicationContext(), ActivityCheckout.class));
                return true;

//            case R.id.save:
//                (new ActivityMenuDetail.SaveTask(ActivityMenuDetail.this)).execute(Config.ADMIN_PANEL_URL + "/" + Menu_image);
//                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    // method to show number of order form
    void inputDialog() {

        // open database first
        try {
            dbhelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle(R.string.order);
        alert.setMessage(R.string.number_order);
        alert.setCancelable(false);
        final EditText edtQuantity = new EditText(this);
        int maxLength = 3;
        edtQuantity.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
        edtQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(edtQuantity);

//        alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                String temp = edtQuantity.getText().toString();
//                int quantity = 0;
//
//                // when add button clicked add menu to order table in database
//                if (!temp.equalsIgnoreCase("")) {
//                    quantity = Integer.parseInt(temp);
//                    Toast.makeText(getApplicationContext(), "Success add product to cart", Toast.LENGTH_SHORT).show();
//
//                    if (dbhelper.isDataExist(Menu_ID)) {
//                        dbhelper.updateData(Menu_ID, quantity, (Menu_price * quantity));
//                    } else {
//                        dbhelper.addData(Menu_ID, Menu_name, quantity, (Menu_price * quantity));
//                    }
//                } else {
//                    dialog.cancel();
//                }
//            }
//        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                // when cancel button clicked close dialog
                dialog.cancel();
            }
        });

        alert.show();
    }

    // close database before back to previous page
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        dbhelper.close();
        finish();
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        //imageLoader.clearCache();
        super.onDestroy();
    }



    @Override
    public void onConfigurationChanged(final Configuration newConfig) {
        // Ignore orientation change to keep activity from restarting
        super.onConfigurationChanged(newConfig);
    }

    public void loadProduct (Context context){

        // TODO Auto-generated method stub
        // when finish parsing, hide progressbar

        Log.d(Tag,"product pos: " + product_pos);

        GridViewItem product = LilyApplication.productList.get(this.product_pos);

        Log.d(Tag, product.toString());

        String productName = product.getTitle();
        DecimalFormat decimalFormat = new DecimalFormat("From: $###.##");
        String productPrice = decimalFormat.format(product.getPriceLower());
        String productDesc = product.getDescription();
        boolean isInStock = product.isInStock();

        prgLoading.setVisibility(View.GONE);
        // if internet connection and data available show data
        // otherwise, show alert text

        coordinatorLayout.setVisibility(View.VISIBLE);

        PicassoTrustAll.getInstance(context).load(product.getImageUrl()).
                placeholder(R.drawable.loading).into(imgPreview, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {

                Bitmap bitmap = ((BitmapDrawable) imgPreview.getDrawable()).getBitmap();
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                    }
                });

            }

            @Override
            public void onError() {

            }
        });


        txtText.setText(productName);
        txtSubText.setText("Price : " + productPrice + " " + "\n" + "Status : " + isInStock + "\n" + "Stock : " + "10");
        txtDescription.loadDataWithBaseURL("", productDesc, "text/html", "UTF-8", "");
        txtDescription.setBackgroundColor(Color.parseColor("#ffffff"));

        txtDescription.getSettings().setDefaultTextEncodingName("UTF-8");
        WebSettings webSettings = txtDescription.getSettings();
        Resources res = getResources();
        int fontSize = res.getInteger(R.integer.font_size);
        webSettings.setDefaultFontSize(fontSize);

    }
    public void parseJSONData(){

        try {

            //Get Image of product from server
            Request okRequest = new Request.Builder()
//                    .url(Config.LILY_SERVER +"/api/products/category/" + catId)
                    .get()
                    .addHeader("authorization", Config.PRODUCT_TOKEN)
                    .build();

//            client.newCall(okRequest).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//
//
//                    String data = response.body().string();
//                    try {
//                        JSONArray productArray = new JSONArray(data);
//
//                        Log.d(Tag,productArray.get(0).toString());
//
//                        for (int i=0; i<productArray.length(); i++) {
//
//                            int f = i;
//                            if (f==0) f = 1;
//                            else f = i%22;
//
//                            JSONObject product = productArray.getJSONObject(i);
//
//                            String id = product.getString("id");
//                            String desc = product.getString("description");
//                            String title = product.getString("name");
//                            boolean isInStock = product.getBoolean("is_in_stock");
//                            String image = Config.LILY_SERVER + "/static/images/products/" +f+".jpg";
//
//                            JSONArray priceRange =  product.getJSONArray("get_price_range");
//                            JSONArray priceLower = priceRange.getJSONArray(0);
//                            JSONArray priceUpper = priceRange.getJSONArray(1);
//
//                            double priceUpperVal = priceUpper.getDouble(0);
//                            double priceLowerVal = priceLower.getDouble(0);
//
//                            GridViewItem gridViewItem = new GridViewItem(image,title,priceLowerVal,
//                                    priceUpperVal,desc,id,isInStock);
//
//                            gridViewItemList.add(gridViewItem);
//
////							Log.d(Tag,"uppper price:"+price);
//
//                        }
//
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                prgLoading.setVisibility(View.GONE);
//
//                                // if data available show data on list
//                                // otherwise, show alert text
//                                Log.d(Tag,"" + gridViewItemList.size());
//                                if(gridViewItemList.size() > 0){
//                                    categoryList.setVisibility(View.VISIBLE);
//                                    categoryList.setAdapter(adapterProductList);
//                                }else{
//                                    txtAlert.setVisibility(View.VISIBLE);
//                                }
//                            }
//                        });
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            });



        } catch (Exception  e) {
            e.printStackTrace();
        }
    }
}
