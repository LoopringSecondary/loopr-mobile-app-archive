package leaf.prod.app.fragment.news;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ramotion.garlandview.TailLayoutManager;
import com.ramotion.garlandview.TailRecyclerView;
import com.ramotion.garlandview.TailSnapHelper;
import com.ramotion.garlandview.header.HeaderTransformer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import leaf.prod.app.R;
import leaf.prod.app.adapter.infomation.NewsHeaderAdapter;
import leaf.prod.app.fragment.BaseFragment;
import leaf.prod.walletsdk.model.NewsHeader;
import leaf.prod.walletsdk.model.response.crawler.NewsPageWrapper;
import leaf.prod.walletsdk.service.CrawlerService;
import leaf.prod.walletsdk.util.LanguageUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class NewsFragment extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.cl_loading)
    public ConstraintLayout clLoading;

    @BindView(R.id.recycler_view)
    public TailRecyclerView recyclerView;

    private CrawlerService crawlerService;

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
        crawlerService = new CrawlerService();
        crawlerService.getFlash(CrawlerService.ALL, LanguageUtil.getSettingLanguage(getContext()), 1, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NewsPageWrapper>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(NewsPageWrapper newsPageWrapper) {
                        List<NewsHeader> newsHeaderList = new ArrayList<>();
                        newsHeaderList.add(NewsHeader.builder()
                                .title("快讯")
                                .description("快讯早知道")
                                .newsList(newsPageWrapper != null ? newsPageWrapper : NewsPageWrapper.emptyBean())
                                .build());
                        newsHeaderList.add(NewsHeader.builder()
                                .title("动态")
                                .description("动态早知道")
                                .newsList(newsPageWrapper != null ? newsPageWrapper : NewsPageWrapper.emptyBean())
                                .build());
                        ((TailLayoutManager) recyclerView.getLayoutManager()).setPageTransformer(new HeaderTransformer());
                        recyclerView.setAdapter(new NewsHeaderAdapter(newsHeaderList));
                        new TailSnapHelper().attachToRecyclerView(recyclerView);
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
    //
    //    private List<NewsHeader> gen() {
    //        List<NewsHeader> lists = new ArrayList<>();
    //        List<NewsBody> list = new ArrayList<>();
    //        for (int i = 0; i < 20; ++i) {
    //            list.add(NewsBody.builder()
    //                    .title("行情|数字币早班车")
    //                    .content("walle-web.io 大概是最为瞩目的免费开源的上线部署平台的新星，让用户代码发布终于可以不只能选择 jenkins，支持各种web代码发布")
    //                    .bearCount(10)
    //                    .bullCount(20)
    //                    .shareCount(2)
    //                    .date(new Date())
    //                    .build());
    //        }
    //        lists.add(NewsHeader.builder().title("快讯").description("快讯早知道").newsList(list).build());
    //        lists.add(NewsHeader.builder().title("动态").description("动态早知道").newsList(list).build());
    //        return lists;
    //    }
}
