package com.notification.notyos10.swipemenulistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.notification.notyos10.adapters.SwipeMenuAdapter;
import com.notification.notyos10.customviews.MyViewPager;
import com.notification.notyos10.services.NotifyService;

public class SwipeMenuListView extends ListView {
    private static final int TOUCH_STATE_NONE = 0;
    private static final int TOUCH_STATE_X = 1;
    private static final int TOUCH_STATE_Y = 2;
    public static final int DIRECTION_LEFT = 1;
    public static final int DIRECTION_RIGHT = -1;
    private int mDirection = 1;//swipe from right to left by default
    private int MAX_Y = 5;
    private int MAX_X = 3;
    private float mDownX;
    private float mDownY;
    private int mTouchState;
    private int mTouchPosition;
    private SwipeMenuLayout mTouchView;
    private OnSwipeListener mOnSwipeListener;
    private SwipeMenuCreator mMenuCreator;
    private OnMenuItemClickListener mOnMenuItemClickListener;
    private OnMenuStateChangeListener mOnMenuStateChangeListener;
    private Interpolator mCloseInterpolator;
    private Interpolator mOpenInterpolator;

    public SwipeMenuListView(Context context) {
        super(context);
        init();
    }

    public SwipeMenuListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SwipeMenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        MAX_X = dp2px(MAX_X);
        MAX_Y = dp2px(MAX_Y);
        mTouchState = TOUCH_STATE_NONE;
    }

    private MyViewPager mMyViewPager;
    public void setViewpager(MyViewPager myViewPager){
        mMyViewPager = myViewPager;
//        mMyViewPager.setPagingEnabled(false)
        if (myViewPager == null){
            Log.e("test_new","mMyViewpager bị null");
        }else{
            Log.e("test_new","mMyViewpager ngon");
            myViewPager.setPagingEnabled(false);
        }

    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(new SwipeMenuAdapter(getContext(), adapter) {
            @Override
            public void createMenu(SwipeMenu menu) {
                if (mMenuCreator != null) {
                    mMenuCreator.create(menu);
                }
            }

            @Override
            public void onItemClick(SwipeMenuView view, SwipeMenu menu,
                                    int index) {
                boolean flag = false;
                if (mOnMenuItemClickListener != null) {
                    flag = mOnMenuItemClickListener.onMenuItemClick(
                            view.getPosition(), menu, index);
                    /*my custom*/
                    mTouchView.smoothCloseMenu();
                    NotifyService.getInstance().setPaddingEnable(true);
                    activeSwipe = false;
                    /*my custom*/
                }
                if (mTouchView != null && !flag) {
                    mTouchView.smoothCloseMenu();
                    NotifyService.getInstance().setPaddingEnable(true);
                    activeSwipe = false;
                }

            }
        });
    }

    public void setCloseInterpolator(Interpolator interpolator) {
        mCloseInterpolator = interpolator;
    }

    public void setOpenInterpolator(Interpolator interpolator) {
        mOpenInterpolator = interpolator;
    }

    public Interpolator getOpenInterpolator() {
        return mOpenInterpolator;
    }

    public Interpolator getCloseInterpolator() {
        return mCloseInterpolator;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //在拦截处处理，在滑动设置了点击事件的地方也能swip，点击时又不能影响原来的点击事件
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownX = ev.getX();
                mDownY = ev.getY();
                boolean handled = super.onInterceptTouchEvent(ev);
                mTouchState = TOUCH_STATE_NONE;
                mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
                View view = getChildAt(mTouchPosition - getFirstVisiblePosition());
                if (view instanceof SwipeMenuLayout) {
                    if (mTouchView != null && mTouchView.isOpen() && !inRangeOfView(mTouchView.getMenuView(), ev)) {
                        return true;
                    }
                    mTouchView = (SwipeMenuLayout) view;
                    mTouchView.setSwipeDirection(mDirection);
                }

                if (mTouchView != null && mTouchView.isOpen() && view != mTouchView) {
                    handled = true;
                }

                if (mTouchView != null) {
                    mTouchView.onSwipe(ev);
                }
                return handled;
            case MotionEvent.ACTION_MOVE:
                float dy = Math.abs((ev.getY() - mDownY));
                float dx = Math.abs((ev.getX() - mDownX));
                if (Math.abs(dy) > MAX_Y || Math.abs(dx) > MAX_X) {
                    if (mTouchState == TOUCH_STATE_NONE) {
                        if (Math.abs(dy) > MAX_Y) {
                            mTouchState = TOUCH_STATE_Y;
                        } else if (dx > MAX_X) {
                            mTouchState = TOUCH_STATE_X;
                            if (mOnSwipeListener != null) {
                                mOnSwipeListener.onSwipeStart(mTouchPosition);
                            }
                        }
                    }
                    return true;
                }
        }
        return super.onInterceptTouchEvent(ev);
    }
    private float mDx;
    private boolean activeSwipe;
    private boolean isOpened;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.e("test_swipe","on touch");
        if (ev.getAction() != MotionEvent.ACTION_DOWN && mTouchView == null){
            Log.e("test_touch"," chinh la may!!!!!!");
            return super.onTouchEvent(ev);
        }
        if (mTouchView == null){
            NotifyService.getInstance().setPaddingEnable(true);
            return false;
        }
        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!isOpened){
                    activeSwipe = false;
                }
                else{
                    activeSwipe = true;
                }
