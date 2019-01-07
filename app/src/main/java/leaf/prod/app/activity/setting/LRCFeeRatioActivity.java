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
import leaf.prod.walletsdk.manager.SettingDataManager;

public class LRCFeeRatioActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.tv_ratio)
    TextView tvRatio;

    @BindView(R.id.seekBar)
    BubbleSeekBar seekBar;

    private SettingDataManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_lrc_fee_ratio);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
    }

    @Override
    protected void initPresenter() {
        manager = SettingDataManager.getInstance(this);
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.set_lrc_proportion));
        title.clickLeftGoBack(getWContext());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        seekBar.setProgress(manager.getLrcFeeFloat() * 1000);
        tvRatio.setText(getResources().getString(R.string.set_lrc_proportion) + " " + manager.getLrcFeeString());
    }

    @Override
    public void initData() {
        seekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                manager.setLrcFee(progress);
                tvRatio.setText(getResources().getString(R.string.set_lrc_proportion) + manager.getLrcFeeString());
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
