package leaf.prod.app.activity;

import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.utils.AppManager;
import leaf.prod.app.utils.PermissionUtils;
import leaf.prod.walletsdk.model.LoginUser;
import leaf.prod.walletsdk.model.UserConfig;
import leaf.prod.walletsdk.model.response.AppResponseWrapper;
import leaf.prod.walletsdk.util.CurrencyUtil;
import leaf.prod.walletsdk.util.LanguageUtil;
import leaf.prod.walletsdk.util.SPUtils;
import leaf.prod.walletsdk.util.ThirdLoginUtil;
import leaf.prod.walletsdk.util.WalletUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                    String uid = map.get("openid");
                    UserConfig userConfig = UserConfig.builder().userId(uid)
                            .language(LanguageUtil.getSettingLanguage(ThirdLoginActivity.this).getText())
                            .currency(CurrencyUtil.getCurrency(ThirdLoginActivity.this).name())
                            .walletList(null).build();
                    ThirdLoginUtil.initThirdLogin(ThirdLoginActivity.this, userConfig, new Callback<AppResponseWrapper<LoginUser>>() {
                        @Override
                        public void onResponse(Call<AppResponseWrapper<LoginUser>> call, Response<AppResponseWrapper<LoginUser>> response) {
                            UserConfig remoteUserConfig = null;
                            LoginUser newLoginUser = LoginUser.builder()
                                    .accountToken(uid)
                                    .config(new Gson().toJson(userConfig))
                                    .build();
                            UserConfig localLoginUserConfig = ThirdLoginUtil.getLocalUser(ThirdLoginActivity.this);
                            try {
                                LoginUser remoteLoginUser = response.body().getMessage();
                                remoteUserConfig = remoteLoginUser != null ? remoteLoginUser.getUserConfig() : null;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (userConfig == null) {
                                if (remoteUserConfig != null) {
                                    // 初始化本地数据
                                    SPUtils.put(ThirdLoginActivity.this, "third_login_" + uid, remoteUserConfig);
                                    ThirdLoginUtil.initLocalConf(ThirdLoginActivity.this);
                                    RxToast.success(getResources().getString(R.string.third_login_success));
                                    forward();
                                } else {
                                    // 初始化本地和线上
                                    ThirdLoginUtil.initLocalAndRemote(ThirdLoginActivity.this, newLoginUser, new Callback<AppResponseWrapper<String>>() {
                                        @Override
                                        public void onResponse(Call<AppResponseWrapper<String>> call, Response<AppResponseWrapper<String>> response) {
                                            RxToast.success(getResources().getString(R.string.third_login_success));
                                            forward();
                                        }

                                        @Override
                                        public void onFailure(Call<AppResponseWrapper<String>> call, Throwable t) {
                                            RxToast.error(getResources().getString(R.string.third_login_error));
                                            ThirdLoginUtil.clearLocal(ThirdLoginActivity.this, uid);
                                        }
                                    });
                                }
                            } else {
                                // 更新线上数据
                                ThirdLoginUtil.initRemote(ThirdLoginActivity.this, localLoginUserConfig, new Callback<AppResponseWrapper<String>>() {
                                    @Override
                                    public void onResponse(Call<AppResponseWrapper<String>> call, Response<AppResponseWrapper<String>> response) {
                                        RxToast.success(getResources().getString(R.string.third_login_success));
                                        forward();
                                    }

                                    @Override
                                    public void onFailure(Call<AppResponseWrapper<String>> call, Throwable t) {
                                        RxToast.error(getResources().getString(R.string.third_login_error));
                                        ThirdLoginUtil.clearLocal(ThirdLoginActivity.this, uid);
                                    }
                                });
                            }
                        }

                        @Override
                        public void onFailure(Call<AppResponseWrapper<LoginUser>> call, Throwable t) {
                            RxToast.error(getResources().getString(R.string.third_login_error));
                            ThirdLoginUtil.clearLocal(ThirdLoginActivity.this, uid);
                        }
                    });
                }

                @Override
                public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                    RxToast.error(getResources().getString(R.string.authorize_failed));
                }

                @Override
                public void onCancel(SHARE_MEDIA share_media, int i) {
                }
            });
        });
        skipLogin.setOnClickListener(view -> {
            ThirdLoginUtil.skip(ThirdLoginActivity.this);
            forward();
        });
    }

    @Override
    public void initData() {
    }

    protected void onCreate(Bundle bundle) {
        setContentView(R.layout.activity_third_login);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        super.onCreate(bundle);
        mSwipeBackLayout.setEnableGesture(false);
        PermissionUtils.initPermissions(this);
    }

    private void forward() {
        if (WalletUtil.hasWallet(ThirdLoginActivity.this)) {
            AppManager.getAppManager().finishAllActivity();
            getOperation().forward(MainActivity.class);
        } else {
            getOperation().forward(CoverActivity.class);
        }
    }
}
