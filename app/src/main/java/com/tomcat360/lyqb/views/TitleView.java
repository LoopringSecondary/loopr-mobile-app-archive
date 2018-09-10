package com.tomcat360.lyqb.views;

import java.lang.ref.WeakReference;

import com.tomcat360.lyqb.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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

public class TitleView extends FrameLayout implements View.OnClickListener {

    /**
     * 左上角返回按钮
     */
    @BindView(R.id.left_btn)
    ImageView mLeftBtn;

    @BindView(R.id.title_text)
    TextView bTitle;

    @BindView(R.id.right_text)
    TextView mRightText;

    @BindView(R.id.right_btn)
    ImageView mRightbtn;

    @BindView(R.id.title_view)
    LinearLayout titleView;

    private OnLeftButtonClickListener mOnLeftButtonClickListener;

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
        //		if (G.SYSTEM_SDK_API < 19) {
        //			blank.setVisibility(View.GONE);
        //		} else {
        //			blank.setVisibility(View.VISIBLE);
        //		}
        mLeftBtn.setVisibility(View.INVISIBLE);
        mLeftBtn.setOnClickListener(this);
        mRightText.setVisibility(View.GONE);
        mRightText.setOnClickListener(this);
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
        mOnLeftButtonClickListener = new OnLeftButtonClickListener() {
            @Override
            public void onClick(View button) {

                context.get().finish();
            }
        };
    }

    public void clickLeftGoBack(int imgResource, final WeakReference<Activity> context) {

        mLeftBtn.setVisibility(View.VISIBLE);
        mLeftBtn.setImageResource(imgResource);
        mOnLeftButtonClickListener = new OnLeftButtonClickListener() {
            @Override
            public void onClick(View button) {

                context.get().finish();
            }
        };
    }

    public void setRightButton(String str, OnRightButtonClickListener listener) {

        mRightText.setText(str);
        mRightText.setVisibility(View.VISIBLE);
        mRightbtn.setVisibility(View.GONE);
        mOnRightButtonClickListener = listener;
    }

    public void setRightImageButton(int srcId, OnRightButtonClickListener listener) {

        mRightText.setVisibility(View.GONE);
        mRightbtn.setVisibility(View.VISIBLE);
        mRightbtn.setImageResource(srcId);
        mOnRightButtonClickListener = listener;
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

    // 居中标题设置String
    public void setStringBTitle(String str) {

        bTitle.setVisibility(View.VISIBLE);
        bTitle.setText(str);
    }

    public void setBTitleColor() {

        bTitle.setTextColor(Color.rgb(0x00, 0x00, 0x00));
        // TextPaint tpPaint = bTitle.getPaint();
        // tpPaint.setFakeBoldText(true); // 加粗
        titleView.setBackgroundColor(Color.rgb(0xf7, 0xf7, 0xf7));
        // mRightText.setTextColor(Color.rgb(0xff, 0xff, 0xff));
    }

    @OnClick({R.id.left_btn, R.id.right_text, R.id.right_btn})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.left_btn:
                if (mOnLeftButtonClickListener != null)
                    mOnLeftButtonClickListener.onClick(view);
                break;

            case R.id.right_text:
            case R.id.right_btn:
                if (mOnRightButtonClickListener != null)
                    mOnRightButtonClickListener.onClick(view);
                break;
        }
    }

    public interface OnLeftButtonClickListener {

        void onClick(View button);
    }

    public interface OnRightButtonClickListener {

        void onClick(View button);
    }

    public interface OnmTitleClickListener {

        void onClick(View text);
    }
}
