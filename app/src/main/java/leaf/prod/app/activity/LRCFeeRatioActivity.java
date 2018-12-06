package leaf.prod.app.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.walletsdk.util.SPUtils;
import leaf.prod.app.views.TitleView;

public class LRCFeeRatioActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.tv_ratio)
    TextView tvRatio;

    @BindView(R.id.seekBar)
    BubbleSeekBar seekBar;

    private int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_lrc_fee_ratio);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.set_lrc_proportion));
        title.clickLeftGoBack(getWContext());
        title.setRightText(getResources().getString(R.string.save), button -> {
            SPUtils.put(LRCFeeRatioActivity.this, "ratio", value);
            finish();
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        value = (int) SPUtils.get(LRCFeeRatioActivity.this, "ratio", 2);
        seekBar.setProgress(value);
        tvRatio.setText(getResources().getString(R.string.set_lrc_proportion) + value + "‰");
    }

    @Override
    public void initData() {
        seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                value = progress;
                tvRatio.setText(getResources().getString(R.string.set_lrc_proportion) + bubbleSeekBar.getProgress() + "‰");
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
            }
        });
    }
}
