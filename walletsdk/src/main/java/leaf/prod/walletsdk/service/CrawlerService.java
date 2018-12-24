/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-10-30 下午5:54
 * Cooperation: Loopring
 */
package leaf.prod.walletsdk.service;

import leaf.prod.walletsdk.SDK;
import leaf.prod.walletsdk.deligate.RpcDelegate;
import leaf.prod.walletsdk.model.IndexAction;
import leaf.prod.walletsdk.model.IndexType;
import leaf.prod.walletsdk.model.Language;
import leaf.prod.walletsdk.model.NewsCategory;
import leaf.prod.walletsdk.model.request.RequestWrapper;
import leaf.prod.walletsdk.model.request.crawlerParam.IndexParam;
import leaf.prod.walletsdk.model.request.crawlerParam.NewsParam;
import leaf.prod.walletsdk.model.response.RelayResponseWrapper;
import leaf.prod.walletsdk.model.response.crawler.IndexResult;
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

    public Observable<IndexResult> confirmBull(String uuid) {
        IndexParam param = IndexParam.builder()
                .uuid(uuid)
                .indexName(IndexType.BULLINDEX.getDescription())
                .direction(IndexAction.CONFIRM.getValue())
                .build();
        RequestWrapper request = new RequestWrapper("updateIndex", param);
        Observable<RelayResponseWrapper<IndexResult>> observable = rpcDelegate.updateIndex(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<IndexResult> cancelBull(String uuid) {
        IndexParam param = IndexParam.builder()
                .uuid(uuid)
                .indexName(IndexType.BULLINDEX.getDescription())
                .direction(IndexAction.CANCEL.getValue())
                .build();
        RequestWrapper request = new RequestWrapper("updateIndex", param);
        Observable<RelayResponseWrapper<IndexResult>> observable = rpcDelegate.updateIndex(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<IndexResult> confirmBear(String uuid) {
        IndexParam param = IndexParam.builder()
                .uuid(uuid)
                .indexName(IndexType.BEARINDEX.getDescription())
                .direction(IndexAction.CONFIRM.getValue())
                .build();
        RequestWrapper request = new RequestWrapper("updateIndex", param);
        Observable<RelayResponseWrapper<IndexResult>> observable = rpcDelegate.updateIndex(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<IndexResult> cancelBear(String uuid) {
        IndexParam param = IndexParam.builder()
                .uuid(uuid)
                .indexName(IndexType.BEARINDEX.getDescription())
                .direction(IndexAction.CANCEL.getValue())
                .build();
        RequestWrapper request = new RequestWrapper("updateIndex", param);
        Observable<RelayResponseWrapper<IndexResult>> observable = rpcDelegate.updateIndex(request);
        return observable.map(RelayResponseWrapper::getResult);
    }

    public Observable<IndexResult> confirmForward(String uuid) {
        IndexParam param = IndexParam.builder()
                .uuid(uuid)
                .indexName(IndexType.FORWARDNUM.getDescription())
                .direction(IndexAction.CONFIRM.getValue())
                .build();
        RequestWrapper request = new RequestWrapper("updateIndex", param);
        Observable<RelayResponseWrapper<IndexResult>> observable = rpcDelegate.updateIndex(request);
        return observable.map(RelayResponseWrapper::getResult);
    }
}
