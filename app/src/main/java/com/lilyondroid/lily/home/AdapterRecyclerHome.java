package com.lilyondroid.lily.home;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lilyondroid.lily.R;
import com.lilyondroid.lily.customviews.LilyTextView;
import com.lilyondroid.lily.utilities.FeaturedItem;

import java.util.ArrayList;


public class AdapterRecyclerHome extends RecyclerView.Adapter<AdapterRecyclerHome.ViewHolder>{

    Context context;

    ArrayList<FeaturedItem> featuredItems;

    public AdapterRecyclerHome(Context context, ArrayList<FeaturedItem> featuredItems) {
        this.context = context;
        this.featuredItems = featuredItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.featured_item_thumbnail,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.image.setImageResource(featuredItems.get(position).getImage());
        holder.title.setText(featuredItems.get(position).getTitle());
        holder.price.setText(featuredItems.get(position).getPrice());
        String sale = featuredItems.get(position).getSale();
        if (sale == null) holder.sale.setVisibility(View.GONE);
        else holder.sale.setText(sale);

    }

    @Override
    public int getItemCount() {
        return featuredItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image;
        public LilyTextView title;
        public LilyTextView price;
        public LilyTextView sale;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.rc_view_item_img);
            title = (LilyTextView) itemView.findViewById(R.id.rc_view_item_title);
            price = (LilyTextView) itemView.findViewById(R.id.rc_view_item_price);
            sale = (LilyTextView) itemView.findViewById(R.id.rc_view_item_other_text);
        }
    }
}


