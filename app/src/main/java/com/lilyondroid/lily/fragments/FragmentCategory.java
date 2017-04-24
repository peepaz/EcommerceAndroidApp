package com.lilyondroid.lily.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lilyondroid.lily.Config;
import com.lilyondroid.lily.LilyApplication;
import com.lilyondroid.lily.R;
import com.lilyondroid.lily.activities.ActivityProductDetail;
import com.lilyondroid.lily.adapters.GridviewAdapter;
import com.lilyondroid.lily.customfonts.LilyTextView;
import com.lilyondroid.lily.utilities.ExpandableHeightGridView;
import com.lilyondroid.lily.utilities.GridViewItem;

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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentCategory.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentCategory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCategory extends Fragment {

    String Tag = "ptasdevz";

    private static final String CAT_TITLE = "catTitle";
    private static final String CAT_ID = "catId";

    private String catTitle;
    private String catId;

    private ExpandableHeightGridView categoryList;
    private ProgressBar prgLoading;
    private LilyTextView txtAlert;
    private SwipeRefreshLayout swipeRefreshLayout = null;
    private GridviewAdapter adapterProductList;


    private OnFragmentInteractionListener mListener;
    String ProductsAPI;
    int IOConnect = 0;


    private ArrayList<GridViewItem> gridViewItemList = new ArrayList<>();


    public FragmentCategory() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param catTitle Parameter 1.
     * @return A new instance of fragment FragmentCategory.
     */
    public static FragmentCategory newInstance(String catTitle, String catId) {
        FragmentCategory fragment = new FragmentCategory();
        Bundle args = new Bundle();
        args.putString(CAT_TITLE, catTitle);
        args.putString(CAT_ID,catId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            catTitle = getArguments().getString(CAT_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);

        prgLoading = (ProgressBar) view.findViewById(R.id.prg_loading);
        categoryList = (ExpandableHeightGridView) view.findViewById(R.id.product_list_category);
        txtAlert = (LilyTextView) view.findViewById(R.id.text_alert);

        // category API url
        ProductsAPI = Config.LILY_SERVER + "/api/product-classes/";

        //Get data from server
        parseJSONData();

        adapterProductList = new GridviewAdapter(getActivity(),gridViewItemList);




        // event listener to handle list when clicked
        categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                // go to menu page
				Intent intent = new Intent(getActivity(), ActivityProductDetail.class);
                intent.putExtra("product_pos",position);
                startActivity(intent);
//                Intent iMenuList = new Intent(ActivityProductCategory.this, ActivityProductList.class);
//                iMenuList.putExtra("category_id", productId.get(position));
//                iMenuList.putExtra("category_name", CategoryName.get(position));
//                startActivity(iMenuList);

                Toast.makeText(getContext(), ""+position, Toast.LENGTH_SHORT).show();
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


        return view;


    }

    void clearData(){
       gridViewItemList.clear();

    }

    public void parseJSONData(){

        clearData();
        try {

            Bundle bundle = getArguments();
            String catId = bundle.getString("catId");
            OkHttpClient client = Config.getOkHttpClient();

            Request okRequest = new Request.Builder()
                    .url(Config.LILY_SERVER +"/api/products/category/" + catId)
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

                            int f = i;
                            if (f==0) f = 1;
                            else f = i%22;

                            JSONObject product = productArray.getJSONObject(i);

                            String id = product.getString("id");
                            String desc = product.getString("description");
                            String title = product.getString("name");
                            boolean isInStock = product.getBoolean("is_in_stock");
                            String image = Config.LILY_SERVER + "/static/images/products/" +f+".jpg";

                            JSONArray priceRange =  product.getJSONArray("get_price_range");
                            JSONArray priceLower = priceRange.getJSONArray(0);
                            JSONArray priceUpper = priceRange.getJSONArray(1);

                            double priceUpperVal = priceUpper.getDouble(0);
                            double priceLowerVal = priceLower.getDouble(0);

                            GridViewItem gridViewItem = new GridViewItem(image,title,priceLowerVal,
                                    priceUpperVal,desc,id,isInStock);

                            gridViewItemList.add(gridViewItem);

//							Log.d(Tag,"uppper price:"+price);

                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                prgLoading.setVisibility(View.GONE);

                                LilyApplication.productList = gridViewItemList;

                                // if data available show data on list
                                // otherwise, show alert text
                                Log.d(Tag,"" + gridViewItemList.size());
                                if(gridViewItemList.size() > 0){
                                    categoryList.setVisibility(View.VISIBLE);
                                    categoryList.setAdapter(adapterProductList);
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public ArrayList<GridViewItem> getGridViewItemList() {
        return gridViewItemList;
    }
}
