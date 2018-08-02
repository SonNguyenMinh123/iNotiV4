package com.notification.notyos10.customviews.slide;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.notification.notyos10.R;
import com.notification.notyos10.adapters.AdapterNotyPartialItem;
import com.notification.notyos10.constants.Const;
import com.notification.notyos10.customviews.MyViewPager;
import com.notification.notyos10.customviews.closebutton.CloseButton;
import com.notification.notyos10.objects.NotifyEntity;
import com.notification.notyos10.services.NotificationMonitorService;
import com.notification.notyos10.services.NotifyService;
import com.notification.notyos10.customviews.widgets.TextViewOSNormal;
import com.notification.notyos10.swipemenulistview.SwipeMenu;
import com.notification.notyos10.swipemenulistview.SwipeMenuCreator;
import com.notification.notyos10.swipemenulistview.SwipeMenuItem;
import com.notification.notyos10.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;


public class NotifyLayout extends RelativeLayout {

    public static final int MENU_BASIC = 0;
    public static final int MENU_CALL = 1;
    public static final int MENU_SMS = 2;

    private Context mContext;
    private static ViewGroup mViewGroup;
    private SwipeMenuListView swipeMenuListView;
    private AdapterNotyPartialItem mNotyPartialAdapter;
    private ArrayList<NotifyEntity> mHeaderNotyModelArrayList;
    private TextViewOSNormal txvPartialNotyEmpty;
    private CloseButton mCloseButton;
    private static MyViewPager mMyViewPager;
    public NotifyLayout(Context context) {
        super(context);
        mContext = context;
    }

