package com.solodroid.ecommerce.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.solodroid.ecommerce.R;
import com.solodroid.ecommerce.activities.ActivityAbout;
import com.solodroid.ecommerce.activities.ActivityCart;
import com.solodroid.ecommerce.activities.ActivityCheckout;
import com.solodroid.ecommerce.activities.ActivityInformation;
import com.solodroid.ecommerce.activities.ActivityMenuCategory;
import com.solodroid.ecommerce.activities.ActivityProfile;
import com.solodroid.ecommerce.adapters.AdapaterGridView;
import com.solodroid.ecommerce.adapters.AdapterGridViewItem;

import java.util.ArrayList;

public class FragmentHome extends Fragment implements OnItemClickListener {

    GridView gridview;
    AdapaterGridView gridviewAdapter;
    ArrayList<AdapterGridViewItem> data = new ArrayList<AdapterGridViewItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        gridview = (GridView) v.findViewById(R.id.gridView1);
        gridview.setOnItemClickListener(this);

        data.add(new AdapterGridViewItem(getResources().getString(R.string.menu_product), ContextCompat.getDrawable(getActivity(), R.drawable.ic_product)));
        data.add(new AdapterGridViewItem(getResources().getString(R.string.menu_cart), ContextCompat.getDrawable(getActivity(), R.drawable.ic_cart)));
        data.add(new AdapterGridViewItem(getResources().getString(R.string.menu_checkout), ContextCompat.getDrawable(getActivity(), R.drawable.ic_checkout)));
        data.add(new AdapterGridViewItem(getResources().getString(R.string.menu_info), ContextCompat.getDrawable(getActivity(), R.drawable.ic_info)));
        data.add(new AdapterGridViewItem(getResources().getString(R.string.menu_profile), ContextCompat.getDrawable(getActivity(), R.drawable.ic_profile)));
        data.add(new AdapterGridViewItem(getResources().getString(R.string.menu_about), ContextCompat.getDrawable(getActivity(), R.drawable.ic_about)));

        setDataAdapter();

        return v;
    }

    // Set the Data Adapter
    private void setDataAdapter() {
        gridviewAdapter = new AdapaterGridView(getActivity(), R.layout.lsv_item_category, data);
        gridview.setAdapter(gridviewAdapter);
    }

    @Override
    public void onItemClick(final AdapterView<?> arg0, final View view, final int position, final long id) {
        if (position == 0) {
            startActivity(new Intent(getActivity(), ActivityMenuCategory.class));
        } else if (position == 1) {
            startActivity(new Intent(getActivity(), ActivityCart.class));
        } else if (position == 2) {
            startActivity(new Intent(getActivity(), ActivityCheckout.class));
        } else if (position == 3) {
            startActivity(new Intent(getActivity(), ActivityInformation.class));
        } else if (position == 4) {
            startActivity(new Intent(getActivity(), ActivityProfile.class));
        } else {
            startActivity(new Intent(getActivity(), ActivityAbout.class));
        }
    }

}