package com.notification.notyos10.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.notification.notyos10.R;
import com.notification.notyos10.objects.WallpaperOnline;

import java.util.List;

public class ListBackgroundAdapter extends RecyclerView.Adapter<ListBackgroundAdapter.BackgroundItemViewHolder>{
    private Context mContext;
    private List<WallpaperOnline> arrBackground;
    private CommucationListBackgroundAdapter mCommucationListBackgroundAdapter;
    public ListBackgroundAdapter(Context mContext, List<WallpaperOnline> arrBackground) {
        this.mContext = mContext;
        this.arrBackground = arrBackground;
    }

    @Override
    public BackgroundItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,parent,false);
        return new BackgroundItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BackgroundItemViewHolder holder, final int position) {
        Log.e("test10","url : " + arrBackground.get(position).getUrl());
        Glide.with(mContext).load(arrBackground.get(position).getUrl())
                .centerCrop()
                .crossFade()
                .into(holder.imgItemBackground);
        holder.imgItemBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCommucationListBackgroundAdapter.clickItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrBackground.size();
    }

    public class BackgroundItemViewHolder extends RecyclerView.ViewHolder{
        ImageView imgItemBackground;
        public BackgroundItemViewHolder(View itemView) {
            super(itemView);
            imgItemBackground = (ImageView) itemView.findViewById(R.id.item_img);
        }
    }

    public void listener(CommucationListBackgroundAdapter mCommucationListBackgroundAdapter){
        this.mCommucationListBackgroundAdapter = mCommucationListBackgroundAdapter;
    }

    public interface CommucationListBackgroundAdapter{
        void clickItem(int positition);
    }
}
