package com.lilyondroid.lily.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilyondroid.lily.R;
import com.lilyondroid.lily.utilities.GridViewItem;
import com.lilyondroid.lily.utilities.PicassoTrustAll;

import java.text.DecimalFormat;
import java.util.ArrayList;


public class AdapterCategoryGridview extends BaseAdapter {

    Context context;

    String Tag = "ptasdevz";

    ArrayList<GridViewItem> gridViewItemList;

    public AdapterCategoryGridview(Context context, ArrayList<GridViewItem> gridViewItemlist) {
        this.gridViewItemList = gridViewItemlist;
        this.context = context;
    }

    @Override
    public int getCount() {
        return gridViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return gridViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.product_item_thumbnail, null);

            viewHolder = new ViewHolder();

            viewHolder.image = (ImageView) convertView.findViewById(R.id.product_thumb_nail_img);
            viewHolder.title = (TextView) convertView.findViewById(R.id.product_title);
            viewHolder.price = (TextView) convertView.findViewById(R.id.product_price);
            viewHolder.otherText = (TextView) convertView.findViewById(R.id.product_other_text);


            convertView.setTag(viewHolder);


        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }


        GridViewItem gridViewItem = (GridViewItem) getItem(position);


//        Log.d(Tag, "image: "+image);

        PicassoTrustAll.getInstance(convertView.getContext())
                .load(gridViewItem.getImageUrl()).into(viewHolder.image);

//        viewHolder.image.setImageResource(gridViewItem.getImageUrl());
        viewHolder.title.setText(gridViewItem.getTitle());
        DecimalFormat decimalFormat = new DecimalFormat("From: $###.##");

        viewHolder.price.setText(decimalFormat.format(gridViewItem.getPriceLower()));
        viewHolder.otherText.setText("");


        return convertView;
    }

    private class ViewHolder {
        ImageView image;
        TextView title;
        TextView price;
        TextView otherText;

    }
}

