package leaf.prod.app.presenter.infomation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;

import com.ramotion.garlandview.TailLayoutManager;
import com.ramotion.garlandview.TailSnapHelper;
import com.ramotion.garlandview.header.HeaderTransformer;

import leaf.prod.app.R;
import leaf.prod.app.adapter.infomation.NewsHeaderAdapter;
import leaf.prod.app.fragment.news.NewsFragment;
import leaf.prod.app.presenter.BasePresenter;
import leaf.prod.walletsdk.model.NewsHeader;
import leaf.prod.walletsdk.model.response.crawler.BlogWrapper;
import leaf.prod.walletsdk.model.response.crawler.NewsPageWrapper;
import leaf.prod.walletsdk.service.CrawlerService;
import leaf.prod.walletsdk.util.LanguageUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-12-27 11:36 AM
 * Cooperation: loopring.org 路印协议基金会
 */
public class NewsPresenter extends BasePresenter<NewsFragment> {

    private static int PAGE_SIZE = 10;

    private CrawlerService crawlerService;

    public NewsPresenter(NewsFragment view, Context context) {
        super(view, context);
        crawlerService = new CrawlerService();
    }

    public void initData() {
        view.clLoading.setVisibility(View.VISIBLE);
        List<NewsHeader> newsHeaderList = new ArrayList<>();
        final BlogWrapper[] blogWrappers = {BlogWrapper.emptyBean()};
        crawlerService.getBlogs().flatMap((Func1<BlogWrapper, Observable<NewsPageWrapper>>) b -> {
            blogWrappers[0] = b;
            return crawlerService.getFlash(CrawlerService.ALL, LanguageUtil.getSettingLanguage(context), 0, PAGE_SIZE);
        }).flatMap((Func1<NewsPageWrapper, Observable<NewsPageWrapper>>) newsPageWrapper -> {
            newsHeaderList.add(NewsHeader.builder()
                    .newsType(NewsHeader.NewsType.NEWS_FLASH)
                    .title(view.getResources().getString(R.string.news_flash))
                    .description(view.getResources().getString(R.string.news_flash))
                    .newsList(newsPageWrapper)
                    .build());
            return crawlerService.getInformation(CrawlerService.ALL, LanguageUtil.getSettingLanguage(context), 0, PAGE_SIZE);
        })
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
                    }

                    @Override
                    public void onNext(NewsPageWrapper newsPageWrapper) {
                        newsHeaderList.add(NewsHeader.builder()
                                .newsType(NewsHeader.NewsType.NEWS_INFO)
                                .title(view.getResources().getString(R.string.news_information))
                                .description(view.getResources().getString(R.string.news_information))
                                .newsList(newsPageWrapper)
                                .build());
                        ((TailLayoutManager) view.recyclerView.getLayoutManager()).setPageTransformer(new HeaderTransformer());
                        view.recyclerView.setAdapter(new NewsHeaderAdapter(blogWrappers[0], newsHeaderList, view.getActivity()));
                        new TailSnapHelper().attachToRecyclerView(view.recyclerView);
                        view.clLoading.setVisibility(View.GONE);
                        unsubscribe();
                    }
                });
    }
}
