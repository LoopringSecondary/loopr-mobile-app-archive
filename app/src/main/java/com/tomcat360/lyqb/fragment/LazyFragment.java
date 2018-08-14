package com.tomcat360.lyqb.fragment;

import com.trello.rxlifecycle.components.support.RxFragment;

/**
 * Title:LazyFragment
 */
public abstract class LazyFragment extends RxFragment {
	protected boolean isVisible;
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}
	protected void onVisible(){
		lazyLoad();
	}
	protected abstract void lazyLoad();
	protected void onInvisible(){}
}
