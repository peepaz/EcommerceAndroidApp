package com.lilyondroid.lily.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lilyondroid.lily.Config;
import com.lilyondroid.lily.R;
import com.lilyondroid.lily.activities.ActivityProductCategory;
import com.lilyondroid.lily.utilities.PicassoTrustAll;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

// adapter class for custom category list
public class AdapterCategoryList extends BaseAdapter {

    private Activity activity;
    String Tag = "ptasdevz";


    public AdapterCategoryList(Activity act) {
        this.activity = act;
    }

    public int getCount() {
        return ActivityProductCategory.CategoryId.size();
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

        holder.txtText.setText(ActivityProductCategory.CategoryName.get(position));

        String [] catPlaceHolderImg = convertView.getResources().getStringArray(R.array.cat_placeholder_img);


        if (position < catPlaceHolderImg.length) {
            Log.d(Tag,Config.LILY_SERVER + "/" +catPlaceHolderImg[position].toString() + " " + position);

//            Picasso.with(activity).load(Config.LILY_SERVER + "/" + catPlaceHolderImg[position])
//                    .placeholder(R.drawable.loading).into(holder.imgThumb);

            PicassoTrustAll.getInstance(convertView.getContext())
                    .load("http://www.dimasword.com/demo/ecommerce/upload//images//5354-2015-07-09.png")
//                    .load(Config.LILY_SERVER + "/" + catPlaceHolderImg[position])
                    .into(holder.imgThumb);
        }

        return convertView;
    }

    static class ViewHolder {
        TextView txtText;
        ImageView imgThumb;
    }


}