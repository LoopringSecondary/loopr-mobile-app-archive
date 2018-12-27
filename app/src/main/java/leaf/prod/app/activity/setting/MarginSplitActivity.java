package leaf.prod.app.activity.setting;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.xw.repo.BubbleSeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.activity.BaseActivity;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.util.SPUtils;

public class MarginSplitActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.seekBar)
    BubbleSeekBar seekBar;

    @BindView(R.id.tv_margin)
    TextView tvMargin;

    private int value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_margin_split);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.set_margin_split));
        title.clickLeftGoBack(getWContext());
        title.setRightText(getResources().getString(R.string.save), button -> {
            SPUtils.put(MarginSplitActivity.this, "margin", value);
            finish();
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        value = (int) SPUtils.get(MarginSplitActivity.this, "margin", 0);
        seekBar.setProgress(value);
        tvMargin.setText("差价分成" + value + "%");
    }

    @Override
    public void initData() {
        seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                value = (int) bubbleSeekBar.getMin();
                tvMargin.setText("差价分成" + value + "%");
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
