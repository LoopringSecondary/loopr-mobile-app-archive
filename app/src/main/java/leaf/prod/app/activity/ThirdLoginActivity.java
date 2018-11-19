package leaf.prod.app.activity;

import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.walletsdk.model.ThirdLoginUser;
import leaf.prod.app.utils.AppManager;
import leaf.prod.walletsdk.util.CurrencyUtil;
import leaf.prod.walletsdk.util.LanguageUtil;
import leaf.prod.app.utils.PermissionUtils;
import leaf.prod.walletsdk.util.ThirdLoginUtil;
import leaf.prod.walletsdk.util.WalletUtil;
import leaf.prod.walletsdk.service.ThirdLoginService;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-27 下午2:29
 */
public class ThirdLoginActivity extends BaseActivity {

    @BindView(R.id.weixin_login)
    ImageView weixinLogin;

    @BindView(R.id.skip_login)
    TextView skipLogin;

    @BindView(R.id.hint_text)
    TextView hintText;

    private ThirdLoginService thirdLoginService;

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
    }

    @Override
    public void initView() {
        if (getIntent().getStringExtra("skip") != null) {
            skipLogin.setVisibility(View.INVISIBLE);
        }
        hintText.setText(getResources().getString(R.string.third_part_hint));
        weixinLogin.setOnClickListener(view -> {
            /**
             * umeng第三方登录
             */
            UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, new UMAuthListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {
                }

                @Override
                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                    ThirdLoginUtil.initThirdLogin(ThirdLoginActivity.this, new ThirdLoginUser(map.get("openid"), LanguageUtil
                            .getSettingLanguage(ThirdLoginActivity.this)
                            .getText(), CurrencyUtil.getCurrency(ThirdLoginActivity.this).name(), null));
                    if (WalletUtil.hasWallet(ThirdLoginActivity.this)) {
                        getOperation().forward(MainActivity.class);
                        // todo 有钱包的情况，让用户选择历史钱包
                    } else {
                        getOperation().forward(CoverActivity.class);
                    }
                    finish();
                }

                @Override
                public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                    Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media, int i) {
                }
            });
        });
        skipLogin.setOnClickListener(view -> {
            ThirdLoginUtil.skip(ThirdLoginActivity.this);
            if (WalletUtil.hasWallet(ThirdLoginActivity.this)) {
                AppManager.getAppManager().finishAllActivity();
                getOperation().forwardClearTop(MainActivity.class);
            } else {
                getOperation().forwardClearTop(CoverActivity.class);
            }
        });
    }

    @Override
    public void initData() {
        thirdLoginService = new ThirdLoginService();
    }

    protected void onCreate(Bundle bundle) {
        setContentView(R.layout.activity_third_login);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        super.onCreate(bundle);
        mSwipeBackLayout.setEnableGesture(false);
        PermissionUtils.initPermissions(this);
    }
}
