package leaf.prod.app.fragment.infomation;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import leaf.prod.app.R;
import leaf.prod.app.adapter.ViewPageAdapter;
import leaf.prod.app.fragment.BaseFragment;
import leaf.prod.app.fragment.wallet.MainFragment;

/**
 *
 */
public class NewsFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.view_pager)
    public ViewPager viewPager;

    @BindView(R.id.right_btn)
    public LinearLayout rightBtn;

    @BindView(R.id.left_scroll)
    public View leftScroll;

    @BindView(R.id.right_scroll)
    public View rightScroll;

    private List<Fragment> fragments = new ArrayList<>();

    private NewsFlashFragment newsFlashFragment;

    private NewsInfoFragment newsInfoFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_news, container, false);
        unbinder = ButterKnife.bind(this, layout);
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
        rightBtn.setOnClickListener(view -> EventBus.getDefault().post(new MainFragment.Event(1)));
        Bundle bundle = getArguments();
        newsFlashFragment = new NewsFlashFragment();
        newsInfoFragment = new NewsInfoFragment();
        if (bundle != null) {
            newsFlashFragment.setArguments(bundle);
            newsInfoFragment.setArguments(bundle);
        }
        fragments.add(newsFlashFragment);
        fragments.add(newsInfoFragment);
        String[] titles = new String[]{getString(R.string.news_flash), getString(R.string.news_information)};
        viewPager.setAdapter(new ViewPageAdapter(getActivity().getSupportFragmentManager(), fragments, titles));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    leftScroll.animate().alpha(0).setDuration(0);
                    rightScroll.animate().alpha(0).setDuration(0);
                } else {
                    if (viewPager.getCurrentItem() == 1) {
                        leftScroll.animate().alpha(1).setDuration(1000);
                    } else if (viewPager.getCurrentItem() == 0) {
                        rightScroll.animate().alpha(1).setDuration(1000);
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
