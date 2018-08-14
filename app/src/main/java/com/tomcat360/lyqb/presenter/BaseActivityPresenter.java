package com.tomcat360.lyqb.presenter;


import com.tomcat360.lyqb.view.IBaseActivityView;

/**
 * Title:BaseActivityPresenter
 */

public class BaseActivityPresenter <T extends IBaseActivityView> implements Presenter<T> {

	private T mView;

	@Override
	public void attachView(T view) {
		this.mView = view;
	}

	@Override
	public void detachView() {
		this.mView = null;
	}

	public boolean isViewAttached() {
		return mView != null;
	}

	public T getView() {
		return mView;
	}

}
