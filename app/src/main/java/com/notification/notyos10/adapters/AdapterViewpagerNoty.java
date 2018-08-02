package com.notification.notyos10.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.notification.notyos10.customviews.MyViewPager;
import com.notification.notyos10.objects.NotifyEntity;
import com.notification.notyos10.customviews.slide.NotifyLayout;
import com.notification.notyos10.customviews.slide.ToDayLayout;

import java.util.ArrayList;


public class AdapterViewpagerNoty extends PagerAdapter {
    private Context mContext;
    private ArrayList<NotifyEntity> mNotyModelLeft;
    private ArrayList<NotifyEntity> mNotyModelRight;
    private ToDayLayout layoutToday;
    private NotifyLayout layoutNoty;
    private MyViewPager myViewPager;
    private CommucationViewpagerNoty mCommucationViewpagerNoty;
    public AdapterViewpagerNoty(Context context) {
        mContext = context;
    }

    public AdapterViewpagerNoty(Context context, ArrayList<NotifyEntity> listNotyLeft, ArrayList<NotifyEntity> listNotyRight) {
        mContext = context;
        mNotyModelLeft = listNotyLeft;
        mNotyModelRight = listNotyRight;
    }
    public AdapterViewpagerNoty(Context context, ArrayList<NotifyEntity> listNotyLeft, ArrayList<NotifyEntity> listNotyRight, MyViewPager myViewPager) {
        mContext = context;
        mNotyModelLeft = listNotyLeft;
        mNotyModelRight = listNotyRight;
        this.myViewPager = myViewPager;
        if (myViewPager == null){
            Log.e("test_pager","myViewPager nulll");
        }
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }


    @Override
    public int getCount() {
        return 2;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (position == 0) {
            layoutToday = (ToDayLayout) object;
            layoutToday.closeLayout();
        } else if (position == 1) {
            layoutNoty = (NotifyLayout) object;
            layoutNoty.closeLayout();
        }
    }

    public void updateNotyAndToday(ArrayList<NotifyEntity> listNotyLeft, ArrayList<NotifyEntity> listNotyRight) {
        mNotyModelLeft = listNotyLeft;
        mNotyModelRight = listNotyRight;
        if (layoutNoty != null){
            layoutNoty.updateNoty(mNotyModelRight);
            layoutToday.updateNoty(mNotyModelLeft);
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (position == 0) {
            Log.e("test_layout","dang o todayPartial");
            layoutToday = ToDayLayout.fromXml(mContext, container);
            layoutToday.openLayout(mNotyModelLeft);
            layoutToday.setTag(position);
            return layoutToday;
        } else if (position == 1) {
            layoutNoty = NotifyLayout.fromXml(mContext, container, myViewPager);
            layoutNoty.openLayout(mNotyModelRight);
            layoutNoty.setTag(position);
            return layoutNoty;
        }
        return null;
    }

    public ToDayLayout getLayoutToday(){
        return layoutToday;
    }
    public NotifyLayout getNotifyPartial(){
        return layoutNoty;
    }

    public void listenerAdapterViewpager(CommucationViewpagerNoty mCommucationViewpagerNoty){
        this.mCommucationViewpagerNoty = mCommucationViewpagerNoty;
    }

    public interface CommucationViewpagerNoty{
        void updateVisiblelyWeather();
    }
}