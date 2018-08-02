package com.notification.notyos10.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.notification.notyos10.R;
import com.notification.notyos10.objects.AppInfo;
import com.notification.notyos10.customviews.swipedown.ToolbarPanelLayout;

import java.util.List;


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
        if(mList.get(position).getIcon()!=null)
        holder.img.setImageDrawable(mList.get(position).getIcon());
        holder.txtNameApp.setText(mList.get(position).getAppname());
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
                                ((InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
//                                holder.relative.setClickable(true);
                                toolbarPanelLayout.closePanel();
                                Log.e("test3"," : " + mList.get(position).getPname());
                                try {
//                                    Intent launchApp = mContext.getPackageManager().getLaunchIntentForPackage(mList.get(position).getPname());
                                    PackageManager packageManager = mContext.getPackageManager();
                                    Intent launchApp = packageManager.getLaunchIntentForPackage(mList.get(position).getPname());
                                    launchApp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mContext.startActivity(launchApp);
                                }catch (Exception e){
                                   Log.e("test4",":" + e);
                                }

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
        private TextView txtNameApp;
        private ImageView img;
        private RelativeLayout relative;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            txtNameApp = (TextView) itemView.findViewById(R.id.txt_name_app);
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
