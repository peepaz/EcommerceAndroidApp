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

import com.lilyondroid.lily.Config;
import com.lilyondroid.lily.R;
import com.lilyondroid.lily.activities.ActivityMenuCategory;
import com.squareup.picasso.Picasso;

// adapter class for custom category list
public class AdapterCategoryList_old extends BaseAdapter {

    private Activity activity;
    String Tag = "ptasdevz";


    public AdapterCategoryList_old(Activity act) {
        this.activity = act;
    }

    public int getCount() {
        return ActivityMenuCategory.Category_ID.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.category_list_item, null);
            holder = new ViewHolder();

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.txtText = (TextView) convertView.findViewById(R.id.txtText);
        holder.imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);

        holder.txtText.setText(ActivityMenuCategory.Category_name.get(position));
        Log.d(Tag,Config.ADMIN_PANEL_URL + "/" + ActivityMenuCategory.Category_image.get(position) + " " + position);

        Picasso.with(activity).load(Config.ADMIN_PANEL_URL + "/" + ActivityMenuCategory.Category_image.get(position)).placeholder(R.drawable.loading).into(holder.imgThumb);

        return convertView;
    }

    static class ViewHolder {
        TextView txtText;
        ImageView imgThumb;
    }


}