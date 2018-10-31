package leaf.prod.app.activity;

import java.util.Map;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.model.ThirdLoginUser;
import leaf.prod.app.utils.ThirdUserUtil;
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
        hintText.setText("第三方授权登录，系统可以保存您的APP配置，\n防止删除或者更新APP后丢失。您也可以选择跳过。");
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
                    // todo 第三方登录信息存入数据库
                    ThirdUserUtil.initThirdLogin(ThirdLoginActivity.this, new ThirdLoginUser(map.get("uid"), new Gson().toJson(map), null));
                    getOperation().forward(MainActivity.class);
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
            getOperation().forward(MainActivity.class);
            finish();
        });
    }

    @Override
    public void initData() {
        thirdLoginService = new ThirdLoginService();
    }

    protected void onCreate(Bundle bundle) {
        setContentView(R.layout.activity_third_login);
        ButterKnife.bind(this);
        super.onCreate(bundle);
        mSwipeBackLayout.setEnableGesture(false);
    }
}