    public NotifyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public static NotifyLayout fromXml(Context context, ViewGroup viewGroup, MyViewPager myViewPager) {
        NotifyLayout layout = (NotifyLayout) LayoutInflater.from(context)
                .inflate(R.layout.slide_layout_noty, viewGroup, false);
        mViewGroup = viewGroup;
        mMyViewPager = myViewPager;
        return layout;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        swipeMenuListView = (SwipeMenuListView) findViewById(R.id.ltv_partial_noty);
        txvPartialNotyEmpty = (TextViewOSNormal) findViewById(R.id.txv_partial_noty__empty);
        mCloseButton = (CloseButton) findViewById(R.id.close_button_partial_noty);

        swipeMenuListView.setViewpager(mMyViewPager);

        mCloseButton.setOnClickListener(new CloseButton.CloseButtonInterface() {
            @Override
            public void onClickClosed() {
                Log.e("test_close", "close all noti");
                for (int i = 0; i < mHeaderNotyModelArrayList.size(); i++) {
                    Intent intent = new Intent(NotificationMonitorService.ACTION_NLS_CONTROL);
                    intent.putExtra("command", "cancel_position");
                    intent.putExtra("id", mHeaderNotyModelArrayList.get(i).getId());
                    intent.putExtra("tag", mHeaderNotyModelArrayList.get(i).getTag());
                    intent.putExtra("packagename", mHeaderNotyModelArrayList.get(i).getPackageName());
                    intent.putExtra("pos", mHeaderNotyModelArrayList.get(i).getPosition());

                    Log.e("test_close", "closing : " + mHeaderNotyModelArrayList.get(i).getPackageName());

                    if (mHeaderNotyModelArrayList.get(i).getKey() != null) {
                        intent.putExtra("key", mHeaderNotyModelArrayList.get(i).getKey());
                    }
                    mContext.sendBroadcast(intent);
                }
                Const.arrNotyMissedCall.clear();
                Const.arrNotySms.clear();
                mHeaderNotyModelArrayList.clear();


                mNotyPartialAdapter.notifyDataSetChanged();
                mCloseButton.closeAnim();
            }
        });
        swipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NotifyService.getInstance().toolbarPanelLayout.closePanel();

                if (!mHeaderNotyModelArrayList.get(position).isMissCalled()
                        && !mHeaderNotyModelArrayList.get(position).isSms()) {
                    try {
                        if (mHeaderNotyModelArrayList.get(position).getPendingIntent() != null) {
                            mHeaderNotyModelArrayList.get(position).getPendingIntent().send();
                        }
                    } catch (PendingIntent.CanceledException e) {
                        AlarmManager am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), mHeaderNotyModelArrayList.get(position).getPendingIntent());
                    }
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
                } else {
                    if (mHeaderNotyModelArrayList.get(position).isMissCalled()) {

                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + mHeaderNotyModelArrayList.get(position).getNumberPhone()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);

                        mHeaderNotyModelArrayList.remove(position);
                        Const.arrNotyMissedCall.remove(mHeaderNotyModelArrayList.get(position).getPostionMissedCall());
                    } else if (mHeaderNotyModelArrayList.get(position).isSms()) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", mHeaderNotyModelArrayList.get(position).getNumberPhone(), null));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);

                        try {
                            mHeaderNotyModelArrayList.remove(position);
                            Const.arrNotySms.remove(mHeaderNotyModelArrayList.get(position).getPositionSms());
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mNotyPartialAdapter.notifyDataSetChanged();
            }
        });
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
        swipeMenuListView.setAdapter(mNotyPartialAdapter);

        if (mHeaderNotyModelArrayList.size() == 0) {
            mCloseButton.setVisibility(INVISIBLE);
        } else
            mCloseButton.setVisibility(VISIBLE);

    }

    public void closeLayout() {
        mViewGroup.removeView(this);
        clearFocus();
    }

    private void initData() {
        swipeMenuListView.setEmptyView(txvPartialNotyEmpty);
        mNotyPartialAdapter = new AdapterNotyPartialItem(mContext, mHeaderNotyModelArrayList);
        swipeMenuListView.setAdapter(mNotyPartialAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                // Create different menus depending on the view type
                switch (menu.getViewType()) {
                    case MENU_BASIC:
                        createMenuBasic(menu);
                        break;
                    case MENU_CALL:
                        createMenuCall(menu);
                        break;
                    case MENU_SMS:
                        createMenuSms(menu);
                    default:
                        break;
                }
            }
        };
        swipeMenuListView.setMenuCreator(creator);
        swipeMenuListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
            }

            @Override
            public void onSwipeEnd(int position) {
            }
        });
        swipeMenuListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
                Const.isOpenedMenuNoty = true;
            }

            @Override
            public void onMenuClose(int position) {
                Const.isOpenedMenuNoty = false;
            }
        });
        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                if (menu.getViewType() == MENU_BASIC) {
                    if (index == 1) {
                        Log.e("test_click_menu", "click ic_clear noty");
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

                        swipeMenuListView.smoothCloseMenu();

                        return true;
                    }
                } else if (menu.getViewType() == MENU_CALL) {
                    if (index == 1) {
                        NotifyService.getInstance().toolbarPanelLayout.closePanel();
                        String number = getPhoneNumber(mHeaderNotyModelArrayList.get(position).getContent(), mContext);
                        String nameFinding = mHeaderNotyModelArrayList.get(position).getContent();
                        Log.e("test_sms", "number : " + number);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", mHeaderNotyModelArrayList.get(position).getNumberPhone(), null));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);


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
                        if (mHeaderNotyModelArrayList.get(position).isMissCalled()) {
                            for (int p = 0; p < Const.arrNotyMissedCall.size(); p++) {
                                if (mHeaderNotyModelArrayList.get(position).getPostionMissedCall()
                                        == Const.arrNotyMissedCall.get(p).getPostionMissedCall()) {
                                    Const.arrNotyMissedCall.remove(p);
                                }
                            }
                        }
                        mHeaderNotyModelArrayList.remove(position);
                        mNotyPartialAdapter.notifyDataSetChanged();

                    } else if (index == 2) {
                        NotifyService.getInstance().toolbarPanelLayout.closePanel();

                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + mHeaderNotyModelArrayList.get(position).getNumberPhone()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);

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
                        if (mHeaderNotyModelArrayList.get(position).isMissCalled()) {
                            for (int p = 0; p < Const.arrNotyMissedCall.size(); p++) {
                                if (mHeaderNotyModelArrayList.get(position).getPostionMissedCall()
                                        == Const.arrNotyMissedCall.get(p).getPostionMissedCall()) {
                                    Const.arrNotyMissedCall.remove(p);
                                }
                            }
                        }
                        mHeaderNotyModelArrayList.remove(position);
                        mNotyPartialAdapter.notifyDataSetChanged();
                    } else if (index == 3) {
                        if (mHeaderNotyModelArrayList.get(position).isMissCalled()) {
                            for (int p = 0; p < Const.arrNotyMissedCall.size(); p++) {
                                if (mHeaderNotyModelArrayList.get(position).getPostionMissedCall()
                                        == Const.arrNotyMissedCall.get(p).getPostionMissedCall()) {
                                    Const.arrNotyMissedCall.remove(p);
                                }
                            }
                        }
                        mHeaderNotyModelArrayList.remove(position);
                        mNotyPartialAdapter.notifyDataSetChanged();
                        return true;
                    }
                } else if (menu.getViewType() == MENU_SMS) {
                    if (index == 1) {
                        NotifyService.getInstance().toolbarPanelLayout.closePanel();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", mHeaderNotyModelArrayList.get(position).getNumberPhone(), null));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
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

                        for (int p = 0; p < Const.arrNotySms.size(); p++) {
                            if (mHeaderNotyModelArrayList.get(position).getPositionSms()
                                    == Const.arrNotySms.get(p).getPositionSms()) {
                                Const.arrNotySms.remove(p);
                                mHeaderNotyModelArrayList.remove(position);
                                break;
                            }
                        }
                        mNotyPartialAdapter.notifyDataSetChanged();

                    } else if (index == 2) {
                        for (int p = 0; p < Const.arrNotySms.size(); p++) {
                            if (mHeaderNotyModelArrayList.get(position).getPositionSms()
                                    == Const.arrNotySms.get(p).getPositionSms()) {
                                Const.arrNotySms.remove(p);
                                mHeaderNotyModelArrayList.remove(position);
                                break;
                            }
                        }
                        mNotyPartialAdapter.notifyDataSetChanged();
                        return true;
                    }
                }

                Const.isOpenedMenuNoty = false;
                return false;
            }
        });
    }

    public String getPhoneNumber(String name, Context context) {
        Log.e("test_sms", " : " + name);
        String ret = null;
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like'%" + name + "%'";
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null);
        if (c.moveToFirst()) {
            ret = c.getString(0);
        }
        c.close();
        if (ret == null)
            ret = "Unsaved";
        return ret;
    }

    private void createMenuBasic(SwipeMenu menu) {
        SwipeMenuItem noneItem = new SwipeMenuItem(mContext);
        noneItem.setBackground(mContext.getResources().getDrawable(android.R.color.transparent));
        noneItem.setWidth(dp2px(10));


        SwipeMenuItem openItem = new SwipeMenuItem(mContext);
        openItem.setBackground(mContext.getResources().getDrawable(R.drawable.background_round));
        openItem.setWidth(dp2px(100));
        openItem.setTitle(R.string.menu_noty_clear);
        openItem.setTitleSize((int) mContext.getResources().getDimension(R.dimen._8sdp));
        openItem.setTitleColor(Color.BLACK);

        menu.addMenuItem(noneItem);
        menu.addMenuItem(openItem);
    }

    private void createMenuCall(SwipeMenu menu) {
        SwipeMenuItem noneItem = new SwipeMenuItem(mContext);
        noneItem.setBackground(mContext.getResources().getDrawable(android.R.color.transparent));
        noneItem.setWidth(dp2px(10));

        SwipeMenuItem sendSms = new SwipeMenuItem(mContext);
        SwipeMenuItem callBack = new SwipeMenuItem(mContext);
        SwipeMenuItem clear = new SwipeMenuItem(mContext);
        sendSms.setBackground(mContext.getResources().getDrawable(R.drawable.background_round_left));
        sendSms.setWidth(dp2px(100));
        sendSms.setTitle(R.string.menu_noty_send_sms);
        sendSms.setTitleSize((int) mContext.getResources().getDimension(R.dimen._8sdp));
        sendSms.setTitleColor(Color.BLACK);

        callBack.setBackground(mContext.getResources().getDrawable(R.drawable.list_divider));
        callBack.setWidth(dp2px(100));
        callBack.setTitle(R.string.menu_noty_call_back);
        callBack.setTitleSize((int) mContext.getResources().getDimension(R.dimen._8sdp));
        callBack.setTitleColor(Color.BLACK);

        clear.setBackground(mContext.getResources().getDrawable(R.drawable.background_round_right));
        clear.setWidth(dp2px(100));
        clear.setTitle(R.string.menu_noty_clear);
        clear.setTitleSize((int) mContext.getResources().getDimension(R.dimen._8sdp));
        clear.setTitleColor(Color.BLACK);

        menu.addMenuItem(noneItem);
        menu.addMenuItem(sendSms);
        menu.addMenuItem(callBack);
        menu.addMenuItem(clear);
    }

    private void createMenuSms(SwipeMenu menu) {
        SwipeMenuItem noneItem = new SwipeMenuItem(mContext);
        noneItem.setBackground(mContext.getResources().getDrawable(android.R.color.transparent));
        noneItem.setWidth(dp2px(10));

        SwipeMenuItem sendSms = new SwipeMenuItem(mContext);
        SwipeMenuItem clear = new SwipeMenuItem(mContext);
        sendSms.setBackground(mContext.getResources().getDrawable(R.drawable.background_round_left_padding));
        sendSms.setWidth(dp2px(100));
        sendSms.setTitle(R.string.menu_noty_reply);
        sendSms.setTitleSize((int) mContext.getResources().getDimension(R.dimen._8sdp));
        sendSms.setTitleColor(Color.BLACK);

        clear.setBackground(mContext.getResources().getDrawable(R.drawable.background_round_right_padding));
        clear.setWidth(dp2px(100));
        clear.setTitle(R.string.menu_noty_clear);
        clear.setTitleSize((int) mContext.getResources().getDimension(R.dimen._8sdp));
        clear.setTitleColor(Color.BLACK);

        menu.addMenuItem(noneItem);
        menu.addMenuItem(sendSms);
        menu.addMenuItem(clear);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}