package leaf.prod.app.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.commonsdk.debug.E;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.utils.FingerprintUtil;
import leaf.prod.app.utils.SPUtils;
import leaf.prod.app.utils.ToastUtils;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.ll_finger_auth)
    LinearLayout llFingerAuth;

    @BindView(R.id.auth_tip)
    TextView fingerTip;

    private FingerprintIdentify identify;

    private static final int MAX_AVAILABLE_TIMES = 5;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if ((boolean) SPUtils.get(SplashActivity.this, "hasWallet", false)) {
                getOperation().forward(MainActivity.class);
            } else {
                getOperation().forward(CoverActivity.class);
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
    }

    @Override
    public void initView() {

        boolean needFinger = (boolean) SPUtils.get(this, "touch_id", false);
        if (FingerprintUtil.isEnable(this) && needFinger) {
            identify = new FingerprintIdentify(this, exception -> {
            });
        } else {
            llFingerAuth.setVisibility(View.GONE);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }
    }

    @Override
    public void initData() {
    }

    @OnClick(R.id.ll_finger_auth)
    public void onViewClicked() {
        fingerTip.setTextColor(getResources().getColor(R.color.colorGreen));
        identify.startIdentify(MAX_AVAILABLE_TIMES, new BaseFingerprint.FingerprintIdentifyListener() {
            @Override
            public void onSucceed() {
                mHandler.sendEmptyMessageDelayed(1, 1000);
            }

            @Override
            public void onNotMatch(int availableTimes) {
                fingerTip.setTextColor(getResources().getColor(R.color.colorRed));
                String tip = getString(R.string.auth_finger_no_match, availableTimes);
                fingerTip.setText(tip);
            }

            @Override
            public void onFailed(boolean isDeviceLocked) {
                if (isDeviceLocked) {
                    fingerTip.setTextColor(getResources().getColor(R.color.colorRed));
                    String tip = getString(R.string.auth_finger_failed);
                    fingerTip.setText(tip);
                }
            }

            @Override
            public void onStartFailedByDeviceLocked() {
            }
        });
    }
}
