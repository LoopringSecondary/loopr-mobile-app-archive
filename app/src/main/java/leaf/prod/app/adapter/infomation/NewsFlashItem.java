package leaf.prod.app.adapter.infomation;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vondear.rxtool.view.RxToast;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.utils.ButtonClickUtil;
import leaf.prod.app.utils.LyqbLogger;
import leaf.prod.app.utils.ShareUtil;
import leaf.prod.walletsdk.model.response.crawler.IndexResult;
import leaf.prod.walletsdk.model.response.crawler.News;
import leaf.prod.walletsdk.service.CrawlerService;
import leaf.prod.walletsdk.util.SPUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-12-22 4:25 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public class NewsFlashItem extends NewsBodyItem {

    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd HH:mm");

    @BindView(R.id.tv_time)
    public TextView tvTime;

    @BindView(R.id.tv_title)
    public TextView tvTitle;

    @BindView(R.id.cl_content)
    public ExpandableTextView clContent;

    @BindView(R.id.tv_share)
    public TextView tvShare;

    @BindView(R.id.tv_bear)
    public TextView tvBear;

    @BindView(R.id.tv_bull)
    public TextView tvBull;

    @BindView(R.id.tv_source)
    public TextView tvSource;

    private static CrawlerService crawlerService = new CrawlerService();

    private boolean expand = false;

    private News data;

    public NewsFlashItem(View itemView, Activity activity) {
        super(itemView, activity);
        clContent.setExpandInterpolator(new OvershootInterpolator());
        clContent.setCollapseInterpolator(new OvershootInterpolator());
        clContent.setOnClickListener(view -> {
            if (!expand && !clContent.isExpanded() || expand && clContent.isExpanded()) {
                expandView(expand = !expand);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setContent(News data) {
        if (data == null)
            return;
        try {
            this.data = data;
            tvTime.setText(sdf2.format(sdf1.parse(data.getPublishTime())));
            tvSource.setText(innerLayout.getResources().getString(R.string.news_source) + ":" + data.getSource());
            tvTitle.setText(data.getTitle());
            clContent.setText(data.getContent());
            tvShare.setText(innerLayout.getResources()
                    .getString(R.string.news_share) + " " + (data.getForwardNum() > 0 ? data.getForwardNum() : ""));
            tvBear.setText(innerLayout.getResources()
                    .getString(R.string.news_bear) + " " + (data.getBearIndex() > 0 ? data.getBearIndex() : ""));
            tvBull.setText(innerLayout.getResources()
                    .getString(R.string.news_bull) + " " + (data.getBullIndex() > 0 ? data.getBullIndex() : ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void expandView(boolean flag) {
        clContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, flag ? clContent.getTextSize() + 8 : clContent.getTextSize() - 8);
        clContent.toggle();
        ViewGroup.LayoutParams lp = innerLayout.getLayoutParams();
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        innerLayout.setLayoutParams(lp);
    }

    @OnClick({R.id.tv_bull, R.id.tv_bear, R.id.tv_share})
    public void onViewClicked(View view) {
        LyqbLogger.log(data.getBullIndex() + " " + data.getBearIndex());
        if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
            switch (view.getId()) {
                case R.id.tv_bull:
                    setBull();
                    break;
                case R.id.tv_bear:
                    setBear();
                    break;
                case R.id.tv_share:
                    ShareUtil.uShareUrl(activity, data.getTitle(), data.getUrl(), " ", new UMShareListener() {
                        @Override
                        public void onStart(SHARE_MEDIA platform) {
                        }

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResult(SHARE_MEDIA platform) {
                            RxToast.success(activity.getResources().getString(R.string.share_success));
                            crawlerService.confirmForward(data.getUuid());
                            tvShare.setText(activity.getString(R.string.news_share) + " " + (data.getForwardNum() + 1));
                        }

                        @Override
                        public void onError(SHARE_MEDIA platform, Throwable t) {
                            if (t.getMessage().contains("2008")) {//错误码
                                RxToast.error(activity.getResources().getString(R.string.share_failed_no_app));
                            } else {
                                RxToast.error(activity.getResources().getString(R.string.share_failed, t.getMessage()));
                            }
                        }

                        @Override
                        public void onCancel(SHARE_MEDIA platform) {
                        }
                    });
                    break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setBull() {
        String result = (String) SPUtils.get(innerLayout.getContext(), "news_" + data.getUuid(), "");
        if (result.isEmpty()) {
            crawlerService.confirmBull(data.getUuid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<IndexResult>() {
                        @Override
                        public void onCompleted() {
                            unsubscribe();
                        }

                        @Override
                        public void onError(Throwable e) {
                            RxToast.error("服务器出错");
                            unsubscribe();
                        }

                        @Override
                        public void onNext(IndexResult indexResult) {
                            SPUtils.put(innerLayout.getContext(), "news_" + data.getUuid(), "bull");
                            tvBull.setTypeface(null, Typeface.BOLD);
                            tvBear.setTypeface(null, Typeface.NORMAL);
                            tvBull.setText(innerLayout.getResources().getString(R.string.news_bull) + "  " + indexResult
                                    .getBullIndex());
                            unsubscribe();
                        }
                    });
        } else {
            if (result.equals("bull")) {
                crawlerService.cancelBull(data.getUuid()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<IndexResult>() {
                            @Override
                            public void onCompleted() {
                                unsubscribe();
                            }

                            @Override
                            public void onError(Throwable e) {
                                RxToast.error("服务器出错");
                                unsubscribe();
                            }

                            @Override
                            public void onNext(IndexResult indexResult) {
                                SPUtils.put(innerLayout.getContext(), "news_" + data.getUuid(), "");
                                tvBull.setTypeface(null, Typeface.NORMAL);
                                tvBull.setText(innerLayout.getResources()
                                        .getString(R.string.news_bull) + "  " + (indexResult.getBullIndex() > 0 ? indexResult
                                        .getBullIndex() : ""));
                                unsubscribe();
                            }
                        });
            } else {
                crawlerService.confirmBull(data.getUuid())
                        .flatMap((Func1<IndexResult, Observable<IndexResult>>) indexResult -> crawlerService.cancelBear(indexResult
                                .getUuid()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<IndexResult>() {
                            @Override
                            public void onCompleted() {
                                unsubscribe();
                            }

                            @Override
                            public void onError(Throwable e) {
                                RxToast.error("服务器出错");
                                unsubscribe();
                            }

                            @Override
                            public void onNext(IndexResult indexResult) {
                                SPUtils.put(innerLayout.getContext(), "news_" + data.getUuid(), "bull");
                                tvBear.setTypeface(null, Typeface.NORMAL);
                                tvBull.setTypeface(null, Typeface.BOLD);
                                tvBull.setText(innerLayout.getResources()
                                        .getString(R.string.news_bull) + "  " + (indexResult.getBullIndex() > 0 ? indexResult
                                        .getBullIndex() : ""));
                                tvBear.setText(innerLayout.getResources()
                                        .getString(R.string.news_bear) + "  " + (indexResult.getBearIndex() > 0 ? indexResult
                                        .getBearIndex() : ""));
                                unsubscribe();
                            }
                        });
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setBear() {
        String result = (String) SPUtils.get(innerLayout.getContext(), "news_" + data.getUuid(), "");
        if (result.isEmpty()) {
            crawlerService.confirmBear(data.getUuid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<IndexResult>() {
                        @Override
                        public void onCompleted() {
                            unsubscribe();
                        }

                        @Override
                        public void onError(Throwable e) {
                            RxToast.error("服务器出错");
                            unsubscribe();
                        }

                        @Override
                        public void onNext(IndexResult indexResult) {
                            SPUtils.put(innerLayout.getContext(), "news_" + data.getUuid(), "bear");
                            tvBear.setTypeface(null, Typeface.BOLD);
                            tvBull.setTypeface(null, Typeface.NORMAL);
                            tvBear.setText(innerLayout.getResources().getString(R.string.news_bear) + "  " + indexResult
                                    .getBearIndex());
                            unsubscribe();
                        }
                    });
        } else {
            if (result.equals("bear")) {
                crawlerService.cancelBear(data.getUuid())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<IndexResult>() {
                            @Override
                            public void onCompleted() {
                                unsubscribe();
                            }

                            @Override
                            public void onError(Throwable e) {
                                RxToast.error("服务器出错");
                                unsubscribe();
                            }

                            @Override
                            public void onNext(IndexResult indexResult) {
                                SPUtils.put(innerLayout.getContext(), "news_" + data.getUuid(), "");
                                tvBear.setTypeface(null, Typeface.NORMAL);
                                tvBear.setText(innerLayout.getResources()
                                        .getString(R.string.news_bear) + "  " + (indexResult.getBearIndex() > 0 ? indexResult
                                        .getBearIndex() : ""));
                                unsubscribe();
                            }
                        });
            } else {
                crawlerService.confirmBear(data.getUuid())
                        .flatMap((Func1<IndexResult, Observable<IndexResult>>) indexResult -> crawlerService.cancelBull(data
                                .getUuid()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<IndexResult>() {

                            @Override
                            public void onCompleted() {
                                unsubscribe();
                            }

                            @Override
                            public void onError(Throwable e) {
                                RxToast.error("服务器出错");
                                unsubscribe();
                            }

                            @Override
                            public void onNext(IndexResult indexResult) {
                                SPUtils.put(innerLayout.getContext(), "news_" + data.getUuid(), "bear");
                                tvBear.setTypeface(null, Typeface.BOLD);
                                tvBull.setTypeface(null, Typeface.NORMAL);
                                tvBull.setText(innerLayout.getResources()
                                        .getString(R.string.news_bull) + "  " + (data.getBullIndex() > 0 ? data.getBullIndex() : ""));
                                tvBear.setText(innerLayout.getResources()
                                        .getString(R.string.news_bear) + "  " + (data.getBearIndex() > 0 ? data.getBearIndex() : ""));
                                unsubscribe();
                            }
                        });
            }
        }
    }
}
