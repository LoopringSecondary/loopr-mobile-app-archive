package leaf.prod.app.fragment.infomation;

import java.util.List;

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
import leaf.prod.app.activity.infomation.NewsInfoActivity;
import leaf.prod.app.adapter.NoDataAdapter;
import leaf.prod.app.adapter.infomation.NewsInfoAdapter;
import leaf.prod.app.fragment.BaseFragment;
import leaf.prod.app.utils.FontUtil;
import leaf.prod.walletsdk.manager.NewsDataManager;
import leaf.prod.walletsdk.model.Language;
import leaf.prod.walletsdk.model.NoDataType;
import leaf.prod.walletsdk.model.response.crawler.News;
import leaf.prod.walletsdk.model.response.crawler.NewsPageWrapper;
import leaf.prod.walletsdk.service.CrawlerService;
import leaf.prod.walletsdk.util.LanguageUtil;
import leaf.prod.walletsdk.util.StringUtils;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsInfoFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.cl_loading)
    public ConstraintLayout clLoading;

    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

    @BindView(R.id.refresh_layout)
    public SmartRefreshLayout refreshLayout;

    @BindView(R.id.tv_title)
    public TextView tvTitle;

    private NoDataAdapter emptyAdapter;

    private NewsInfoAdapter newsInfoAdapter;

    private static CrawlerService crawlerService;

    private List<News> newsList;

    private NewsDataManager newsDataManager;

    private int pageIndex = 0;

    private String symbol;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 布局导入
        layout = inflater.inflate(R.layout.fragment_news_details, container, false);
        unbinder = ButterKnife.bind(this, layout);
        symbol = getArguments() != null ? getArguments().getString("symbol") : "";
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
        tvTitle.setText(getString(R.string.news_information));
        tvTitle.setTypeface(FontUtil.getTypeface(getContext(), 11));
    }

    @Override
    protected void initData() {
        clLoading.setVisibility(View.VISIBLE);
        if (crawlerService == null) {
            crawlerService = new CrawlerService();
        }
        newsDataManager = NewsDataManager.getInstance(getContext());
        newsInfoAdapter = new NewsInfoAdapter(R.layout.news_info_item, null, getActivity());
        emptyAdapter = new NoDataAdapter(R.layout.adapter_item_no_data, null, NoDataType.asset);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(newsInfoAdapter);
        getInformation(pageIndex = 0);
        refreshLayout.setOnRefreshListener(refreshLayout -> getInformation(pageIndex = 0));
        refreshLayout.setOnLoadMoreListener(refreshLayout -> getInformation(++pageIndex));
        newsInfoAdapter.setOnItemClickListener((adapter, view, position) -> {
            getOperation().addParameter("data", newsList.get(position));
            getOperation().forward(NewsInfoActivity.class);
        });
    }

    private void getInformation(int page) {
        Language language = LanguageUtil.getSettingLanguage(getContext());
        crawlerService.getInformation(StringUtils.isEmpty(symbol) ? CrawlerService.ALL : symbol, language == Language.en_US ? Language.en_US : Language.zh_CN, page, 10)
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
                            newsInfoAdapter.setNewData(newsPageWrapper.getData());
                            refreshLayout.finishRefresh(true);
                        } else {
                            newsInfoAdapter.addData(newsPageWrapper.getData());
                            newsPageWrapper.setData(newsInfoAdapter.getData());
                            refreshLayout.finishLoadMore(0, true, newsInfoAdapter.getData()
                                    .size() == newsPageWrapper.getTotal());
                        }
                        newsDataManager.setNews(newsPageWrapper);
                        newsList = newsInfoAdapter.getData();
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
