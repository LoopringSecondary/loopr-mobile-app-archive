package leaf.prod.app.adapter.infomation;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vondear.rxtool.view.RxToast;

import leaf.prod.app.R;
import leaf.prod.app.layout.RoundSmartImageView;
import leaf.prod.app.utils.ButtonClickUtil;
import leaf.prod.app.utils.LyqbLogger;
import leaf.prod.app.utils.ShareUtil;
import leaf.prod.walletsdk.model.response.crawler.IndexResult;
import leaf.prod.walletsdk.model.response.crawler.News;
import leaf.prod.walletsdk.service.CrawlerService;
import leaf.prod.walletsdk.util.DateUtil;
import leaf.prod.walletsdk.util.LanguageUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsInfoAdapter extends BaseQuickAdapter<News, BaseViewHolder> {

    private Activity activity;

    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private Pattern p = Pattern.compile("<img src=\"([\\s\\S]*?)\">");

    private static CrawlerService crawlerService;

    public NewsInfoAdapter(int layoutResId, @Nullable List<News> news, Activity activity) {
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
            TextView tvTitle = helper.getView(R.id.tv_title);
            tvTitle.setText(news.getTitle());
            Matcher m = p.matcher(news.getContent());
            TextView tvContent = helper.getView(R.id.tv_content);
            tvContent.post(() -> {
                if (4 - tvTitle.getLineCount() > 0) {
                    tvContent.setVisibility(View.VISIBLE);
                    tvContent.setLines(4 - tvTitle.getLineCount());
                    tvContent.setText(m.replaceAll("").trim());
                } else {
                    tvContent.setVisibility(View.GONE);
                }
            });
            String img = getFirstImg(news.getContent());
            if (img == null) {
                helper.setGone(R.id.iv_navigation, false);
            } else {
                helper.setGone(R.id.iv_navigation, true);
                RoundSmartImageView imageView = helper.getView(R.id.iv_navigation);
                imageView.setImageUrl(img);
            }
            helper.setText(R.id.tv_share, activity.getString(R.string.news_share) + " " + (news.getForwardNum() > 0 ? news
                    .getForwardNum() : ""));
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

    private String getFirstImg(String content) {
        Matcher m = p.matcher(content);
        if (m.find()) {
            return content
                    .substring(m.start(), m.end())
                    .replace("<img src=\"", "")
                    .replace("\">", "");
        }
        return null;
    }
}
