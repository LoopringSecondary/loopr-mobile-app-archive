package leaf.prod.app.adapter.infomation;

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vondear.rxtool.view.RxToast;

import leaf.prod.app.R;
import leaf.prod.app.utils.ButtonClickUtil;
import leaf.prod.app.utils.LyqbLogger;
import leaf.prod.app.utils.ShareUtil;
import leaf.prod.walletsdk.model.response.crawler.IndexResult;
import leaf.prod.walletsdk.model.response.crawler.News;
import leaf.prod.walletsdk.service.CrawlerService;
import leaf.prod.walletsdk.util.DateUtil;
import leaf.prod.walletsdk.util.LanguageUtil;
import leaf.prod.walletsdk.util.SPUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class NewsFlashAdapter extends BaseQuickAdapter<News, BaseViewHolder> {

    private Activity activity;

    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd HH:mm");

    private static CrawlerService crawlerService;

    public NewsFlashAdapter(int layoutResId, @Nullable List<News> news, Activity activity) {
        super(layoutResId, news);
        this.activity = activity;
        if (crawlerService == null) {
            crawlerService = new CrawlerService();
        }
    }

    @Override
    protected void convert(BaseViewHolder helper, News news) {
        if (news == null)
            return;
        try {
            helper.setText(R.id.tv_time, DateUtil.formatFriendly(sdf1.parse(news.getPublishTime()), LanguageUtil.getSettingLanguage(activity)));
            helper.setText(R.id.tv_source, news.getSource());
            helper.setText(R.id.tv_title, news.getTitle());
            //            helper.setText(R.id.cl_content, news.getContent());
            helper.setText(R.id.tv_brief, news.getContent());
            helper.setText(R.id.tv_complete, news.getContent());
            helper.setText(R.id.tv_share, activity.getString(R.string.news_share) + " " + (news.getForwardNum() > 0 ? news
                    .getForwardNum() : ""));
            String result = (String) SPUtils.get(activity, "news_" + news.getUuid(), "");
            hideBullView(helper, news.getBullIndex());
            hideBearView(helper, news.getBearIndex());
            if ("bull".equalsIgnoreCase(result)) {
                if (news.getBullIndex() == 0) {
                    SPUtils.put(activity, "news_" + news.getUuid(), "");
                } else {
                    showBullView(helper, news.getBullIndex());
                }
            }
            if ("bear".equalsIgnoreCase(result)) {
                if (news.getBearIndex() == 0) {
                    SPUtils.put(activity, "news_" + news.getUuid(), "");
                } else {
                    showBearView(helper, news.getBearIndex());
                }
            }
            ConstraintLayout clContent = helper.getView(R.id.cl_content);
            helper.setOnClickListener(R.id.tv_brief, view -> {
                helper.setGone(R.id.tv_brief, false);
                helper.setVisible(R.id.tv_complete, true);
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) clContent.getLayoutParams();
                lp.height = ConstraintSet.WRAP_CONTENT;
                clContent.setLayoutParams(lp);
            });
            helper.setOnClickListener(R.id.tv_complete, view -> {
                helper.setGone(R.id.tv_complete, false);
                helper.setVisible(R.id.tv_brief, true);
                ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) clContent.getLayoutParams();
                lp.height = ConstraintSet.WRAP_CONTENT;
                clContent.setLayoutParams(lp);
            });
            //            ExpandableTextView clContent = helper.getView(R.id.cl_content);
            //            clContent.setExpandInterpolator(new OvershootInterpolator());
            //            clContent.setCollapseInterpolator(new OvershootInterpolator());
            //            clContent.setOnClickListener(view -> {
            //                if (clContent.isExpanded()) {
            //                    clContent.setTextSize(11);
            //                } else {
            //                    clContent.setTextSize(13);
            //                }
            //                clContent.post(clContent::toggle);
            //                //                clContent.addOnExpandListener(new ExpandableTextView.OnExpandListener() {
            //                //                    @Override
            //                //                    public void onExpand(@NonNull ExpandableTextView view) {
            //                //                        clContent.setTextSize(13);
            //                //                    }
            //                //
            //                //                    @Override
            //                //                    public void onCollapse(@NonNull ExpandableTextView view) {
            //                //                        clContent.setTextSize(11);
            //                //                    }
            //                //                });
            //                //                clContent.toggle();
            //            });
            helper.setOnClickListener(R.id.cl_bull, view -> {
                if (!(ButtonClickUtil.isFastDoubleClick(1))) {
                    setBull(helper, news);
                }
            });
            helper.setOnClickListener(R.id.cl_bear, view -> {
                if (!(ButtonClickUtil.isFastDoubleClick(1))) {
                    setBear(helper, news);
                }
            });
            helper.setOnClickListener(R.id.cl_share, view -> {
                if (!(ButtonClickUtil.isFastDoubleClick(1))) {
                    ShareUtil.uShareUrl(activity, news.getTitle(), news.getUrl(), " ", new UMShareListener() {
                        @Override
                        public void onStart(SHARE_MEDIA platform) {
                        }

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResult(SHARE_MEDIA platform) {
                            RxToast.success(activity.getResources().getString(R.string.share_success));
                            crawlerService.confirmForward(news.getUuid())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<IndexResult>() {
                                        @Override
                                        public void onCompleted() {
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            LyqbLogger.log(e.getMessage());
                                        }

                                        @Override
                                        public void onNext(IndexResult indexResult) {
                                            helper.setText(R.id.tv_share, activity.getString(R.string.news_share) + " " + indexResult
                                                    .getForwardNum());
                                        }
                                    });
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
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //    private void expandView(BaseViewHolder helper, boolean flag) {
    //
    //        clContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, flag ? clContent.getTextSize() + 8 : clContent.getTextSize() - 8);
    //        clContent.toggle();
    //        ViewGroup.LayoutParams lp = view.getLayoutParams();
    //        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
    //        view.setLayoutParams(lp);
    //    }

    private void showBullView(BaseViewHolder helper, int index) {
        helper.setText(R.id.tv_bull, activity.getString(R.string.news_bull) + " " + (index > 0 ? index : ""));
        helper.setTypeface(R.id.tv_bull, Typeface.create((Typeface) null, Typeface.BOLD));
        helper.setTextColor(R.id.tv_bull, activity.getResources().getColor(R.color.colorGreen));
        helper.setGone(R.id.iv_bull, false);
        helper.setVisible(R.id.iv_bull_active, true);
    }

    private void hideBullView(BaseViewHolder helper, int index) {
        helper.setText(R.id.tv_bull, activity.getString(R.string.news_bull) + " " + (index > 0 ? index : ""));
        helper.setTypeface(R.id.tv_bull, Typeface.create((Typeface) null, Typeface.NORMAL));
        helper.setTextColor(R.id.tv_bull, activity.getResources().getColor(R.color.colorNineText));
        helper.setVisible(R.id.iv_bull, true);
        helper.setGone(R.id.iv_bull_active, false);
    }

    private void showBearView(BaseViewHolder helper, int index) {
        helper.setText(R.id.tv_bear, activity.getString(R.string.news_bear) + " " + (index > 0 ? index : ""));
        helper.setTypeface(R.id.tv_bear, Typeface.create((Typeface) null, Typeface.BOLD));
        helper.setTextColor(R.id.tv_bear, activity.getResources().getColor(R.color.colorRed));
        helper.setGone(R.id.iv_bear, false);
        helper.setVisible(R.id.iv_bear_active, true);
    }

    private void hideBearView(BaseViewHolder helper, int index) {
        helper.setText(R.id.tv_bear, activity.getString(R.string.news_bear) + " " + (index > 0 ? index : ""));
        helper.setTypeface(R.id.tv_bear, Typeface.create((Typeface) null, Typeface.NORMAL));
        helper.setTextColor(R.id.tv_bear, activity.getResources().getColor(R.color.colorNineText));
        helper.setVisible(R.id.iv_bear, true);
        helper.setGone(R.id.iv_bear_active, false);
    }

    private void setBull(BaseViewHolder helper, News news) {
        String result = (String) SPUtils.get(activity, "news_" + news.getUuid(), "");
        if (result.isEmpty()) {
            crawlerService.confirmBull(news.getUuid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<IndexResult>() {
                        @Override
                        public void onCompleted() {
                            unsubscribe();
                        }

                        @Override
                        public void onError(Throwable e) {
                            RxToast.error(activity.getResources().getString(R.string.service_error));
                            unsubscribe();
                        }

                        @Override
                        public void onNext(IndexResult indexResult) {
                            SPUtils.put(activity, "news_" + news.getUuid(), "bull");
                            showBullView(helper, indexResult.getBullIndex());
                            unsubscribe();
                        }
                    });
        } else {
            if (result.equals("bull")) {
                crawlerService.cancelBull(news.getUuid()).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<IndexResult>() {
                            @Override
                            public void onCompleted() {
                                unsubscribe();
                            }

                            @Override
                            public void onError(Throwable e) {
                                RxToast.error(activity.getResources().getString(R.string.service_error));
                                unsubscribe();
                            }

                            @Override
                            public void onNext(IndexResult indexResult) {
                                SPUtils.put(activity, "news_" + news.getUuid(), "");
                                hideBullView(helper, indexResult.getBullIndex());
                                unsubscribe();
                            }
                        });
            } else {
                crawlerService.confirmBull(news.getUuid())
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
                                RxToast.error(activity.getResources().getString(R.string.service_error));
                                unsubscribe();
                            }

                            @Override
                            public void onNext(IndexResult indexResult) {
                                SPUtils.put(activity, "news_" + news.getUuid(), "bull");
                                showBullView(helper, indexResult.getBullIndex());
                                hideBearView(helper, indexResult.getBearIndex());
                                unsubscribe();
                            }
                        });
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setBear(BaseViewHolder helper, News news) {
        String result = (String) SPUtils.get(activity, "news_" + news.getUuid(), "");
        if (result.isEmpty()) {
            crawlerService.confirmBear(news.getUuid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<IndexResult>() {
                        @Override
                        public void onCompleted() {
                            unsubscribe();
                        }

                        @Override
                        public void onError(Throwable e) {
                            RxToast.error(activity.getResources().getString(R.string.service_error));
                            unsubscribe();
                        }

                        @Override
                        public void onNext(IndexResult indexResult) {
                            SPUtils.put(activity, "news_" + news.getUuid(), "bear");
                            showBearView(helper, indexResult.getBearIndex());
                            unsubscribe();
                        }
                    });
        } else {
            if (result.equals("bear")) {
                crawlerService.cancelBear(news.getUuid())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<IndexResult>() {
                            @Override
                            public void onCompleted() {
                                unsubscribe();
                            }

                            @Override
                            public void onError(Throwable e) {
                                RxToast.error(activity.getResources().getString(R.string.service_error));
                                unsubscribe();
                            }

                            @Override
                            public void onNext(IndexResult indexResult) {
                                SPUtils.put(activity, "news_" + news.getUuid(), "");
                                hideBearView(helper, indexResult.getBearIndex());
                                unsubscribe();
                            }
                        });
            } else {
                crawlerService.confirmBear(news.getUuid())
                        .flatMap((Func1<IndexResult, Observable<IndexResult>>) indexResult -> crawlerService.cancelBull(news
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
                                RxToast.error(activity.getResources().getString(R.string.service_error));
                                unsubscribe();
                            }

                            @Override
                            public void onNext(IndexResult indexResult) {
                                SPUtils.put(activity, "news_" + news.getUuid(), "bear");
                                showBearView(helper, indexResult.getBearIndex());
                                hideBullView(helper, indexResult.getBullIndex());
                                unsubscribe();
                            }
                        });
            }
        }
    }
}
