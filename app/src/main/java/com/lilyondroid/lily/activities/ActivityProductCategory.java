package com.lilyondroid.lily.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lilyondroid.lily.Config;
import com.lilyondroid.lily.R;
import com.lilyondroid.lily.adapters.AdapterCategoryList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityProductCategory extends AppCompatActivity {
	
	GridView categoryList;
	ProgressBar prgLoading;
	TextView txtAlert;
	SwipeRefreshLayout swipeRefreshLayout = null;
	AdapterCategoryList adapterCategorylist;

	public static ArrayList<Integer> CategoryId = new ArrayList<Integer>();
	public static ArrayList<String> CategoryName = new ArrayList<String>();
	public static ArrayList<String> CategoryImage = new ArrayList<String>();
	public static ArrayList<String> okClientResp = new ArrayList<String>();


	String Tag = "ptasdevz";
	
	String CategoryAPI;
	int IOConnect = 0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.title_category);
		}

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        
        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        categoryList = (GridView) findViewById(R.id.listCategory);
        txtAlert = (TextView) findViewById(R.id.txtAlert);
        
        adapterCategorylist = new AdapterCategoryList(ActivityProductCategory.this);

        // category API url
    	CategoryAPI = Config.LILY_SERVER + "/api/product-classes/";

        // call asynctask class to request data from server
//        new getDataTask().execute();

		parseJSONData();




		// event listener to handle list when clicked
		categoryList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
									long arg3) {
				// TODO Auto-generated method stub
				// go to menu page
//				Intent iMenuList = new Intent(ActivityMenuCategory.this, ActivityMenuList.class);
				Intent iMenuList = new Intent(ActivityProductCategory.this, ActivityProductList.class);
				iMenuList.putExtra("category_id", CategoryId.get(position));
				iMenuList.putExtra("category_name", CategoryName.get(position));
				startActivity(iMenuList);
			}
		});

		// Using to refresh webpage when user swipes the screen
		swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						swipeRefreshLayout.setRefreshing(false);
						IOConnect = 0;
						categoryList.invalidateViews();
						clearData();
						parseJSONData();
					}
				}, 3000);
			}
		});

		categoryList.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				boolean enable = false;
				if (categoryList != null && categoryList.getChildCount() > 0) {
					boolean firstItemVisible = categoryList.getFirstVisiblePosition() == 0;
					boolean topOfFirstItemVisible = categoryList.getChildAt(0).getTop() == 0;
					enable = firstItemVisible && topOfFirstItemVisible;
				}
				swipeRefreshLayout.setEnabled(enable);
			}
		});
        
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_category, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.cart:
			// refresh action
			Intent iMyOrder = new Intent(ActivityProductCategory.this, ActivityCart.class);
			startActivity(iMyOrder);
			return true;
			
//		case R.id.refresh:
//			IOConnect = 0;
//			categoryList.invalidateViews();
//			clearData();
//			new getDataTask().execute();
//			return true;
			
		case android.R.id.home:
            // app icon in action bar clicked; go home
        	this.finish();
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
    
    // clear arraylist variables before used
    public void clearData(){
    	CategoryId.clear();
    	CategoryName.clear();
    	CategoryImage.clear();
    }

    
    // method to parse json data from server
    public void parseJSONData(){

		clearData();
//		Log.d(Tag,"tatatatata");

		try {


			OkHttpClient client = Config.getOkHttpClient();
			Request okRequest = new Request.Builder()
					.url(CategoryAPI)
					.get()
					.addHeader("authorization", Config.CATEGORY_TOKEN)
					.build();

			client.newCall(okRequest).enqueue(new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {

				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {


					String data = response.body().string();
					try {
						JSONArray cataegoryArray = new JSONArray(data);

//						Log.d(Tag,cataegoryArray.get(0).toString());

						for (int i=0; i<cataegoryArray.length(); i++) {

							JSONObject category = cataegoryArray.getJSONObject(i);
							CategoryName.add(category.getString("name"));
							CategoryId.add(i);

//							Log.d(Tag,"category name:"+category.getString("name"));

						}

						ActivityProductCategory.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {

								prgLoading.setVisibility(View.GONE);

								// if data available show data on list
								// otherwise, show alert text
//								Log.d(Tag,"" + CategoryName.size());
								if(CategoryName.size() > 0){
									categoryList.setVisibility(View.VISIBLE);
									categoryList.setAdapter(adapterCategorylist);
								}else{
									txtAlert.setVisibility(View.VISIBLE);
								}
							}
						});

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			});

		} catch (Exception  e) {
			e.printStackTrace();
		}
	}
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	//adapterCategorylist.imageLoader.clearCache();
    	categoryList.setAdapter(null);
    	super.onDestroy();
    }

    
    @Override
	public void onConfigurationChanged(final Configuration newConfig)
	{
	    // Ignore orientation change to keep activity from restarting
	    super.onConfigurationChanged(newConfig);
	}
    
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	super.onBackPressed();
    	finish();
    }


    
}
