/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-20 下午5:47
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.views.TitleView;

public class DefaultWebViewActivity extends BaseActivity {

    @BindView(R.id.title)
    TitleView title;

    @BindView(R.id.browser_btn)
    ImageView browser;

    @BindView(R.id.webView)
    WebView webView;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_defalut_webview);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        url = getIntent().getStringExtra("url");
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url == null)
                    return false;
                try {
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
                        return true;
                    }
                } catch (Exception e) {
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });
    }

    @OnClick({R.id.browser_btn})
    public void onViewClicked(View view) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

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
        title.setBTitle(getResources().getString(R.string.etherscan));
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
}
