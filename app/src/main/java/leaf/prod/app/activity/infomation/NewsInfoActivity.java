package leaf.prod.app.activity.infomation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.loopj.android.image.WebImage;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.activity.BaseActivity;
import leaf.prod.app.layout.RoundSmartImageView;
import leaf.prod.app.utils.ButtonClickUtil;
import leaf.prod.app.utils.LyqbLogger;
import leaf.prod.app.utils.ShareUtil;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.model.response.crawler.IndexResult;
import leaf.prod.walletsdk.model.response.crawler.News;
import leaf.prod.walletsdk.service.CrawlerService;
import leaf.prod.walletsdk.util.DpUtil;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsInfoActivity extends BaseActivity {

    @BindView(R.id.title)
    public TitleView title;

    @BindView(R.id.tv_title)
    public TextView tvTitle;
    //    @BindView(R.id.tv_content)
    //    public TextView tvContent;

    @BindView(R.id.tv_time)
    public TextView tvTime;

    @BindView(R.id.tv_source)
    public TextView tvSource;

    @BindView(R.id.ll_content)
    public LinearLayout llContent;

    @BindView(R.id.sv_content)
    public ScrollView svContent;

    @BindView(R.id.tv_share)
    public TextView tvShare;

    private static CrawlerService crawlerService;

    private static int margin = 0;

    private News news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_news_info);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
        margin = DpUtil.dp2Int(this, 12);
        if (crawlerService == null) {
            crawlerService = new CrawlerService();
        }
    }

    @Override
    protected void initPresenter() {
    }

    @Override
    public void initTitle() {
        title.setBTitle(getResources().getString(R.string.news));
        title.clickLeftGoBack(getWContext());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initView() {
        margin = DpUtil.dp2Int(this, 12);
        news = (News) getIntent().getSerializableExtra("data");
        tvTitle.setText(news.getTitle());
        //        tvContent.setText(news.getContent());
        tvTime.setText(news.getPublishTime());
        tvSource.setText(getString(R.string.news_source) + ": " + news.getSource());
        tvShare.setText(getString(R.string.news_share) + " " + (news.getForwardNum() > 0 ? news.getForwardNum() : ""));
        Pattern p = Pattern.compile("<img src=\"([\\s\\S]*?)\">");
        Matcher m = p.matcher(news.getContent());
        int begin = 0;
        int end = 0;
        while (m.find()) {
            String content = news.getContent().substring(begin, m.start());
            String image = news.getContent()
                    .substring(m.start(), m.end())
                    .replace("<img src=\"", "")
                    .replace("\">", "");
            addTextView(content.trim());
            addImageView(image);
            begin = end = m.end();
        }
        if (begin == 0) {
            addTextView(news.getContent());
        }
        if (end < news.getContent().length() && !news.getContent().substring(end).trim().isEmpty()) {
            addTextView(news.getContent().substring(end).trim());
        }
        svContent.post(() -> svContent.fullScroll(ScrollView.FOCUS_UP));
    }

    @Override
    public void initData() {
    }

    private void addTextView(String content) {
        if (content.trim().isEmpty())
            return;
        TextView textView = new TextView(this);
        textView.setId(View.generateViewId());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, margin, 0, 0);
        textView.setLayoutParams(lp);
        textView.setTextIsSelectable(true);
        textView.setText(content);
        textView.setTextColor(getResources().getColor(R.color.colorNineText));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        textView.setLineSpacing(0, 1.5f);
        llContent.addView(textView);
    }

    private void addImageView(String url) {
        LyqbLogger.log(url);
        WebImage webImage = new WebImage(url);
        //        Bitmap bitmap = webImage.getBitmap(this);
        //        if (bitmap == null || bitmap.getWidth() < 80)
        //            return;
        RoundSmartImageView imageView = new RoundSmartImageView(this);
        imageView.setId(View.generateViewId());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, margin, 0, 0);
        imageView.setLayoutParams(lp);
        imageView.setImage(webImage);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        llContent.addView(imageView);
    }

    @OnClick({R.id.cl_share})
    public void onViewClicked(View view) {
        if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
            switch (view.getId()) {
                case R.id.cl_share:
                    ShareUtil.uShareUrl(this, news.getTitle(), news.getUrl(), " ", new UMShareListener() {
                        @Override
                        public void onStart(SHARE_MEDIA platform) {
                        }

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResult(SHARE_MEDIA platform) {
                            RxToast.success(getResources().getString(R.string.share_success));
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
                                            tvShare.setText(getString(R.string.news_share) + " " + indexResult.getForwardNum());
                                        }
                                    });
                        }

                        @Override
                        public void onError(SHARE_MEDIA platform, Throwable t) {
                            if (t.getMessage().contains("2008")) {//错误码
                                RxToast.error(getResources().getString(R.string.share_failed_no_app));
                            } else {
                                RxToast.error(getResources().getString(R.string.share_failed, t.getMessage()));
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
