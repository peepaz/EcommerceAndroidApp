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
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lilyondroid.lily.Config;
import com.lilyondroid.lily.R;
import com.lilyondroid.lily.adapters.AdapterProductList;

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
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityProductList extends AppCompatActivity {
	
	ListView productList;
	ProgressBar prgLoading;
	SwipeRefreshLayout swipeRefreshLayout = null;
	EditText edtKeyword;
	ImageButton btnSearch;
	TextView txtAlert;
	String Tag = "ptasdevz";

	// declare static variable to store tax and currency symbol
	public static double Tax;

	// declare adapter object to create custom product list
	AdapterProductList adapterProductList;
	
	// create arraylist variables to store data from server
//	public static ArrayList<Long> Menu_ID = new ArrayList<Long>();
	public static ArrayList<Integer> ProductId = new ArrayList<>();
	public static ArrayList<String> ProductName = new ArrayList<>();
	public static ArrayList<String> ProductDesc = new ArrayList<String>();
	public static ArrayList<Double> ProductPrice = new ArrayList<>();

//	public static ArrayList<String> Menu_image = new ArrayList<String>();
	
	String ProductsAPI;
	String TaxCurrencyAPI;
	int IOConnect = 0;
	long Category_ID;
	String Category_name;
	String Keyword;
	
	// create price format
	DecimalFormat formatData = new DecimalFormat("#.##");
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		final android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			getSupportActionBar().setTitle(R.string.title_menu);
		}

		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
		swipeRefreshLayout.setColorSchemeResources(R.color.red, R.color.black, R.color.grey);

        prgLoading = (ProgressBar) findViewById(R.id.prgLoading);
        productList = (ListView) findViewById(R.id.listMenu);
        edtKeyword = (EditText) findViewById(R.id.edtKeyword);
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        txtAlert = (TextView) findViewById(R.id.txtAlert);
        
        // menu API url
//        ProductsAPI = Config.ADMIN_PANEL_URL+"/api/get-menu-data-by-category-id.php"+"?accesskey="+Config.AccessKey+"&category_id=";
		ProductsAPI = Config.LILY_SERVER + "/api/products";
        // tax and currency API url
//        TaxCurrencyAPI = Config.ADMIN_PANEL_URL+"/api/get-tax-and-currency.php"+"?accesskey="+Config.AccessKey;
        
        // get category id and category name that sent from previous page
        Intent iGet = getIntent();
        Category_ID = iGet.getLongExtra("category_id",0);
        Category_name = iGet.getStringExtra("category_name");
//        ProductsAPI += CategoryId;
        
        // set category name to textview
//        txtTitle.setText(CategoryName);
		parseJSONData();

		adapterProductList = new AdapterProductList(ActivityProductList.this);
       
        // call asynctask class to request tax and currency data from server
