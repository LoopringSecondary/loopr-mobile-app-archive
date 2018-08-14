package com.tomcat360.lyqb.presenter;


import com.tomcat360.lyqb.view.IBaseFragmentView;

/**
 * Title:BaseActivityPresenter
 */

public class BaseFragmentPresenter<T extends IBaseFragmentView> implements FPresenter<T> {

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
