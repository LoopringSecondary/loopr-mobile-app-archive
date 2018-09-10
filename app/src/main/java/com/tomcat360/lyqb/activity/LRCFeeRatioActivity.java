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

public class LRCFeeRatioActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.tv_ratio)
    TextView tvRatio;

    @BindView(R.id.seekBar)
    RangeSeekBar seekBar;

    private int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_lrc_fee_ratio);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.set_lrc_proportion));
        title.clickLeftGoBack(getWContext());
        title.setRightButton(getResources().getString(R.string.save), new TitleView.OnRightButtonClickListener() {
            @Override
            public void onClick(View button) {
                SPUtils.put(LRCFeeRatioActivity.this, "ratio", value);
                finish();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        value = (int) SPUtils.get(LRCFeeRatioActivity.this, "ratio", 2);
        seekBar.setValue(value);
        tvRatio.setText("LRC费用比例" + value + "‰");
    }

    @Override
    public void initData() {
        seekBar.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max, boolean isFromUser) {
                value = (int) min;
                tvRatio.setText("LRC费用比例" + value + "‰");
            }
        });
    }
}
