package com.tomcat360.lyqb.presenter;

import java.util.List;

import com.lyqb.walletsdk.model.response.data.Token;
import com.lyqb.walletsdk.service.LoopringService;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BasePresenter<V> {

    private V mView;

    private List<Token> tokens;

    private LoopringService loopringService = new LoopringService();

    public void attachView(V view) {
        this.mView = view;
    }

    public void detachView() {
        this.mView = null;
    }

    public boolean isViewAttached() {
        return mView != null;
    }

    public V getView() {
        return mView;
    }

    private void updateTokens() {
        loopringService.getSupportedToken()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(tokens -> this.tokens = tokens);
        // TODO token 逻辑
    }
}
