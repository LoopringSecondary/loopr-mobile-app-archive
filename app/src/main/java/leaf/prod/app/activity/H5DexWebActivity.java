/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-10-15 1:10 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.layout.WebLayout;
import leaf.prod.app.presenter.H5DexPresenter;
import leaf.prod.app.utils.ToastUtils;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.model.H5ScanType;

public class H5DexWebActivity extends BaseActivity {

    public String password;

    @BindView(R.id.title)
    TitleView title;

    /**
     * 输入密码dialog
     */
    private AlertDialog passwordDialog;

    private AgentWeb mAgentWeb;

    private H5DexPresenter presenter;

    /**
     * 初始化P层
     */
    @Override
    protected void initPresenter() {
        this.presenter = new H5DexPresenter(this, this);
    }

    /**
     * 初始化标题
     */
    @Override
    public void initTitle() {
        title.setBTitle(getString(R.string.h5_dex));
        title.clickLeftGoBack(getWContext());
    }

    /**
     * 初始化视图
     */
    @Override
    public void initView() {
        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.ll_web_view);
        AgentWeb.PreAgentWeb preAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(linearLayout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .defaultProgressBarColor()
                .setReceivedTitleCallback((view, title) -> {
                })
                .setWebChromeClient(new WebChromeClient())
                .setWebViewClient(new WebViewClient())
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.strict)
                .setWebLayout(new WebLayout(this))
                .openParallelDownload() //打开并行下载 , 默认串行下载
                .setNotifyIcon(R.mipmap.download) //下载图标
                .setOpenOtherAppWays(DefaultWebClient.OpenOtherAppWays.DISALLOW) //打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownScheme() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .ready();
        mAgentWeb = preAgentWeb.go("https://h5dex.loopr.io/#/auth/tpwallet");
        mAgentWeb.getJsInterfaceHolder().addJavaObject("android", presenter);
    }

    /**
     * 0
     * 初始化数据
     */
    @Override
    public void initData() {
        String p2pOrder = getIntent().getStringExtra("p2p_order");
        if (p2pOrder != null && !p2pOrder.isEmpty()) {
            presenter.type = H5ScanType.P2P_ORDER;
            presenter.scanContent = p2pOrder;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_h5_dex);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
    }

    public void call(String string) {
        Log.d("CALL_BACK", "Javascript string = " + string);
        mAgentWeb.getLoader().loadUrl(string);
    }

    public void showPasswordDialog() {
        if (passwordDialog == null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_put_password, null);
            builder.setView(view);
            final EditText passwordInput = view.findViewById(R.id.password_input);
            view.findViewById(R.id.cancel).setOnClickListener(v -> passwordDialog.dismiss());
            view.findViewById(R.id.confirm).setOnClickListener(v -> {
                if (TextUtils.isEmpty(passwordInput.getText().toString())) {
                    ToastUtils.toast("请输入Keystore密码");
                } else {
                    presenter.sign(passwordInput.getText().toString());
                }
            });
            builder.setCancelable(true);
            passwordDialog = null;
            passwordDialog = builder.create();
            passwordDialog.setCancelable(true);
            passwordDialog.setCanceledOnTouchOutside(true);
        }
        passwordDialog.show();
    }

    public void hidePasswordDialog() {
        if (passwordDialog != null) {
            passwordDialog.dismiss();
        }
    }
}