//        new getTaxCurrency().execute();
		
        // event listener to handle search button when clicked
		btnSearch.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// get keyword and send it to server
				try {
					Keyword = URLEncoder.encode(edtKeyword.getText().toString(), "utf-8");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ProductsAPI += "&keyword=" + Keyword;
				IOConnect = 0;
				productList.invalidateViews();
				parseJSONData();
			}
		});
		
		// event listener to handle list when clicked
		productList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
									long arg3) {
				// TODO Auto-generated method stub
				// go to menu detail page
				Intent iDetail = new Intent(ActivityProductList.this, ActivityMenuDetail.class);
//				iDetail.putExtra("menu_id", Menu_ID.get(position));
				startActivity(iDetail);
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
						productList.invalidateViews();
						parseJSONData();
					}
				}, 3000);
			}
		});

		productList.setOnScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				boolean enable = false;
				if (productList != null && productList.getChildCount() > 0) {
					boolean firstItemVisible = productList.getFirstVisiblePosition() == 0;
					boolean topOfFirstItemVisible = productList.getChildAt(0).getTop() == 0;
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
			Intent i = new Intent(ActivityProductList.this, ActivityCart.class);
			startActivity(i);
			return true;
			
		case R.id.refresh:
			IOConnect = 0;
			productList.invalidateViews();
			clearData();
//			new getDataTask().execute();
			parseJSONData();
			return true;			
			
		case android.R.id.home:
            // app icon in action bar clicked; go home
        	this.finish();
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
    
    // asynctask class to handle parsing json in background
    public class getTaxCurrency extends AsyncTask<Void, Void, Void>{
    	
    	// show progressbar first
    	getTaxCurrency(){
    		if(!prgLoading.isShown()){
    			prgLoading.setVisibility(View.VISIBLE);
				txtAlert.setVisibility(View.GONE);
    		}
    	}
    	
		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			// parse json data from server in background
			parseJSONDataTax();
			return null;
		}
    	
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			// when finish parsing, hide progressbar
			prgLoading.setVisibility(View.GONE);
			// if internet connection and data available request menu data from server
			// otherwise, show alert text
//			if((Currency != null) && IOConnect == 0){
//				new getDataTask().execute();
//			}else{
//				txtAlert.setVisibility(View.VISIBLE);
//			}
		}
    }

    // method to parse json data from server
	public void parseJSONDataTax(){
		try {
	        // request data from tax and currency API
	        HttpClient client = new DefaultHttpClient();
			HttpConnectionParams.setConnectionTimeout(client.getParams(), 15000);
			HttpConnectionParams.setSoTimeout(client.getParams(), 15000);
	        HttpUriRequest request = new HttpGet(TaxCurrencyAPI);
			HttpResponse response = client.execute(request);
			InputStream atomInputStream = response.getEntity().getContent();
	
			BufferedReader in = new BufferedReader(new InputStreamReader(atomInputStream));
		        
	        String line;
	        String str = "";
	        while ((line = in.readLine()) != null){
	        	str += line;
	        }
    
	        
	        // parse json data and store into tax and currency variables
			JSONObject json = new JSONObject(str);
			JSONArray data = json.getJSONArray("data"); // this is the "items: [ ] part
			
			JSONObject object_tax = data.getJSONObject(0); 
			JSONObject tax = object_tax.getJSONObject("tax_n_currency");
			    
			Tax = Double.parseDouble(tax.getString("Value"));
			
			JSONObject object_currency = data.getJSONObject(1); 
			JSONObject currency = object_currency.getJSONObject("tax_n_currency");
			    
//			Currency = currency.getString("Value");
			
			
		} catch (MalformedURLException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (IOException e) {
			IOConnect = 1;
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		} catch (JSONException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}	
	}
    
	// clear arraylist variables before used
    void clearData(){
		ProductId.clear();
    	ProductName.clear();
		ProductDesc.clear();
		ProductPrice.clear();
//    	ProductPrice.clear();
//    	Menu_image.clear();
    }
    
    // method to parse json data from server
    public void parseJSONData(){
    	
    	clearData();
//		Log.d(Tag,"tatatatata");

		try {


			OkHttpClient client = Config.getOkHttpClient();
			Request okRequest = new Request.Builder()
					.url(Config.LILY_SERVER +"/api/products/")
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
					try {
						JSONArray productArray = new JSONArray(data);

						Log.d(Tag,productArray.get(0).toString());

						for (int i=0; i<productArray.length(); i++) {

							JSONObject product = productArray.getJSONObject(i);
							ProductId.add(product.getInt("id"));
							ProductName.add(product.getString("name"));
							ProductDesc.add(product.getString("description"));
							JSONArray priceRange =  product.getJSONArray("get_price_range");
							JSONArray priceLower = priceRange.getJSONArray(0);
							JSONArray priceUpper = priceRange.getJSONArray(1);
							double price = priceUpper.getDouble(0);
//							Log.d(Tag,"uppper price:"+price);
							ProductPrice.add(price);

						}

						ActivityProductList.this.runOnUiThread(new Runnable() {
							@Override
							public void run() {

								prgLoading.setVisibility(View.GONE);

								// if data available show data on list
								// otherwise, show alert text
								Log.d(Tag,"" + ProductName.size());
								if(ProductName.size() > 0){
									productList.setVisibility(View.VISIBLE);
									productList.setAdapter(adapterProductList);
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
    	//adapterProductList.imageLoader.clearCache();
    	productList.setAdapter(null);
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
