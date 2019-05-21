package leaf.prod.app.activity.wallet;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.app.activity.BaseActivity;
import leaf.prod.app.adapter.ViewPageAdapter;
import leaf.prod.app.fragment.infomation.NewsFragment;
import leaf.prod.app.fragment.wallet.MainFragment;
import leaf.prod.app.layout.MyVerticalViewPager;

public class WalletDetailActivity extends BaseActivity {

    @BindView(R.id.vp_main)
    MyVerticalViewPager viewPager;

    private List<Fragment> fragments = new ArrayList<>();

    private MainActivity.MyTouchListener myTouchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_main);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        mSwipeBackLayout.setEnableGesture(false);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setItem(MainFragment.Event event) {
        viewPager.setCurrentItem(event.getIndex(), true);
    }

    @Override
    public void initView() {
        Bundle bundle = new Bundle();
        bundle.putString("symbol", getIntent().getStringExtra("symbol"));
        NewsFragment newsFragment = new NewsFragment();
        newsFragment.setArguments(bundle);
        fragments.add(newsFragment);
        WalletDetailFragment walletDetailFragment = new WalletDetailFragment();
        walletDetailFragment.setArguments(bundle);
        fragments.add(walletDetailFragment);
        viewPager.setAdapter(new ViewPageAdapter(getSupportFragmentManager(), fragments, new String[]{"新闻"}));
        viewPager.setCurrentItem(1);
    }

    @Override
    public void initData() {
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            if (viewPager.getCurrentItem() == 0) {
                viewPager.setCurrentItem(1);
            } else {
                super.onBackPressed();
            }
        } else {
            getFragmentManager().popBackStack();
        }
    }

    /**
     * 在fragment回调触摸事件获得坐标
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (null != myTouchListener) {
            myTouchListener.onTouch(ev);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void registerMyTouchListener(MainActivity.MyTouchListener myTouchListener) {
        this.myTouchListener = myTouchListener;
    }

    public interface MyTouchListener {

        void onTouch(MotionEvent ev);
    }
}
