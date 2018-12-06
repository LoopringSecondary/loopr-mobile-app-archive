package leaf.prod.app.layout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class ChildClickableLinearLayout extends LinearLayout {

    private boolean childClickable = true;

    public ChildClickableLinearLayout(@NonNull Context context) {
        super(context);
    }

    public ChildClickableLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ChildClickableLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return !childClickable;
    }

    public void setChildClickable(boolean clickable) {
        childClickable = clickable;
    }
}
