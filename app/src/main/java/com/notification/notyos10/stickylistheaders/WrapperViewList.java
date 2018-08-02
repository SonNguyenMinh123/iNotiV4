package com.notification.notyos10.stickylistheaders;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.notification.notyos10.adapters.SwipeMenuAdapter;
import com.notification.notyos10.swipemenulistview.SwipeMenu;
import com.notification.notyos10.swipemenulistview.SwipeMenuCreator;
import com.notification.notyos10.swipemenulistview.SwipeMenuLayout;
import com.notification.notyos10.swipemenulistview.SwipeMenuView;


public class WrapperViewList extends ListView {

	public interface LifeCycleListener {
		void onDispatchDrawOccurred(Canvas canvas);
	}

	private LifeCycleListener mLifeCycleListener;
	private List<View> mFooterViews;
	private int mTopClippingLength;
	private Rect mSelectorRect = new Rect();// for if reflection fails
	private Field mSelectorPositionField;
	private boolean mClippingToPadding = true;
    private boolean mBlockLayoutChildren = false;

	public WrapperViewList(Context context) {
		super(context);

		try {
			Field selectorRectField = AbsListView.class.getDeclaredField("mSelectorRect");
			selectorRectField.setAccessible(true);
			mSelectorRect = (Rect) selectorRectField.get(this);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				mSelectorPositionField = AbsListView.class.getDeclaredField("mSelectorPosition");
				mSelectorPositionField.setAccessible(true);
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}


		init();

	}

	@Override
	public boolean performItemClick(View view, int position, long id) {
		if (view instanceof WrapperView) {
			view = ((WrapperView) view).mItem;
		}
		return super.performItemClick(view, position, id);
	}

	private void positionSelectorRect() {
		if (!mSelectorRect.isEmpty()) {
			int selectorPosition = getSelectorPosition();
			if (selectorPosition >= 0) {
				int firstVisibleItem = getFixedFirstVisibleItem();
				View v = getChildAt(selectorPosition - firstVisibleItem);
				if (v instanceof WrapperView) {
					WrapperView wrapper = ((WrapperView) v);
					mSelectorRect.top = wrapper.getTop() + wrapper.mItemTop;
				}
			}
		}
	}

	private int getSelectorPosition() {
		if (mSelectorPositionField == null) { // not all supported andorid
			// version have this variable
			for (int i = 0; i < getChildCount(); i++) {
				if (getChildAt(i).getBottom() == mSelectorRect.bottom) {
					return i + getFixedFirstVisibleItem();
				}
			}
		} else {
			try {
				return mSelectorPositionField.getInt(this);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return -1;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		positionSelectorRect();
		if (mTopClippingLength != 0) {
			canvas.save();
			Rect clipping = canvas.getClipBounds();
			clipping.top = mTopClippingLength;
			canvas.clipRect(clipping);
			super.dispatchDraw(canvas);
			canvas.restore();
		} else {
			super.dispatchDraw(canvas);
		}
		mLifeCycleListener.onDispatchDrawOccurred(canvas);
	}

	void setLifeCycleListener(LifeCycleListener lifeCycleListener) {
		mLifeCycleListener = lifeCycleListener;
	}

	@Override
	public void addFooterView(View v) {
		super.addFooterView(v);
		addInternalFooterView(v);
	}

	@Override
	public void addFooterView(View v, Object data, boolean isSelectable) {
		super.addFooterView(v, data, isSelectable);
		addInternalFooterView(v);
	}

	private void addInternalFooterView(View v) {
		if (mFooterViews == null) {
			mFooterViews = new ArrayList<View>();
		}
		mFooterViews.add(v);
	}

	@Override
	public boolean removeFooterView(View v) {
		if (super.removeFooterView(v)) {
			mFooterViews.remove(v);
			return true;
		}
		return false;
	}

	boolean containsFooterView(View v) {
		if (mFooterViews == null) {
			return false;
		}
		return mFooterViews.contains(v);
	}

	public void setTopClippingLength(int topClipping) {
		mTopClippingLength = topClipping;
	}

	int getFixedFirstVisibleItem() {
		int firstVisibleItem = getFirstVisiblePosition();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			return firstVisibleItem;
		}

		// first getFirstVisiblePosition() reports items
		// outside the view sometimes on old versions of android
		for (int i = 0; i < getChildCount(); i++) {
			if (getChildAt(i).getBottom() >= 0) {
				firstVisibleItem += i;
				break;
			}
		}

		// work around to fix bug with firstVisibleItem being to high
		// because list view does not take clipToPadding=false into account
		// on old versions of android
		if (!mClippingToPadding && getPaddingTop() > 0 && firstVisibleItem > 0) {
			if (getChildAt(0).getTop() > 0) {
				firstVisibleItem -= 1;
			}
		}

		return firstVisibleItem;
	}

	@Override
	public void setClipToPadding(boolean clipToPadding) {
		mClippingToPadding = clipToPadding;
		super.setClipToPadding(clipToPadding);
	}

    public void setBlockLayoutChildren(boolean block) {
        mBlockLayoutChildren = block;
    }

    @Override
    protected void layoutChildren() {
        if (!mBlockLayoutChildren) {
            super.layoutChildren();
        }
    }

	/*test swipe*/
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

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		Log.e("test_swipe","on touch swraplist");
		if (ev.getAction() != MotionEvent.ACTION_DOWN && mTouchView == null)
			return super.onTouchEvent(ev);
		int action = ev.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				int oldPos = mTouchPosition;
				mDownX = ev.getX();
				mDownY = ev.getY();
				mTouchState = TOUCH_STATE_NONE;

				mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY());

