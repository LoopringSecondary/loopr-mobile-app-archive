/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-10-15 1:10 PM
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.activity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.layout.WebLayout;
import leaf.prod.app.manager.DexDataManager;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.listener.CallbackListener;

public class H5DexWebActivity extends BaseActivity implements CallbackListener {

    private AgentWeb mAgentWeb;

    @BindView(R.id.title)
    TitleView title;

    /**
     * 初始化P层
     */
    @Override
    protected void initPresenter() {
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
    }

    /**
     * 0
     * 初始化数据
     */
    @Override
    public void initData() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_h5_dex);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        LinearLayout linearLayout = (LinearLayout) this.findViewById(R.id.ll_web_view);
        AgentWeb.PreAgentWeb preAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent(linearLayout, new LinearLayout.LayoutParams(-1, -1))//
                .useDefaultIndicator()//
                .defaultProgressBarColor()
                .setReceivedTitleCallback((view, title) -> {})
                .setWebChromeClient(new WebChromeClient())
                .setWebViewClient(new WebViewClient())
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.strict)
                .setWebLayout(new WebLayout(this))
                .openParallelDownload()//打开并行下载 , 默认串行下载
                .setNotifyIcon(R.mipmap.download) //下载图标
                .setOpenOtherAppWays(DefaultWebClient.OpenOtherAppWays.DISALLOW)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownScheme() //拦截找不到相关页面的Scheme
                .createAgentWeb()//
                .ready();
        mAgentWeb = preAgentWeb.go("https://h5dex.loopr.io/#/auth/tpwallet");
        mAgentWeb.getJsInterfaceHolder().addJavaObject("android", DexDataManager.getInstance(this).setCallbackListener(this));
    }

    @Override
    public void callback(String string) {
        Log.d("CALL_BACK", "Javascript string = " + string);
        mAgentWeb.getLoader().loadUrl(string);
    }
}
