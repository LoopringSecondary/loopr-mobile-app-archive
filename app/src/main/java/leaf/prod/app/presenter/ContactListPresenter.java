package leaf.prod.app.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import leaf.prod.app.R;
import leaf.prod.app.activity.ContactListActivity;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-12-12 11:37 AM
 * Cooperation: loopring.org 路印协议基金会
 */
public class ContactListPresenter extends BasePresenter<ContactListActivity> {

    private TextView currentTv = null;

    public ContactListPresenter(ContactListActivity view, Context context) {
        super(view, context);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initIndexBar() {
        for (int i = 0; i < 27; ++i) {
            String name = i != 0 ? (char) ('a' + (i - 1)) + "" : "#";
            TextView textView = new TextView(context);
            textView.setId(View.generateViewId());
            textView.setWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, view.getResources()
                    .getDisplayMetrics()));
            textView.setHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, view.getResources()
                    .getDisplayMetrics()));
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            textView.setText(name.toUpperCase());
            textView.setTextColor(view.getResources().getColor(R.color.colorNineText));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            textView.setTypeface(null, Typeface.BOLD);
            view.llIndex.addView(textView);
            view.tvMap.put(name, textView);
        }
        view.llIndex.setOnTouchListener((v, motionEvent) -> {
            TextView tv = view.llIndex.findViewByXY(view.llIndex, (int) motionEvent.getX(), (int) motionEvent
                    .getY());
            if (tv != null) {
                currentTv = tv;
                Integer index = view.indexMap.get(tv.getText().toString().toLowerCase());
                if (index != null) {
                    view.layoutManager.scrollToPositionWithOffset(index, 0);
                }
            }
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                currentTv.setTextColor(view.getResources().getColor(R.color.colorCenter));
            }
            return true;
        });
    }

    public void highLightBar(String tag) {
        for (String key : view.tvMap.keySet()) {
            view.tvMap.get(key).setTextColor(view.getResources().getColor(R.color.colorNineText));
        }
        TextView textView = view.tvMap.get(tag.toLowerCase());
        textView.setTextColor(view.getResources().getColor(R.color.colorCenter));
    }
}
