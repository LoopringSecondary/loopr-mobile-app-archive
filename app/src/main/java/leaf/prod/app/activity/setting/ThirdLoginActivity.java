package leaf.prod.app.activity.setting;

import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.activity.BaseActivity;
import leaf.prod.app.activity.setupWallet.CoverActivity;
import leaf.prod.app.activity.wallet.MainActivity;
import leaf.prod.app.utils.AppManager;
import leaf.prod.app.utils.PermissionUtils;
import leaf.prod.walletsdk.manager.LoginDataManager;
import leaf.prod.walletsdk.model.LoginUser;
import leaf.prod.walletsdk.model.response.AppResponseWrapper;
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
                    loginDataManager.getRemoteUser(uid, new Callback<AppResponseWrapper<LoginUser>>() {
                        @Override
                        public void onResponse(Call<AppResponseWrapper<LoginUser>> call, Response<AppResponseWrapper<LoginUser>> response) {
                            loginDataManager.loginSuccess(uid);
                            AppResponseWrapper<LoginUser> responseWrapper = response.body();
                            if (responseWrapper.getSuccess() && responseWrapper.getMessage() != null) {
                                loginDataManager.updateLocal(responseWrapper.getMessage());
                            } else {
                                loginDataManager.updateRemote(loginDataManager.getLocalUser());
                            }
                            RxToast.success(getString(R.string.third_login_success));
                            forward();
                        }

                        @Override
                        public void onFailure(Call<AppResponseWrapper<LoginUser>> call, Throwable t) {
                            RxToast.error(getResources().getString(R.string.third_login_error));
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
            loginDataManager.skip();
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
        loginDataManager = LoginDataManager.getInstance(this);
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
