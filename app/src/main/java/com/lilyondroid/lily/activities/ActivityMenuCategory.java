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
import com.lilyondroid.lily.adapters.AdapterCategoryList_old;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityMenuCategory extends AppCompatActivity {
	
	GridView listCategory;
	ProgressBar prgLoading;
	TextView txtAlert;
	SwipeRefreshLayout swipeRefreshLayout = null;
	AdapterCategoryList_old cla;
	public static ArrayList<Long> Category_ID = new ArrayList<Long>();
	public static ArrayList<String> Category_name = new ArrayList<String>();
	public static ArrayList<String> Category_image = new ArrayList<String>();
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
        listCategory = (GridView) findViewById(R.id.listCategory);
        txtAlert = (TextView) findViewById(R.id.txtAlert);
        
        cla = new AdapterCategoryList_old(ActivityMenuCategory.this);

        // category API url
    	CategoryAPI = Config.ADMIN_PANEL_URL + "/api/get-all-category-data.php" +"?accesskey="+Config.AccessKey;
        
        // call asynctask class to request data from server
        new getDataTask().execute();

//		parseJSONData();




		// event listener to handle list when clicked
		listCategory.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
									long arg3) {
				// TODO Auto-generated method stub
				// go to menu page
//				Intent iMenuList = new Intent(ActivityMenuCategory.this, ActivityMenuList.class);
				Intent iMenuList = new Intent(ActivityMenuCategory.this, ActivityProductList.class);
				iMenuList.putExtra("category_id", Category_ID.get(position));
				iMenuList.putExtra("category_name", Category_name.get(position));
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
						listCategory.invalidateViews();
						clearData();
						new getDataTask().execute();
					}
				}, 3000);
			}
		});

		listCategory.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				boolean enable = false;
				if (listCategory != null && listCategory.getChildCount() > 0) {
					boolean firstItemVisible = listCategory.getFirstVisiblePosition() == 0;
					boolean topOfFirstItemVisible = listCategory.getChildAt(0).getTop() == 0;
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
			Intent iMyOrder = new Intent(ActivityMenuCategory.this, ActivityCart.class);
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
    void clearData(){
    	Category_ID.clear();
    	Category_name.clear();
    	Category_image.clear();
    }


	// asynctask class to handle parsing json in background
    public class getDataTask extends AsyncTask<Void, Void, Void>{
    	
    	// show progressbar first
    	getDataTask(){
    		if(!prgLoading.isShown()){
    			prgLoading.setVisibility(View.VISIBLE);
				txtAlert.setVisibility(View.GONE);
    		}
    	}
    	
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			// parse json data from server in background
			parseJSONData();
//			parseJSONData();
			return null;
		}
    	
		@Override
		protected void onPostExecute(Void result) {
			try{
				// TODO Auto-generated method stub
				// when finish parsing, hide progressbar
				prgLoading.setVisibility(View.GONE);

				// if internet connection and data available show data on list
				// otherwise, show alert text
				if ((Category_ID.size() > 0) && (IOConnect == 0)) {
					listCategory.setVisibility(View.VISIBLE);
					listCategory.setAdapter(cla);
				} else {
					txtAlert.setVisibility(View.VISIBLE);
				}
			}catch (Exception e){
				e.printStackTrace();
			}


		}
    }
    
    // method to parse json data from server
    public void parseJSONData(){
    	
    	clearData();
    	
    	try {
    		// request data from Category API
	        HttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
			HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
	        HttpUriRequest request = new HttpGet(CategoryAPI);
			HttpResponse response = client.execute(request);
			InputStream atomInputStream = response.getEntity().getContent();
			BufferedReader in = new BufferedReader(new InputStreamReader(atomInputStream));


//
//			Log.d("ptasdevz","teatata");
//			Log.d(Tag,okResponse.body().toString());


		
	        String line;
	        String str = "";
	        while ((line = in.readLine()) != null){
	        	str += line;
	        }
        
	        // parse json data and store into arraylist variables
			JSONObject json = new JSONObject(str);
			JSONArray data = json.getJSONArray("data");
			Log.d(Tag,data.toString() +"");

			for (int i = 0; i < data.length(); i++) {
			    JSONObject object = data.getJSONObject(i); 
			    
			    JSONObject category = object.getJSONObject("Category");
			    
			    Category_ID.add(Long.parseLong(category.getString("CategoryId")));
			    Category_name.add(category.getString("CategoryName"));
			    Category_image.add(category.getString("CategoryImage"));
			    Log.d("Category name", Category_name.get(i));
				    
			}
			Category_ID.add(Long.valueOf(data.length()));
			Category_name.add("Lily Products");
			Category_image.add("upload\\/images\\/lily_prod.png");

				
		} catch (MalformedURLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IOException e) {
		    // TODO Auto-generated catch block
			IOConnect = 1;
		    e.printStackTrace();
		} catch (JSONException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}	
    }

    public void parseJSONData1(){

		try {
			okClientResp.add("first");


			OkHttpClient client = Config.getOkHttpClient();
			Request okRequest = new Request.Builder()
					.url("https://138.197.40.125/api/products/")
					.get()
					.addHeader("authorization", "Token 9465145d11df5c44557a3ddfb257a28ae76beb0f")
					.build();

			client.newCall(okRequest).enqueue(new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {

				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {

					okClientResp.add("second");

					okClientResp.add(response.body().string());

//			okClientResp.add("third");

					Log.d(Tag,"product is loading");
					Log.d(Tag,""+okClientResp.size());


					for (String data : okClientResp) {
						try {
							JSONArray jsonArray = new JSONArray(data);

							Log.d(Tag, jsonArray.get(0).toString());


//							if (data.length() > 4000) {
//							Log.d(Tag, "data.length = " + data.length());
//							int chunkCount = data.length() / 4000;     // integer division
//							for (int i = 0; i <= chunkCount; i++) {
//								int max = 4000 * (i + 1);
//								if (max >= data.length()) {
//									Log.d(Tag, "1st chunk " + i + " of " + chunkCount + ":" + data.substring(4000 * i));
//								} else {
//									Log.d(Tag, "2nd chunk " + i + " of " + chunkCount + ":" + data.substring(4000 * i, max));
//								}
//							}
//						}
						} catch (JSONException e) {
							e.printStackTrace();
						}


					}


				}
			});

//

		} catch (Exception  e) {
			e.printStackTrace();
		}
	}
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	//adapterCategorylist.imageLoader.clearCache();
    	listCategory.setAdapter(null);
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
