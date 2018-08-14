package com.tomcat360.lyqb.presenter;


import com.tomcat360.lyqb.view.IBaseActivityView;

/**
 * Title:Presenter
 */

public interface Presenter<V extends IBaseActivityView> {
	void attachView(V view);

	void detachView();
}
