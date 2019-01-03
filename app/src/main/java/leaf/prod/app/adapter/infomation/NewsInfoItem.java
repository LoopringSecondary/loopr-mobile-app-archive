package leaf.prod.app.adapter.infomation;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.activity.infomation.NewsInfoActivity;
import leaf.prod.app.utils.ButtonClickUtil;
import leaf.prod.app.utils.LyqbLogger;
import leaf.prod.app.utils.ShareUtil;
import leaf.prod.walletsdk.model.response.crawler.IndexResult;
import leaf.prod.walletsdk.model.response.crawler.News;
import leaf.prod.walletsdk.service.CrawlerService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-12-22 4:25 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public class NewsInfoItem extends NewsBodyItem {

    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd HH:mm");

    @BindView(R.id.tv_time)
    public TextView tvTime;

    @BindView(R.id.tv_title)
    public TextView tvTitle;

    @BindView(R.id.cl_content)
    public TextView clContent;

    @BindView(R.id.tv_share)
    public TextView tvShare;
    //    @BindView(R.id.tv_comment)
    //    public TextView tvComment;

    @BindView(R.id.tv_source)
    public TextView tvSource;

    private News data;

    private static CrawlerService crawlerService;

    public NewsInfoItem(View itemView, Activity activity) {
        super(itemView, activity);
        if (crawlerService == null) {
            crawlerService = new CrawlerService();
        }
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
            Pattern p = Pattern.compile("<img src=\"([\\s\\S]*?)\">");
            Matcher m = p.matcher(data.getContent());
            clContent.setText(m.replaceAll(""));
            tvShare.setText(innerLayout.getResources()
                    .getString(R.string.news_share) + " " + (data.getForwardNum() > 0 ? data
                    .getForwardNum() : ""));
            innerLayout.setOnClickListener(view -> {
                Intent intent = new Intent(innerLayout.getContext(), NewsInfoActivity.class);
                intent.putExtra("data", data);
                innerLayout.getContext().startActivity(intent);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.cl_share})
    public void onViewClicked(View view) {
        if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
            switch (view.getId()) {
                case R.id.cl_share:
                    ShareUtil.uShareUrl(activity, data.getTitle(), data.getUrl(), " ", new UMShareListener() {
                        @Override
                        public void onStart(SHARE_MEDIA platform) {
                        }

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResult(SHARE_MEDIA platform) {
                            RxToast.success(activity.getResources().getString(R.string.share_success));
                            crawlerService.confirmForward(data.getUuid())
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
                                            tvShare.setText(activity.getString(R.string.news_share) + " " + indexResult.getForwardNum());
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
                    break;
            }
        }
    }
}
