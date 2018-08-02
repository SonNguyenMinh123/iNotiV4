package com.notification.notyos10.customviews.swipedown;

/**
 * Created by nyuwono on 11/17/15.
 */

import android.view.View;

/**
 * Listener for monitoring events about panels.
 */
public interface ToolbarPanelListener {
    /**
     * Called when a panel's position changes.
     * @param toolbar Toolbar view that is now open
     * @param panelView The child view that was moved
     * @param slideOffset The new offset of this panel within its range, from 0-1
     */
    public void onPanelSlide(View toolbar, View panelView, float slideOffset, boolean isTouch, long currentTime, float currentY,float upX);

    public void onPanelClose(View toolbar, View panelView, float slideOffset, boolean isTouch, long currentTime, float currentY);

    /**
     * Called when a panel has settled in a completely open state.
     * The panel is interactive at this point.
     *
     * @param toolbar Toolbar view that is now open
     * @param panelView Panel view that is now open
     */
    public void onPanelOpened(View toolbar, View panelView);

    /**
     * Called when a panel has settled in a completely closed state.
     *
     * @param toolbar Toolbar view that is now closed
     * @param panelView Panel view that is now closed
     */
    public void onPanelClosed(View toolbar, View panelView);

}