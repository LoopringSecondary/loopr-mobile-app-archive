package leaf.prod.app.presenter.wallet;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import leaf.prod.app.R;
import leaf.prod.app.activity.wallet.AddContactActivity;
import leaf.prod.app.activity.wallet.ContactListActivity;
import leaf.prod.app.presenter.BasePresenter;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-12-12 11:37 AM
 * Cooperation: loopring.org 路印协议基金会
 */
public class ContactListPresenter extends BasePresenter<ContactListActivity> {

    private TextView currentTv = null;

    private AlertDialog dialog;

    private View dialogView;

    private int dialogWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120,
            context.getResources().getDisplayMetrics());

    private int dialogHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50,
            context.getResources().getDisplayMetrics());

    private int screenWidth;

    public ContactListPresenter(ContactListActivity view, Context context) {
        super(view, context);
        screenWidth = view.getWindowManager().getDefaultDisplay().getWidth();
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

    @SuppressLint("RtlHardcoded")
    public void showContactOptionDialog(Point point, int position) {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);
            dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_contact_option, null);
            builder.setView(dialogView);
            dialog = builder.create();
            dialog.setCancelable(true);
            dialog.setCanceledOnTouchOutside(true);
        }
        dialogView.findViewById(R.id.tv_edit).setOnClickListener(v -> {
            view.getOperation().addParameter("address", view.adapter.getData().get(position).getAddress());
            view.getOperation().forward(AddContactActivity.class);
        });
        ImageView ivLeftTriangle = dialogView.findViewById(R.id.left_triangle);
        ImageView ivRightTriangle = dialogView.findViewById(R.id.right_triangle);
        dialog.show();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        if (point.x <= screenWidth / 2) {
            layoutParams.x = point.x - view.recyclerView.getLeft();
            ivLeftTriangle.setVisibility(View.VISIBLE);
            ivRightTriangle.setVisibility(View.GONE);
        } else {
            layoutParams.x = point.x - view.recyclerView.getLeft() - dialogWidth;
            ivLeftTriangle.setVisibility(View.GONE);
            ivRightTriangle.setVisibility(View.VISIBLE);
        }
        layoutParams.y = point.y - view.recyclerView.getTop() - 50;
        layoutParams.height = dialogHeight;
        layoutParams.width = dialogWidth;
        window.setAttributes(layoutParams);
    }

    public void hideContactOptionDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.hide();
        }
    }
}
