package com.lilyondroid.lily.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.lilyondroid.lily.R;
import com.lilyondroid.lily.activities.ActivityAbout;
import com.lilyondroid.lily.activities.ActivityCart;
import com.lilyondroid.lily.activities.ActivityCategory;
import com.lilyondroid.lily.activities.ActivityCheckout;
import com.lilyondroid.lily.activities.ActivityProfile;
import com.lilyondroid.lily.adapters.RecyclerViewAdapter;
import com.lilyondroid.lily.utilities.ChildAnimationExample;
import com.lilyondroid.lily.utilities.ExpandableHeightGridView;
import com.lilyondroid.lily.utilities.FeaturedItem;
import com.lilyondroid.lily.utilities.SliderLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class FragmentHome extends Fragment implements BaseSliderView.OnSliderClickListener {


    private ExpandableHeightGridView gridview;
    private RecyclerView featuredItemsRecyclerView;
    private RecyclerView featuredItemsRecyclerView1;
    private RecyclerView.LayoutManager recyclerViewLayoutMgr;
    private RecyclerView.LayoutManager recyclerViewLayoutMgr1;
    private RecyclerView.Adapter recycleViewrAdapter;

    private ArrayList<FeaturedItem> data;

    SliderLayout mDemoSlider;
    private DrawerLayout mDrawerLayout;

    String TAG = "ptasdevz";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);



        mDemoSlider = (SliderLayout)v.findViewById(R.id.slider);
        data = new ArrayList<FeaturedItem>();

//
//        gridview = (ExpandableHeightGridView) v.findViewById(R.id.homeGridView);
//        gridview.setOnItemClickListener(this);


        data.add(new FeaturedItem(R.drawable.feature1,"Reed Inc","From $100"));
        data.add(new FeaturedItem(R.drawable.feature2,"Gutierrez Group","From $200"));
        data.add(new FeaturedItem(R.drawable.feature3,"Ruiz-Walker","From 300"));
        data.add(new FeaturedItem(R.drawable.feature4,"Adkins and Sons","From 150"));



        featuredItemsRecyclerView = (RecyclerView) v.findViewById(R.id.featured_products_recycler);
        featuredItemsRecyclerView1 = (RecyclerView) v.findViewById(R.id.most_recently_viewed_recycler);

        featuredItemsRecyclerView.setHasFixedSize(true);
        featuredItemsRecyclerView1.setHasFixedSize(true);

        recyclerViewLayoutMgr = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewLayoutMgr1 = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);

        featuredItemsRecyclerView.setLayoutManager(recyclerViewLayoutMgr);
        featuredItemsRecyclerView1.setLayoutManager(recyclerViewLayoutMgr1);

        recycleViewrAdapter = new RecyclerViewAdapter(getActivity(),data);

        featuredItemsRecyclerView.setAdapter(recycleViewrAdapter);
        featuredItemsRecyclerView1.setAdapter(recycleViewrAdapter);

//        data.add(new GridViewItem(R.drawable.ic_shopping_basket_black_24dp,getResources().getString(R.string.menu_product)));
//        data.add(new GridViewItem(R.drawable.ic_shopping_cart_black_24dp,getResources().getString(R.string.menu_cart)));
//        data.add(new GridViewItem(R.drawable.ic_checkout,getResources().getString(R.string.menu_info)));
//        data.add(new GridViewItem(R.drawable.ic_info,getResources().getString(R.string.menu_info)));
//        data.add(new GridViewItem(R.drawable.ic_profile,getResources().getString(R.string.menu_profile)));
//        data.add(new GridViewItem(R.drawable.ic_about,getResources().getString(R.string.menu_about)));


//        gridviewAdapter = new AdapterCategoryGridview(getActivity(), data);
//        gridview.setExpanded(true);
//        gridview.setAdapter(gridviewAdapter);


        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("1", R.drawable.anniversary);
        file_maps.put("2",R.drawable.love_romance);
        file_maps.put("3",R.drawable.sympathy);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(v.getContext());
            // initialize a SliderLayout
            textSliderView
                    //  .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                    .image(file_maps.get(name))
                    .description(name)
                    .setOnSliderClickListener(this);


            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);

            mDemoSlider.addSlider(textSliderView);

        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new ChildAnimationExample());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//        inflater.inflate(R.menu.main, menu);
//        super.onCreateOptionsMenu(menu, inflater);
//
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem menuItem) {
//        switch (menuItem.getItemId()) {
//
//            case android.R.id.home:
//                return true;
//
//            default:
//                return super.onOptionsItemSelected(menuItem);
//        }
//    }
//    @Override
//    public void onItemClick(final AdapterView<?> arg0, final View view, final int position, final long id) {
//        if (position == 0) {
//            startActivity(new Intent(getActivity(), ActivityCategory.class));
////            startActivity(new Intent(getActivity(), ActivityMenuCategory.class));
////            startActivity(new Intent(getActivity(), ActivityProductList.class));
//        } else if (position == 1) {
//            startActivity(new Intent(getActivity(), ActivityCart.class));
//        } else if (position == 2) {
//            startActivity(new Intent(getActivity(), ActivityCheckout.class));
//        } else if (position == 3) {
//            startActivity(new Intent(getActivity(), ActivityInformation.class));
//        } else if (position == 4) {
//            startActivity(new Intent(getActivity(), ActivityProfile.class));
//        } else {
//            startActivity(new Intent(getActivity(), ActivityAbout.class));
//        }
//    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

//      SliderLayout sliderLayout = (SliderLayout) slider.getView();

//        BaseSliderView sliderView = sliderLayout.getCurrentSlider();
//        sliderView.
        String description = slider.getDescription();
        Intent intent = new Intent(getActivity(), ActivityCategory.class);
        intent.putExtra("cat_id",description);
        startActivity(intent);
        Log.d(TAG, "Slider id "+ description);

    }
}


