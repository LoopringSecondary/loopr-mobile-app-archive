package leaf.prod.app.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.utils.FingerprintUtil;
import leaf.prod.app.utils.SPUtils;
import leaf.prod.app.utils.ThirdLoginUtil;
import leaf.prod.app.utils.WalletUtil;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.ll_finger_auth)
    LinearLayout llFingerAuth;

    @BindView(R.id.auth_image)
    ImageView fingerImage;

    @BindView(R.id.auth_tip)
    TextView fingerTip;

    private int count = 4;

    private Animation animation;

    private boolean isAuthFailed = false;

    private boolean isAuthenticated = false;

    private FingerprintManagerCompat fingerprintManager;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (WalletUtil.hasWallet(SplashActivity.this)) {
                        if (!ThirdLoginUtil.isThirdLogin(SplashActivity.this) && !ThirdLoginUtil.isSkip(SplashActivity.this)) {
                            getOperation().forwardClearTop(ThirdLoginActivity.class);
                        } else {
                            getOperation().forwardClearTop(MainActivity.class);
                        }
                    } else {
                        if (!ThirdLoginUtil.isThirdLogin(SplashActivity.this) && !ThirdLoginUtil.isSkip(SplashActivity.this)) {
                            getOperation().forwardClearTop(ThirdLoginActivity.class);
                        } else {
                            getOperation().forwardClearTop(CoverActivity.class);
                        }
                    }
                    break;
                case 2:
                    count = 4;
                    isAuthFailed = false;
                    fingerTip.setText(getText(R.string.auth_finger_tip));
                    fingerTip.setTextColor(getResources().getColor(R.color.colorGreen));
                    fingerprintManager.authenticate(null, 0, null, new FingerCallBack(), null);
                    break;
            }
        }
    };

    private CancellationSignal cancellationSignal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        ThirdLoginUtil.updateRemote(this);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
    }

    @Override
    public void initView() {
        cancellationSignal = new CancellationSignal();
        fingerprintManager = FingerprintManagerCompat.from(this);
        boolean needFinger = (boolean) SPUtils.get(this, "touch_id", false);
        if (FingerprintUtil.isEnable(this) && needFinger) {
            fingerTip.setVisibility(View.INVISIBLE);
            animation = new TranslateAnimation(0, 10, 0, 0);
            animation.setDuration(700);
            animation.setRepeatMode(Animation.REVERSE);
            animation.setInterpolator(new CycleInterpolator(7));
        } else {
            llFingerAuth.setVisibility(View.GONE);
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }
        handleClick();
    }

    @Override
    public void initData() {
    }

    @OnClick(R.id.ll_finger_auth)
    public void onViewClicked() {
        handleClick();
    }

    private void handleClick() {
        if (isAuthFailed) {
            fingerTip.setVisibility(View.VISIBLE);
            fingerTip.setText(getText(R.string.auth_finger_failed));
            fingerTip.setTextColor(getResources().getColor(R.color.colorRed));
        } else {
            fingerTip.setVisibility(View.VISIBLE);
            fingerTip.setText(getText(R.string.auth_finger_tip));
            fingerTip.setTextColor(getResources().getColor(R.color.colorGreen));
        }
        if (!isAuthenticated) {
            isAuthenticated = true;
            fingerprintManager.authenticate(null, 0, cancellationSignal, new FingerCallBack(), null);
        }
    }

    private class FingerCallBack extends FingerprintManagerCompat.AuthenticationCallback {

        // 多次识别失败, 并且，不能短时间内调用指纹验证
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            super.onAuthenticationError(errMsgId, errString);
            if (count == 0 || errMsgId == 7) {
                isAuthFailed = true;
                fingerTip.setText(getString(R.string.auth_finger_failed));
            } else {
                fingerTip.setText(getString(R.string.auth_finger_no_match, count));
                count--;
            }
            fingerTip.setTextColor(getResources().getColor(R.color.colorRed));
            mHandler.sendEmptyMessageDelayed(2, 1000 * 60);
        }

        //出错可恢复
        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            super.onAuthenticationHelp(helpMsgId, helpString);
        }

        //识别成功
        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            cancellationSignal.cancel();
            mHandler.sendEmptyMessageDelayed(1, 1000);
        }

        //识别失败
        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            if (count > 0) {
                fingerTip.setText(getString(R.string.auth_finger_no_match, count));
                fingerTip.setTextColor(getResources().getColor(R.color.colorRed));
                count--;
            }
            if (animation != null) {
                fingerImage.startAnimation(animation);
            }
        }
    }
}
