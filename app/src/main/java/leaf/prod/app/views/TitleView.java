package leaf.prod.app.views;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.activity.wallet.MainActivity;
import leaf.prod.app.view.Operation;

public class TitleView extends FrameLayout implements View.OnClickListener {

    /**
     * 左上角返回按钮
     */
    @BindView(R.id.left_btn)
    ImageView mLeftBtn;

    @BindView(R.id.title_text)
    TextView bTitle;

    @BindView(R.id.title_view)
    LinearLayout titleView;

    @BindView(R.id.drop_down_btn)
    ImageView mDropdownbtn;

    @BindView(R.id.mid_btn)
    ImageView mMiddlebtn;

    @BindView(R.id.right_btn)
    ImageView mRightbtn;

    @BindView(R.id.right_text)
    TextView mRightText;

    private OnLeftButtonClickListener mOnLeftButtonClickListener;

    private OnDropdownButtonClickListener mOnDropdownButtonClickListener;

    private OnMiddleButtonClickListener mOnMiddleButtonClickListener;

    private OnRightButtonClickListener mOnRightButtonClickListener;

    private OnmTitleClickListener mTitleClickListener;

    private Activity activity;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()) {
            return;
        }
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.title_view, this, true);
        ButterKnife.bind(this);
        mLeftBtn.setVisibility(View.INVISIBLE);
        mLeftBtn.setOnClickListener(this);
        mDropdownbtn.setVisibility(View.INVISIBLE);
        mDropdownbtn.setOnClickListener(this);
        mMiddlebtn.setVisibility(View.INVISIBLE);
        mMiddlebtn.setOnClickListener(this);
        mRightbtn.setVisibility(View.INVISIBLE);
        mRightbtn.setOnClickListener(this);
        bTitle.setVisibility(View.INVISIBLE);
    }

    public void setLeftButton(OnLeftButtonClickListener listener) {
        mLeftBtn.setVisibility(View.VISIBLE);
        mOnLeftButtonClickListener = listener;
    }

    /**
     * @param context
     */
    public void clickLeftGoBack(final WeakReference<Activity> context) {
        mLeftBtn.setVisibility(View.VISIBLE);
        mOnLeftButtonClickListener = button -> context.get().finish();
    }

    public void clickLeftGoBack(int imgResource, final WeakReference<Activity> context) {
        mLeftBtn.setVisibility(View.VISIBLE);
        mLeftBtn.setImageResource(imgResource);
        mOnLeftButtonClickListener = button -> context.get().finish();
    }

    public void clickLeftGoBack(final WeakReference<Activity> context, Operation operation, Class<? extends Activity> clazz) {
        mLeftBtn.setVisibility(View.VISIBLE);
        mOnLeftButtonClickListener = button -> {
            context.get().finish();
            operation.forward(clazz);
        };
    }

    public void clickLeftGoMain(final WeakReference<Activity> context) {
        mLeftBtn.setVisibility(View.VISIBLE);
        mOnLeftButtonClickListener = button -> context.get()
                .getApplication()
                .startActivity(new Intent(context.get().getApplication(), MainActivity.class));
    }

    public void setDropdownImageButton(int srcId, OnDropdownButtonClickListener listener) {
        mDropdownbtn.setVisibility(View.VISIBLE);
        mDropdownbtn.setImageResource(srcId);
        mOnDropdownButtonClickListener = listener;
    }

    public void setMiddleImageButton(int srcId, OnMiddleButtonClickListener listener) {
        mMiddlebtn.setVisibility(View.VISIBLE);
        mMiddlebtn.setImageResource(srcId);
        mOnMiddleButtonClickListener = listener;
    }

    public void setRightText(String str, OnRightButtonClickListener listener) {
        mRightText.setText(str);
        mRightText.setVisibility(View.VISIBLE);
        mOnRightButtonClickListener = listener;
    }

    public void setRightImageButton(int srcId, OnRightButtonClickListener listener) {
        mRightbtn.setVisibility(View.VISIBLE);
        mRightbtn.setImageResource(srcId);
        mOnRightButtonClickListener = listener;
    }

    public void hideRightImageButton() {
        mRightbtn.setVisibility(View.INVISIBLE);
    }

    public void setHeight(int height) {
        titleView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, height));
    }

    // 居中标题设置
    public void setBTitle(int stringID) {
        bTitle.setVisibility(View.VISIBLE);
        bTitle.setText(stringID);
    }

    // 居中标题设置
    public void setBTitle(String stringID) {
        bTitle.setVisibility(View.VISIBLE);
        bTitle.setText(stringID);
    }

    // 居中标题设置不带返回按钮
    public void setBTitleNoBar(String stringID) {
        bTitle.setVisibility(View.VISIBLE);
        bTitle.setText(stringID);
        mLeftBtn.setVisibility(INVISIBLE);
    }

    @OnClick({R.id.left_btn, R.id.drop_down_btn, R.id.mid_btn, R.id.right_btn, R.id.right_text})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_btn:
                if (mOnLeftButtonClickListener != null)
                    mOnLeftButtonClickListener.onClick(view);
                break;
            case R.id.drop_down_btn:
                if (mOnDropdownButtonClickListener != null)
                    mOnDropdownButtonClickListener.onClick(view);
                break;
            case R.id.mid_btn:
                if (mOnMiddleButtonClickListener != null)
                    mOnMiddleButtonClickListener.onClick(view);
                break;
            case R.id.right_btn:
                if (mOnRightButtonClickListener != null)
                    mOnRightButtonClickListener.onClick(view);
                break;
            case R.id.right_text:
                if (mOnRightButtonClickListener != null)
                    mOnRightButtonClickListener.onClick(view);
                break;
        }
    }

    public interface OnLeftButtonClickListener {

        void onClick(View button);
    }

    public interface OnDropdownButtonClickListener {

        void onClick(View button);
    }

    public interface OnMiddleButtonClickListener {

        void onClick(View button);
    }

    public interface OnRightButtonClickListener {

        void onClick(View button);
    }

    public interface OnmTitleClickListener {

        void onClick(View text);
    }
}