				if (mTouchPosition == oldPos && mTouchView != null
						&& mTouchView.isOpen()) {
					mTouchState = TOUCH_STATE_X;
					mTouchView.onSwipe(ev);
					return true;
				}

				View view = getChildAt(mTouchPosition - getFirstVisiblePosition());

				if (mTouchView != null && mTouchView.isOpen()) {
					mTouchView.smoothCloseMenu();
					mTouchView = null;
					// return super.onTouchEvent(ev);
					// try to cancel the touch event
					MotionEvent cancelEvent = MotionEvent.obtain(ev);
					cancelEvent.setAction(MotionEvent.ACTION_CANCEL);
					onTouchEvent(cancelEvent);
					if (mOnMenuStateChangeListener != null) {
						mOnMenuStateChangeListener.onMenuClose(oldPos);
					}
					return true;
				}
				if (view instanceof SwipeMenuLayout) {
					mTouchView = (SwipeMenuLayout) view;
					mTouchView.setSwipeDirection(mDirection);
				}
				if (mTouchView != null) {
					mTouchView.onSwipe(ev);
				}
				break;
			case MotionEvent.ACTION_MOVE:
				//有些可能有header,要减去header再判断
				mTouchPosition = pointToPosition((int) ev.getX(), (int) ev.getY()) - getHeaderViewsCount();
				//如果滑动了一下没完全展现，就收回去，这时候mTouchView已经赋值，再滑动另外一个不可以swip的view
				//会导致mTouchView swip 。 所以要用位置判断是否滑动的是一个view
				if (!mTouchView.getSwipEnable() || mTouchPosition != mTouchView.getPosition()) {
					break;
				}
				float dy = Math.abs((ev.getY() - mDownY));
				float dx = Math.abs((ev.getX() - mDownX));
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
				if (mTouchState == TOUCH_STATE_X) {
					if (mTouchView != null) {
						boolean isBeforeOpen = mTouchView.isOpen();
						mTouchView.onSwipe(ev);
						boolean isAfterOpen = mTouchView.isOpen();
						if (isBeforeOpen != isAfterOpen && mOnMenuStateChangeListener != null) {
							if (isAfterOpen) {
								mOnMenuStateChangeListener.onMenuOpen(mTouchPosition);
							} else {
								mOnMenuStateChangeListener.onMenuClose(mTouchPosition);
							}
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

				//只在空的时候赋值 以免每次触摸都赋值，会有多个open状态
				if (view instanceof SwipeMenuLayout) {
					//如果有打开了 就拦截.
					if (mTouchView != null && mTouchView.isOpen() && !inRangeOfView(mTouchView.getMenuView(), ev)) {
						return true;
					}
					mTouchView = (SwipeMenuLayout) view;
					mTouchView.setSwipeDirection(mDirection);
				}
				//如果摸在另外个view
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
					//每次拦截的down都把触摸状态设置成了TOUCH_STATE_NONE 只有返回true才会走onTouchEvent 所以写在这里就够了
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
				}
				if (mTouchView != null && !flag) {
					mTouchView.smoothCloseMenu();
				}
			}
		});
	}

	private void init() {
		MAX_X = dp2px(MAX_X);
		MAX_Y = dp2px(MAX_Y);
		mTouchState = TOUCH_STATE_NONE;
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getContext().getResources().getDisplayMetrics());
	}

	public Interpolator getOpenInterpolator() {
		return mOpenInterpolator;
	}

	public Interpolator getCloseInterpolator() {
		return mCloseInterpolator;
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

	public void setSwipeDirection(int direction) {
		mDirection = direction;
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
