package leaf.prod.walletsdk.manager;

import android.annotation.SuppressLint;
import android.content.Context;

import leaf.prod.walletsdk.model.response.crawler.News;
import leaf.prod.walletsdk.model.response.crawler.NewsPageWrapper;
import leaf.prod.walletsdk.service.CrawlerService;
import leaf.prod.walletsdk.util.LanguageUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 上午11:33
 * Cooperation: Loopring
 */
public class NewsDataManager {

    private static NewsDataManager newsDataManager;

    private static CrawlerService crawlerService = new CrawlerService();

    private Context context;

    private NewsPageWrapper newsPageWrapper;

    public static NewsDataManager getInstance(Context context) {
        if (newsDataManager == null) {
            newsDataManager = new NewsDataManager(context);
        }
        return newsDataManager;
    }

    @SuppressLint("HardwareIds")
    private NewsDataManager(Context context) {
        this.context = context;
    }

    public NewsPageWrapper getNews() {
        return newsPageWrapper;
    }

    public void setNews(NewsPageWrapper newsPageWrapper) {
        this.newsPageWrapper = newsPageWrapper;
    }

    public News getPreNews(News news) {
        int index = newsPageWrapper.getData().indexOf(news);
        if (index == 0)
            return null;
        return newsPageWrapper.getData().get(index - 1);
    }

    public News getNextNews(News news) {
        int index = newsPageWrapper.getData().indexOf(news);
        if (index < newsPageWrapper.getData().size() - 1) {
            if (index == newsPageWrapper.getData().size() - 2) {
                crawlerService.getInformation(CrawlerService.ALL, LanguageUtil.getSettingLanguage(context), newsPageWrapper
                        .getPageIndex() + 1, 10)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<NewsPageWrapper>() {
                    @Override
                    public void onCompleted() {
                        unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        unsubscribe();
                    }

                    @Override
                    public void onNext(NewsPageWrapper newsPageWrapper1) {
                        newsPageWrapper = newsPageWrapper1;
                        unsubscribe();
                    }
                });
            }
            return newsPageWrapper.getData().get(index + 1);
        }
        return null;
    }
}
