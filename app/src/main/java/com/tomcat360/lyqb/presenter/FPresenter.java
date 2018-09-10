package com.tomcat360.lyqb.presenter;

import com.tomcat360.lyqb.view.IBaseFragmentView;

/**
 * Title:Presenter
 */

public interface FPresenter<V extends IBaseFragmentView> {

    void attachView(V view);

    void detachView();
}
