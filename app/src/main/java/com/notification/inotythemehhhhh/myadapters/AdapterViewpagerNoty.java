package com.notification.inotythemehhhhh.myadapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.notification.inotythemehhhhh.objects.NotifyEntity;
import com.notification.inotythemehhhhh.customviews.partial.NotifyPartial;
import com.notification.inotythemehhhhh.customviews.partial.ToDayPartial;

import java.util.ArrayList;


public class AdapterViewpagerNoty extends PagerAdapter {
    private Context mContext;
    private ArrayList<NotifyEntity> mNotyModelLeft;
    private ArrayList<NotifyEntity> mNotyModelRight;
    private ToDayPartial layoutToday;
    private NotifyPartial layoutNoty;

    public AdapterViewpagerNoty(Context context) {
        mContext = context;
    }

    public AdapterViewpagerNoty(Context context, ArrayList<NotifyEntity> listNotyLeft, ArrayList<NotifyEntity> listNotyRight) {
        mContext = context;
        mNotyModelLeft = listNotyLeft;
        mNotyModelRight = listNotyRight;
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
            layoutToday = (ToDayPartial) object;
            layoutToday.closeLayout();
        } else if (position == 1) {
            layoutNoty = (NotifyPartial) object;
            layoutNoty.closeLayout();
        }
    }

    public void updateNotyAndToday(ArrayList<NotifyEntity> listNotyLeft, ArrayList<NotifyEntity> listNotyRight) {
        mNotyModelLeft = listNotyLeft;
        mNotyModelRight = listNotyRight;
        if (layoutNoty != null)
            layoutNoty.updateNoty(mNotyModelRight);
        if (layoutToday != null)
            layoutToday.updateNoty(mNotyModelLeft);

    }

    public void updateCalendar() {
        layoutToday.updateCalendar();
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
            layoutToday = ToDayPartial.fromXml(mContext, container);
            layoutToday.openLayout(mNotyModelLeft);
            layoutToday.setTag(position);
            return layoutToday;
        } else if (position == 1) {
            layoutNoty = NotifyPartial.fromXml(mContext, container);
            layoutNoty.openLayout(mNotyModelRight);
            layoutNoty.setTag(position);
            return layoutNoty;
        }
        return null;
    }
}