//                NotifyService.getInstance().setPaddingEnable(false);
                int oldPos = mTouchPosition;
                mDownX = ev.getX();
                mDownY = ev.getY();
                mTouchState = TOUCH_STATE_NONE;
                mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
                if (mTouchPosition == oldPos && mTouchView != null
                        && mTouchView.isOpen()) {
                    mTouchState = TOUCH_STATE_X;
                    mTouchView.onSwipe(ev);

                    Log.e("test_padding_enable"," dang mở, khác null, chạm item cũ");

                    activeSwipe = true;

                    NotifyService.getInstance().setPaddingEnable(false);

                    return true;
                }
                View view = getChildAt(mTouchPosition - getFirstVisiblePosition());
                if (mTouchView != null && mTouchView.isOpen()) {
                    Log.e("test_padding_enable"," dang mở, khác null, chạm item khác");

                    activeSwipe = true;

                    mTouchView.smoothCloseMenu();
                    mTouchView = null;
                    MotionEvent cancelEvent = MotionEvent.obtain(ev);
                    cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
                    onTouchEvent(cancelEvent);
                    if (mOnMenuStateChangeListener != null) {
                        mOnMenuStateChangeListener.onMenuClose(oldPos);
                    }
                    NotifyService.getInstance().setPaddingEnable(false);
                    return true;
                }

                if (view instanceof SwipeMenuLayout) {
                    mTouchView = (SwipeMenuLayout) view;
                    mTouchView.setSwipeDirection(mDirection);

                }
                if (mTouchView != null) {
                    Log.e("test_touch"," dang đóng, khác null");
                    mTouchView.onSwipe(ev);
                    NotifyService.getInstance().setPaddingEnable(false);
                }


                break;
            case MotionEvent.ACTION_MOVE:
                View view2 = getChildAt(mTouchPosition - getFirstVisiblePosition());
                if (mTouchView == null){
                    Log.e("test_padding_enable","click vao null view");
                }
                if (mTouchView!= null){
                    if (!mTouchView.isOpen()){
//                        NotifyService.getInstance().setPaddingEnable(true);
//                        Log.e("test_padding_enable","enable khi dang dong menu");
                    }
                }
                float dy = Math.abs((ev.getY() - mDownY));
                float dx = Math.abs((ev.getX() - mDownX));
                float mDx = ev.getX() - mDownX;
                Log.e("test_xy","dx = " + mDx);
                if (mDx > 10 && !mTouchView.isOpen()){
                    NotifyService.getInstance().setPaddingEnable(true);
                    Log.e("test_touch","viewpager enable");
                }else
