package leaf.prod.app.layout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import leaf.prod.app.R;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-12-13 1:57 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public class IndexBarLayout extends LinearLayout {

    public IndexBarLayout(Context context) {
        super(context);
    }

    public IndexBarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexBarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public IndexBarLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TextView findViewByXY(View view, int x, int y) {
        TextView target = null;
        if (view instanceof ViewGroup) {
            ViewGroup v = (ViewGroup) view;
            for (int i = 0; i < v.getChildCount(); i++) {
                TextView child = (TextView) v.getChildAt(i);
                child.setTextColor(getResources().getColor(R.color.colorNineText));
                if (x >= child.getLeft() && x <= child.getRight() && y >= child.getTop() && y <= child.getBottom()) {
                    target = child;
                }
            }
        }
        return target;
    }
}
