package leaf.prod.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.fragment.MainFragment;
import leaf.prod.app.fragment.SettingFragment;
import leaf.prod.app.fragment.TradeFragment;
import leaf.prod.app.layout.ChildClickableLinearLayout;
import leaf.prod.app.utils.AppManager;
import leaf.prod.app.utils.UpgradeUtil;
import leaf.prod.walletsdk.util.SPUtils;

/**
 * Created by niedengqiang on 2018/8/13.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.main_frame)
    FrameLayout mainFrame;

    @BindView(R.id.iv_main)
    ImageView ivMain;

    @BindView(R.id.tv_main)
    TextView tvMain;

    @BindView(R.id.rl_main)
    RelativeLayout rlMain;

    @BindView(R.id.iv_trade)
    ImageView ivTrade;

    @BindView(R.id.tv_trade)
    TextView tvTrade;

    @BindView(R.id.rl_trade)
    RelativeLayout rlTrade;

    @BindView(R.id.iv_setting)
    ImageView ivSetting;

    @BindView(R.id.tv_setting)
    TextView tvSetting;

    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;

    @BindView(R.id.ccl_main)
    ChildClickableLinearLayout cclMain;

    private long exitTime = 0;

    private int index;

    private int currentTabIndex;  //当前页

    private ImageView[] imagebuttons; //底部tab-imageview集合

    private TextView[] textviews; //底部tab-textview集合

    private Fragment Fragment1, Fragment2, Fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
        UpgradeUtil.showUpdateHint(this, false);
        cclMain.setChildClickable(false);
    }

    @Override
    public void initView() {
        changeFragment();
        setTabSelect(0);
        if ((Boolean) SPUtils.get(this, "isRecreate", false)) {//判断是否是更改语言设置后，执行了系统的recreate()方法,
            ChangeMainFragment(2);
            SPUtils.put(this, "isRecreate", false);
        }
    }

    @Override
    public void initData() {
        /*
         * 通过language的状态来判断是否设置了显示英文还是中文，1为英文，2为中文,0为未设置，显示系统默认
         */
        //        if (LanguageUtil.getLanguage(this) != LanguageUtil.getSettingLanguage(this)) {
        //            LanguageUtil.changeLanguage(this, LanguageUtil.getSettingLanguage(this));
        //            recreate();
        //        }
    }

    @Override
    public void initTitle() {
    }

    public void setTabSelect(int i) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (i) {
            case 0:
                Fragment1 = manager.findFragmentByTag("TAG1");
                hideTab(transaction);
                if (Fragment1 == null) {
                    Fragment1 = new MainFragment();
                    transaction.add(R.id.main_frame, Fragment1, "TAG1");
                } else {
                    transaction.show(Fragment1);
                }
                break;
            case 1:
                Fragment2 = manager.findFragmentByTag("TAG2");
                hideTab(transaction);
                if (Fragment2 == null) {
                    Fragment2 = new TradeFragment();
                    transaction.add(R.id.main_frame, Fragment2, "TAG2");
                } else {
                    transaction.show(Fragment2);
                }
                break;
            case 2:
                Fragment3 = manager.findFragmentByTag("TAG3");
                hideTab(transaction);
                if (Fragment3 == null) {
                    Fragment3 = new SettingFragment();
                    transaction.add(R.id.main_frame, Fragment3, "TAG3");
                } else {
                    transaction.show(Fragment3);
                }
                break;
        }
        transaction.commitAllowingStateLoss();
    }

    private void hideTab(FragmentTransaction transaction) {
        if (Fragment1 != null) {
            transaction.hide(Fragment1);
        }
        if (Fragment2 != null) {
            transaction.hide(Fragment2);
        }
        if (Fragment3 != null) {
            transaction.hide(Fragment3);
        }
    }

    private void changeFragment() {
        imagebuttons = new ImageView[3];
        imagebuttons[0] = (ImageView) findViewById(R.id.iv_main);
        imagebuttons[1] = (ImageView) findViewById(R.id.iv_trade);
        imagebuttons[2] = (ImageView) findViewById(R.id.iv_setting);
        imagebuttons[0].setSelected(true);
        textviews = new TextView[3];
        textviews[0] = (TextView) findViewById(R.id.tv_main);
        textviews[1] = (TextView) findViewById(R.id.tv_trade);
        textviews[2] = (TextView) findViewById(R.id.tv_setting);
        textviews[0].setTextColor(0xFFFF2741);
    }

    //改变主界面的fragment
    public void ChangeMainFragment(int type) {
        index = type;
        imagebuttons[currentTabIndex].setSelected(false);
        imagebuttons[index].setSelected(true);
        textviews[currentTabIndex].setTextColor(0xFF333333);
        textviews[index].setTextColor(0xFFFF2741);
        currentTabIndex = index;
        setTabSelect(type);
    }

    @OnClick({R.id.rl_main, R.id.rl_trade, R.id.rl_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_main:
                index = 0;
                setTabSelect(0);
                break;
            case R.id.rl_trade:
                index = 1;
                setTabSelect(1);
                break;
            case R.id.rl_setting:
                index = 2;
                setTabSelect(2);
                break;
        }
        imagebuttons[currentTabIndex].setSelected(false);
        imagebuttons[index].setSelected(true);
        textviews[currentTabIndex].setTextColor(0xFF333333);
        textviews[index].setTextColor(0xFFFF2741);
        currentTabIndex = index;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ((Boolean) SPUtils.get(this, "isRecreate", false)) {//判断是否是更改语言设置后，执行了系统的recreate()方法,
            recreate();//判断是否是更改语言设置后，执行了系统的recreate()方法,
        } else {
            if (Fragment1 != null && Fragment1.isVisible()) {
                ((MainFragment) Fragment1).refresh();
            }
        }
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        index = 0;
        setTabSelect(0);
        SPUtils.put(this, "isNewintent", true);
        recreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //        APP.getLoopring().destroy();
    }

    /***
     * 返回键按两下退出
     *
     * @return
     * @throws Exception
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) // System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                RxToast.warning(this, getResources().getString(R.string.click_twice_close), Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void setClickable(boolean clickable) {
        if (cclMain != null) {
            cclMain.setChildClickable(clickable);
        }
    }
}