//                if (mDx < 0){
//                    activeSwipe = true;
//                }else
//                if (activeSwipe && mDx > 0){
//                    Log.e("test_padding_enable","tai move...........");
//                    NotifyService.getInstance().setPaddingEnable(false);
//                }
                mDx = dx;
                mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY()) - getHeaderViewsCount();
                if (!mTouchView.getSwipEnable() || mTouchPosition != mTouchView.getPosition()) {
//                    NotifyService.getInstance().setPaddingEnable(true);
                    break;
                }
                if (mTouchState == TOUCH_STATE_X) {
                    if (mTouchView != null) {
                        mTouchView.onSwipe(ev);
                    }
                    getSelector().setState(new int[]{0});
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                } else if (mTouchState == TOUCH_STATE_NONE) {
                    if (Math.abs(dy) > MAX_Y) {
                        mTouchState = TOUCH_STATE_Y;
                    } else if (dx > MAX_X) {
                        mTouchState = TOUCH_STATE_X;
                        if (mOnSwipeListener != null) {
                            mOnSwipeListener.onSwipeStart(mTouchPosition);
                        }
                    }
                }
                break;

            case MotionEvent.ACTION_UP:

                NotifyService.getInstance().setPaddingEnable(true);

                if (mTouchState == TOUCH_STATE_X) {
                    if (mTouchView != null) {
                        boolean isBeforeOpen = mTouchView.isOpen();
                        mTouchView.onSwipe(ev);
                        boolean isAfterOpen = mTouchView.isOpen();

                        Log.e("test_touch","ifAfterOpen : " + isAfterOpen);



                        if (isBeforeOpen != isAfterOpen && mOnMenuStateChangeListener != null) {
                            if (isAfterOpen) {
                                mOnMenuStateChangeListener.onMenuOpen(mTouchPosition);
                                activeSwipe = true;
                                isOpened = true;
                                Log.e("test_touch","mo menu");
//                                NotifyService.getInstance().setPaddingEnable(false);
                            } else {
                                Log.e("test_touch","dong lai menu");
                                mOnMenuStateChangeListener.onMenuClose(mTouchPosition);
//                                NotifyService.getInstance().setPaddingEnable(true);
                                isOpened = false;
                            }
                        }

                        if (isAfterOpen == isBeforeOpen && isAfterOpen == true){
                            NotifyService.getInstance().setPaddingEnable(true);
                        }


                        if (!isAfterOpen) {
                            mTouchPosition = -1;
                            mTouchView = null;
                        }
                    }
                    if (mOnSwipeListener != null) {
                        mOnSwipeListener.onSwipeEnd(mTouchPosition);
                    }
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void smoothOpenMenu(int position) {
        if (position >= getFirstVisiblePosition()
                && position <= getLastVisiblePosition()) {
            View view = getChildAt(position - getFirstVisiblePosition());
            if (view instanceof SwipeMenuLayout) {
                mTouchPosition = position;
                if (mTouchView != null && mTouchView.isOpen()) {
                    mTouchView.smoothCloseMenu();
                }
                mTouchView = (SwipeMenuLayout) view;
                mTouchView.setSwipeDirection(mDirection);
                mTouchView.smoothOpenMenu();
            }
        }
    }

    public void smoothCloseMenu(){
        if (mTouchView != null && mTouchView.isOpen()) {
            mTouchView.smoothCloseMenu();
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getContext().getResources().getDisplayMetrics());
    }

    public void setMenuCreator(SwipeMenuCreator menuCreator) {
        this.mMenuCreator = menuCreator;
    }

    public void setOnMenuItemClickListener(
            OnMenuItemClickListener onMenuItemClickListener) {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOnSwipeListener(OnSwipeListener onSwipeListener) {
        this.mOnSwipeListener = onSwipeListener;
    }

    public void setOnMenuStateChangeListener(OnMenuStateChangeListener onMenuStateChangeListener) {
        mOnMenuStateChangeListener = onMenuStateChangeListener;
    }

    public static interface OnMenuItemClickListener {
        boolean onMenuItemClick(int position, SwipeMenu menu, int index);
    }

    public static interface OnSwipeListener {
        void onSwipeStart(int position);

        void onSwipeEnd(int position);
    }

    public static interface OnMenuStateChangeListener {
        void onMenuOpen(int position);

        void onMenuClose(int position);
    }

    public void setSwipeDirection(int direction) {
        mDirection = direction;
    }


    public static boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        if (ev.getRawX() < x || ev.getRawX() > (x + view.getWidth()) || ev.getRawY() < y || ev.getRawY() > (y + view.getHeight())) {
            return false;
        }
        return true;
    }
}
