/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 下午5:54
 * Cooperation: Loopring
 */
package leaf.prod.walletsdk.service;

import leaf.prod.walletsdk.SDK;
import leaf.prod.walletsdk.deligate.RpcDelegate;
import leaf.prod.walletsdk.model.Language;
import leaf.prod.walletsdk.model.NewsCategory;
import leaf.prod.walletsdk.model.request.RequestWrapper;
import leaf.prod.walletsdk.model.request.crawlerParam.NewsParam;
import leaf.prod.walletsdk.model.response.RelayResponseWrapper;
import leaf.prod.walletsdk.model.response.crawler.NewsPageWrapper;
import rx.Observable;

public class CrawlerService {

    private RpcDelegate rpcDelegate;

    public CrawlerService() {
        String url = SDK.relayBase();
        rpcDelegate = RpcDelegate.getService(url);
    }

    public Observable<NewsPageWrapper> getInformation(String token, Language language, int pageIndex, int pageSize) {
        NewsParam param = NewsParam.builder()
                .token(token)
                .language(language.getText())
                .category(NewsCategory.information.name())
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        RequestWrapper request = new RequestWrapper("queryNews", param);
        Observable<RelayResponseWrapper<NewsPageWrapper>> observable = rpcDelegate.getNews(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<NewsPageWrapper> getFlash(String token, Language language, int pageIndex, int pageSize) {
        NewsParam param = NewsParam.builder()
                .token(token)
                .language(language.getText())
                .category(NewsCategory.flash.name())
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .build();
        RequestWrapper request = new RequestWrapper("queryNews", param);
        Observable<RelayResponseWrapper<NewsPageWrapper>> observable = rpcDelegate.getNews(request);
        return observable.map(RelayResponseWrapper::getResult);
    }
}
