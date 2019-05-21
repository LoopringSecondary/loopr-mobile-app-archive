package leaf.prod.app.layout;

import java.lang.reflect.Field;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import me.kaelaela.verticalviewpager.VerticalViewPager;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2019-01-14 3:45 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public class MyVerticalViewPager extends VerticalViewPager {

    private boolean isPagingEnabled = false;

    public MyVerticalViewPager(Context context) {
        super(context);
        setViewPagerScrollSpeed(this, context);
    }

    public MyVerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setViewPagerScrollSpeed(this, context);
    }

    private void setViewPagerScrollSpeed(MyVerticalViewPager mViewPager, Context context) {
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(context);
            mScroller.set(mViewPager, scroller);
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean pagingEnabled) {
        isPagingEnabled = pagingEnabled;
    }

    public class FixedSpeedScroller extends Scroller {

        private int mDuration = 1000;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }
}
