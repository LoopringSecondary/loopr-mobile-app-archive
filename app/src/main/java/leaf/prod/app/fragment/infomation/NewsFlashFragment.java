package leaf.prod.app.fragment.infomation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import leaf.prod.app.R;
import leaf.prod.app.adapter.infomation.NewsFlashAdapter;
import leaf.prod.app.fragment.BaseFragment;
import leaf.prod.app.utils.FontUtil;
import leaf.prod.walletsdk.model.response.crawler.NewsPageWrapper;
import leaf.prod.walletsdk.service.CrawlerService;
import leaf.prod.walletsdk.util.LanguageUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsFlashFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.cl_loading)
    public ConstraintLayout clLoading;

    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

    @BindView(R.id.refresh_layout)
    public SmartRefreshLayout refreshLayout;

    @BindView(R.id.tv_title)
    public TextView tvTitle;

    private NewsFlashAdapter newsFlashAdapter;

    private static CrawlerService crawlerService;

    private int pageIndex = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_news_details, container, false);
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
        tvTitle.setText(getString(R.string.news_flash));
        tvTitle.setTypeface(FontUtil.getTypeface(getContext(), 11));
    }

    @Override
    protected void initData() {
        clLoading.setVisibility(View.VISIBLE);
        if (crawlerService == null) {
            crawlerService = new CrawlerService();
        }
        newsFlashAdapter = new NewsFlashAdapter(R.layout.news_flash_item, null, getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(newsFlashAdapter);
        getFlash(pageIndex = 0);
        refreshLayout.setOnRefreshListener(refreshLayout -> getFlash(pageIndex = 0));
        refreshLayout.setOnLoadMoreListener(refreshLayout -> getFlash(++pageIndex));
    }

    private void getFlash(int page) {
        crawlerService.getFlash(CrawlerService.ALL, LanguageUtil.getSettingLanguage(getContext()), page, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NewsPageWrapper>() {
                    @Override
                    public void onCompleted() {
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        unsubscribe();
                        if (page == 0) {
                            refreshLayout.finishRefresh(false);
                        } else {
                            refreshLayout.finishLoadMore(false);
                        }
                    }

                    @Override
                    public void onNext(NewsPageWrapper newsPageWrapper) {
                        if (page == 0) {
                            newsFlashAdapter.setNewData(newsPageWrapper.getData());
                            refreshLayout.finishRefresh(true);
                        } else {
                            newsFlashAdapter.addData(newsPageWrapper.getData());
                            refreshLayout.finishLoadMore(0, true, newsFlashAdapter.getData()
                                    .size() == newsPageWrapper.getTotal());
                        }
                        clLoading.setVisibility(View.GONE);
                        unsubscribe();
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
