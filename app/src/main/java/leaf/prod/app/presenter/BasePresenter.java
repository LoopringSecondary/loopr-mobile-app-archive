package leaf.prod.app.presenter;

import android.content.Context;

public class BasePresenter<V> {

    protected V view;

    protected Context context;

    public BasePresenter(V view, Context context) {
        this.attachView(view);
        this.context = context;
    }

    public void attachView(V view) {
        this.view = view;
    }
}
