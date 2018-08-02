package com.notification.inotythemehhhhh.myadapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.notification.inotythemehhhhh.R;
import com.notification.inotythemehhhhh.objects.AppInfo;
import com.notification.inotythemehhhhh.customviews.swipedown.ToolbarPanelLayout;

import java.util.List;

/**
 * Created by MyPC on 12/07/2016.
 */
public class AdapterAppInfo extends RecyclerView.Adapter<AdapterAppInfo.ViewHolder> {

    private Context mContext;
    private List<AppInfo> mList;
    private ToolbarPanelLayout toolbarPanelLayout;

    public AdapterAppInfo(Context mContext, List<AppInfo> mList, ToolbarPanelLayout toolbarPanelLayout) {
        this.mContext = mContext;
        this.mList = mList;
        this.toolbarPanelLayout = toolbarPanelLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        Glide.with(mContext)
//                .load(mList.get(position).getIcon())
//                .centerCrop()
//                .into(holder.img);

        holder.img.setBackground(mList.get(position).getIcon());

        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewCompat.animate(view)
                        .setDuration(100)
                        .scaleX(0.9f)
                        .scaleY(0.9f)
                        .setInterpolator(new CycleInterpolator())
                        .setListener(new ViewPropertyAnimatorListener() {
                            @Override
                            public void onAnimationStart(final View view) {
                                holder.relative.setClickable(false);
                            }

                            @Override
                            public void onAnimationEnd(final View view) {

                                Intent LaunchApp = mContext.getPackageManager().getLaunchIntentForPackage(mList.get(position).getPname());
                                LaunchApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(LaunchApp);

                                ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);

                                holder.relative.setClickable(true);
                                toolbarPanelLayout.closePanel();
                            }

                            @Override
                            public void onAnimationCancel(final View view) {
                                holder.relative.setClickable(true);
                            }
                        })
                        .withLayer()
                        .start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(AppInfo item) {
        insert(item, mList.size());
    }

    public void insert(AppInfo item, int position) {
        mList.add(position, item);
        notifyItemInserted(position);
    }

    public void clear() {
        int size = mList.size();
        mList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView img;
        private RelativeLayout relative;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            relative = (RelativeLayout) itemView.findViewById(R.id.relative);

        }
    }

    private class CycleInterpolator implements android.view.animation.Interpolator {

        private final float mCycles = 0.5f;

        @Override
        public float getInterpolation(final float input) {
            return (float) Math.sin(2.0f * mCycles * Math.PI * input);
        }
    }


}
