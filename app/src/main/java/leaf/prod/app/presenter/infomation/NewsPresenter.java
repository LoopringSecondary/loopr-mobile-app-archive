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
import leaf.prod.walletsdk.model.response.crawler.NewsPageWrapper;
import leaf.prod.walletsdk.service.CrawlerService;
import leaf.prod.walletsdk.util.LanguageUtil;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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
        Observable.zip(crawlerService.getFlash(CrawlerService.ALL, LanguageUtil.getSettingLanguage(context), 0, PAGE_SIZE),
                crawlerService.getInformation(CrawlerService.ALL, LanguageUtil.getSettingLanguage(context), 0, PAGE_SIZE),
                CombineObservable::new)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CombineObservable>() {
                    @Override
                    public void onCompleted() {
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        unsubscribe();
                    }

                    @Override
                    public void onNext(CombineObservable combineObservable) {
                        List<NewsHeader> newsHeaderList = new ArrayList<>();
                        newsHeaderList.add(NewsHeader.builder()
                                .newsType(NewsHeader.NewsType.NEWS_FLASH)
                                .title(view.getResources().getString(R.string.news_flash))
                                .description(view.getResources().getString(R.string.news_flash))
                                .newsList(combineObservable.getFlashNews() != null ? combineObservable.getFlashNews() : NewsPageWrapper
                                        .emptyBean())
                                .build());
                        newsHeaderList.add(NewsHeader.builder()
                                .newsType(NewsHeader.NewsType.NEWS_INFO)
                                .title(view.getResources().getString(R.string.news_information))
                                .description(view.getResources().getString(R.string.news_information))
                                .newsList(combineObservable.getInfoNews() != null ? combineObservable.getInfoNews() : NewsPageWrapper
                                        .emptyBean())
                                .build());
                        ((TailLayoutManager) view.recyclerView.getLayoutManager()).setPageTransformer(new HeaderTransformer());
                        view.recyclerView.setAdapter(new NewsHeaderAdapter(newsHeaderList));
                        new TailSnapHelper().attachToRecyclerView(view.recyclerView);
                        view.clLoading.setVisibility(View.GONE);
                        unsubscribe();
                    }
                });
    }

    public void setInformation(int pageIndex) {
        crawlerService.getInformation(CrawlerService.ALL, LanguageUtil.getSettingLanguage(context), pageIndex, 10)
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
                    }
                });
    }

    public void setFlash(int pageIndex) {
        crawlerService.getFlash(CrawlerService.ALL, LanguageUtil.getSettingLanguage(context), pageIndex, PAGE_SIZE)
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
                        ((TailLayoutManager) view.recyclerView.getLayoutManager()).setPageTransformer(new HeaderTransformer());
                        view.recyclerView.setAdapter(new NewsHeaderAdapter(newsHeaderList));
                        new TailSnapHelper().attachToRecyclerView(view.recyclerView);
                        view.clLoading.setVisibility(View.GONE);
                    }
                });
    }

    private class CombineObservable {

        private NewsPageWrapper flashNews;

        private NewsPageWrapper infoNews;

        public CombineObservable() {
        }

        public CombineObservable(NewsPageWrapper flashNews, NewsPageWrapper infoNews) {
            this.flashNews = flashNews;
            this.infoNews = infoNews;
        }

        public NewsPageWrapper getFlashNews() {
            return flashNews;
        }

        public void setFlashNews(NewsPageWrapper flashNews) {
            this.flashNews = flashNews;
        }

        public NewsPageWrapper getInfoNews() {
            return infoNews;
        }

        public void setInfoNews(NewsPageWrapper infoNews) {
            this.infoNews = infoNews;
        }
    }
}
