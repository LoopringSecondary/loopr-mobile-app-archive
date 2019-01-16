package leaf.prod.app.fragment.wallet;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import leaf.prod.app.R;
import leaf.prod.app.activity.wallet.MainActivity;
import leaf.prod.app.adapter.ViewPageAdapter;
import leaf.prod.app.fragment.BaseFragment;
import leaf.prod.app.fragment.infomation.NewsFragment;
import leaf.prod.app.layout.MyVerticalViewPager;

public class MainFragment extends BaseFragment {

    public final static int BALANCE_SUCCESS = 1;

    private static int REQUEST_CODE = 1;  //二维码扫一扫code

    @BindView(R.id.vp_main)
    MyVerticalViewPager viewPager;

    Unbinder unbinder;

    @SuppressLint("HandlerLeak")
    Handler handlerBalance = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case BALANCE_SUCCESS:
                default:
                    break;
            }
        }
    };

    private List<Fragment> fragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, layout);
        EventBus.getDefault().register(this);
        return layout;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    protected void initView() {
        fragments.add(new NewsFragment());
        fragments.add(new MainWalletFragment());
        viewPager.setAdapter(new ViewPageAdapter(getActivity().getSupportFragmentManager(), fragments, new String[]{"新闻", "钱包"}));
        viewPager.setCurrentItem(1);
    }

    @Override
    protected void initData() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setItem(Event event) {
        viewPager.setCurrentItem(event.getIndex(), true);
        ((MainActivity) getActivity()).showBottomBar(event.getIndex() != 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    public static class Event {

        private int index;

        public Event() {
        }

        public Event(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
