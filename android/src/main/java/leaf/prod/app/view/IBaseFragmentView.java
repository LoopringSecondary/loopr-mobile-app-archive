package leaf.prod.app.view;

import com.trello.rxlifecycle.FragmentEvent;

import rx.Observable;

/**
 * Title:IBaseActivityView
 */
public interface IBaseFragmentView {

    /**
     * 结束刷新
     */
    void finishRefresh();

    /**
     * 页面显示提示语
     *
     * @param str 提示语
     */
    void showMessage(String str);

    /**
     * RxFragment/RxActivity 中方法,声明在view中 便于在mvp中的presenter里调用
     *
     * @param <T> T
     * @return
     */
    <T> Observable.Transformer<T, T> bindToLifecycle();

    /**
     * RxFragment/RxActivity 中方法,声明在view中 便于在mvp中的presenter里调用
     *
     * @param <T> T
     * @return
     */
    <T> Observable.Transformer<T, T> bindUntilEvent(FragmentEvent event);
}
