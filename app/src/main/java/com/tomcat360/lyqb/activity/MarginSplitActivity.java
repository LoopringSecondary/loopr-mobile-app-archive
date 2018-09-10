package com.tomcat360.lyqb.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tomcat360.lyqb.R;
import com.tomcat360.lyqb.utils.SPUtils;
import com.tomcat360.lyqb.views.RangeSeekBar;
import com.tomcat360.lyqb.views.TitleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MarginSplitActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.seekBar)
    RangeSeekBar seekBar;

    @BindView(R.id.tv_margin)
    TextView tvMargin;

    private int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_margin_split);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.set_margin_split));
        title.clickLeftGoBack(getWContext());
        title.setRightButton("保存", new TitleView.OnRightButtonClickListener() {
            @Override
            public void onClick(View button) {
                SPUtils.put(MarginSplitActivity.this, "margin", value);
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        value = (int) SPUtils.get(MarginSplitActivity.this, "margin", 0);
        seekBar.setValue(value);
        tvMargin.setText("差价分成" + value + "%");
    }

    @Override
    public void initData() {
        seekBar.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max, boolean isFromUser) {
                value = (int) min;
                tvMargin.setText("差价分成" + value + "%");
            }
        });
    }
}
