package com.tomcat360.lyqb.views.swipeback;

/**
 * @author Yrom
 */
public interface SwipeBackActivityBase {

    /**
     * @return the SwipeBackLayout associated with this activity.
     */
    SwipeBackLayout getSwipeBackLayout();

    void setSwipeBackEnable(boolean enable);

    /**
     * Scroll out contentView and finish the activity
     */
    void scrollToFinishActivity();
}
