package com.lilyondroid.lily.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilyondroid.lily.R;
import com.lilyondroid.lily.utilities.GridViewItem;

import java.util.ArrayList;


public class GridviewAdapter extends BaseAdapter {

    Context context;

    String Tag = "ptasdevz";

    ArrayList<GridViewItem> gridViewItemList;

    public GridviewAdapter(Context context, ArrayList<GridViewItem> gridViewItemlist) {
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
            convertView = layoutInflater.inflate(R.layout.gridview, null);

            viewHolder = new ViewHolder();

            viewHolder.image = (ImageView) convertView.findViewById(R.id.grid_view_item_img);
            viewHolder.title = (TextView) convertView.findViewById(R.id.grid_view_item_title);
            viewHolder.price = (TextView) convertView.findViewById(R.id.grid_view_item_price);
            viewHolder.otherText = (TextView) convertView.findViewById(R.id.grid_view_item_other_text);


            convertView.setTag(viewHolder);


        } else {

            viewHolder = (ViewHolder) convertView.getTag();
        }


        GridViewItem gridViewItem = (GridViewItem) getItem(position);


//        Log.d(Tag, "image: "+image);

        viewHolder.image.setImageResource(gridViewItem.getImage());
        viewHolder.title.setText(gridViewItem.getTitle());
        viewHolder.price.setText(gridViewItem.getPrice());
        viewHolder.otherText.setText(gridViewItem.getOtherText());


        return convertView;
    }

    private class ViewHolder {
        ImageView image;
        TextView title;
        TextView price;
        TextView otherText;

    }
}

