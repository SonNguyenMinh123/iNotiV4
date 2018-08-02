package com.notification.inotythemehhhhh.customviews.partial;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.notification.inotythemehhhhh.R;
import com.notification.inotythemehhhhh.myadapters.AdapterNotyPartialItem;
import com.notification.inotythemehhhhh.objects.NotifyEntity;
import com.notification.inotythemehhhhh.myservices.NotificationMonitorService;
import com.notification.inotythemehhhhh.myservices.NotifyService;
import com.notification.inotythemehhhhh.customviews.widgets.TextViewOSNormal;

import java.util.ArrayList;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class NotifyPartial extends RelativeLayout {
    private Context mContext;
    private static ViewGroup mViewGroup;
    private StickyListHeadersListView ltvPartialNoty;
    private AdapterNotyPartialItem mNotyPartialAdapter;
    private ArrayList<NotifyEntity> mHeaderNotyModelArrayList;
    private TextViewOSNormal txvPartialNotyEmpty;

    public NotifyPartial(Context context) {
        super(context);
        mContext = context;
    }

    public NotifyPartial(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public static NotifyPartial fromXml(Context context, ViewGroup viewGroup) {
        NotifyPartial layout = (NotifyPartial) LayoutInflater.from(context)
                .inflate(R.layout.partial_noty, viewGroup, false);
        mViewGroup = viewGroup;
        return layout;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ltvPartialNoty = (StickyListHeadersListView) findViewById(R.id.ltv_partial_noty);
        txvPartialNotyEmpty = (TextViewOSNormal) findViewById(R.id.txv_partial_noty__empty);
        ltvPartialNoty.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotifyService.getInstance().toolbarPanelLayout.closePanel();
                try {
                    if (mHeaderNotyModelArrayList.get(position).getPendingIntent() != null) {
                        mHeaderNotyModelArrayList.get(position).getPendingIntent().send();
                    }
                } catch (PendingIntent.CanceledException e) {
                    AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), mHeaderNotyModelArrayList.get(position).getPendingIntent());
                }


//                if (mHeaderNotyModelArrayList.get(position).isAction()) {
                Intent i = new Intent(NotificationMonitorService.ACTION_NLS_CONTROL);
                i.putExtra("command", "cancel_position");
                i.putExtra("id", mHeaderNotyModelArrayList.get(position).getId());
                i.putExtra("tag", mHeaderNotyModelArrayList.get(position).getTag());
                i.putExtra("packagename", mHeaderNotyModelArrayList.get(position).getPackageName());
                i.putExtra("pos", mHeaderNotyModelArrayList.get(position).getPosition());
                if (mHeaderNotyModelArrayList.get(position).getKey() != null) {
                    i.putExtra("key", mHeaderNotyModelArrayList.get(position).getKey());
                }
                mContext.sendBroadcast(i);
                mHeaderNotyModelArrayList.remove(position);
                mNotyPartialAdapter.notifyDataSetChanged();
//                }
            }
        });
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View header = inflater.inflate(R.layout.partial_today_footer, ltvPartialNoty.getWrappedList(), false);
        Button btnPartialTodayFotterClearAll = (Button) header.findViewById(R.id.btn_partial_today_fotter__clear_all);
        btnPartialTodayFotterClearAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mHeaderNotyModelArrayList.size(); i++) {
                    Intent intent = new Intent(NotificationMonitorService.ACTION_NLS_CONTROL);
                    intent.putExtra("command", "cancel_position");
                    intent.putExtra("id", mHeaderNotyModelArrayList.get(i).getId());
                    intent.putExtra("tag", mHeaderNotyModelArrayList.get(i).getTag());
                    intent.putExtra("packagename", mHeaderNotyModelArrayList.get(i).getPackageName());
                    intent.putExtra("pos", mHeaderNotyModelArrayList.get(i).getPosition());
                    if (mHeaderNotyModelArrayList.get(i).getKey() != null) {
                        intent.putExtra("key", mHeaderNotyModelArrayList.get(i).getKey());
                    }
                    mContext.sendBroadcast(intent);
                }
                mHeaderNotyModelArrayList.clear();
                mNotyPartialAdapter.notifyDataSetChanged();
            }
        });
        ltvPartialNoty.addFooterView(header);
    }

    public void openLayout(ArrayList<NotifyEntity> listNotyRight) {
        mHeaderNotyModelArrayList = listNotyRight;
        mViewGroup.addView(this);
        initData();
        requestFocus();
        requestLayout();
    }

    public void updateNoty(ArrayList<NotifyEntity> listNotyRight) {
        mHeaderNotyModelArrayList = listNotyRight;
        mNotyPartialAdapter = new AdapterNotyPartialItem(mContext, mHeaderNotyModelArrayList);
        ltvPartialNoty.setAdapter(mNotyPartialAdapter);
    }


    public void closeLayout() {
        mViewGroup.removeView(this);
        clearFocus();
    }

    private void initData() {
        ltvPartialNoty.setEmptyView(txvPartialNotyEmpty);
        mNotyPartialAdapter = new AdapterNotyPartialItem(mContext, mHeaderNotyModelArrayList);
        ltvPartialNoty.setAdapter(mNotyPartialAdapter);
    }
}