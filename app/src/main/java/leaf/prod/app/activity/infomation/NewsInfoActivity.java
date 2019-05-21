package leaf.prod.app.activity.infomation;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vondear.rxtool.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.activity.BaseActivity;
import leaf.prod.app.adapter.infomation.NewsInfoDetailAdapter;
import leaf.prod.app.utils.ButtonClickUtil;
import leaf.prod.app.utils.ShareUtil;
import leaf.prod.app.views.TitleView;
import leaf.prod.walletsdk.manager.NewsDataManager;
import leaf.prod.walletsdk.model.response.crawler.News;
import leaf.prod.walletsdk.service.CrawlerService;
import leaf.prod.walletsdk.util.DpUtil;
import leaf.prod.walletsdk.util.SPUtils;

public class NewsInfoActivity extends BaseActivity {

    @BindView(R.id.title)
    public TitleView title;

    @BindView(R.id.recycler_view)
    public RecyclerView recyclerView;

    @BindView(R.id.tv_pre)
    public TextView tvPre;

    @BindView(R.id.tv_next)
    public TextView tvNext;

    @BindView(R.id.cl_footer)
    public ConstraintLayout clFooter;

    private NewsInfoDetailAdapter adapter;

    private static CrawlerService crawlerService;

    private News news;

    private List<News> newsList = new ArrayList();

    private int position = 1;

    private NewsDataManager newsDataManager;

    private AlertDialog letterDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_news_info);
        ButterKnife.bind(this);
        super.onCreate(savedInstanceState);
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

    @Override
    public void initView() {
    }

    @Override
    public void initData() {
        newsDataManager = NewsDataManager.getInstance(this);
        news = (News) getIntent().getSerializableExtra("data");
        News preNews = newsDataManager.getPreNews(news);
        if (preNews == null) {
            position = 0;
        } else {
            newsList.add(preNews);
        }
        newsList.add(news);
        News nextNews = newsDataManager.getNextNews(news);
        if (nextNews != null) {
            newsList.add(newsDataManager.getNextNews(news));
        }
        adapter = new NewsInfoDetailAdapter(R.layout.adapter_news_info, newsList, position, recyclerView, this);
        recyclerView.setAdapter(adapter);
    }

    @OnClick({R.id.cl_share, R.id.cl_letter, R.id.tv_pre, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cl_share:
                if (!(ButtonClickUtil.isFastDoubleClick(1))) { //防止一秒内多次点击
                    ShareUtil.uShareUrl(this, news.getTitle(), news.getUrl(), " ", new UMShareListener() {
                        @Override
                        public void onStart(SHARE_MEDIA platform) {
                        }

                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onResult(SHARE_MEDIA platform) {
                            RxToast.success(getResources().getString(R.string.share_success));
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
                }
                break;
            case R.id.cl_letter:
                if (letterDialog == null) {
                    final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(NewsInfoActivity.this, R.style.DialogTheme);//
                    View view1 = LayoutInflater.from(NewsInfoActivity.this)
                            .inflate(R.layout.dialog_letter_modify, null);
                    builder.setView(view1);
                    int textSize = (int) SPUtils.get(NewsInfoActivity.this, "news_text_size", 17);
                    adapter.setLetterSize(textSize);
                    view1.findViewById(R.id.iv_decrease_select)
                            .setVisibility(textSize == 17 ? View.VISIBLE : View.GONE);
                    view1.findViewById(R.id.iv_decrease_unselect)
                            .setVisibility(textSize == 17 ? View.GONE : View.VISIBLE);
                    view1.findViewById(R.id.iv_increase_select)
                            .setVisibility(textSize == 21 ? View.VISIBLE : View.GONE);
                    view1.findViewById(R.id.iv_increase_unselect)
                            .setVisibility(textSize == 21 ? View.GONE : View.VISIBLE);
                    view1.findViewById(R.id.small_letter).setOnClickListener(view2 -> {
                        SPUtils.put(NewsInfoActivity.this, "news_text_size", 17);
                        view1.findViewById(R.id.iv_decrease_select).setVisibility(View.VISIBLE);
                        view1.findViewById(R.id.iv_decrease_unselect).setVisibility(View.GONE);
                        view1.findViewById(R.id.iv_increase_select).setVisibility(View.GONE);
                        view1.findViewById(R.id.iv_increase_unselect).setVisibility(View.VISIBLE);
                        adapter.setLetterSize(17);
                    });
                    view1.findViewById(R.id.big_letter).setOnClickListener(view2 -> {
                        SPUtils.put(NewsInfoActivity.this, "news_text_size", 21);
                        view1.findViewById(R.id.iv_decrease_select).setVisibility(View.GONE);
                        view1.findViewById(R.id.iv_decrease_unselect).setVisibility(View.VISIBLE);
                        view1.findViewById(R.id.iv_increase_select).setVisibility(View.VISIBLE);
                        view1.findViewById(R.id.iv_increase_unselect).setVisibility(View.GONE);
                        adapter.setLetterSize(21);
                    });
                    letterDialog = builder.create();
                    letterDialog.setCancelable(true);
                    letterDialog.setCanceledOnTouchOutside(true);
                    Window window = letterDialog.getWindow();
                    WindowManager.LayoutParams lp = window.getAttributes();
                    lp.y = DpUtil.dp2Int(this, 60);
                    window.setAttributes(lp);
                    window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    window.setGravity(Gravity.BOTTOM);
                }
                letterDialog.show();
                break;
        }
    }

    public void showTopAndBottom(boolean flag) {
        if (flag && title.getVisibility() != View.VISIBLE) {
            title.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translate_between_interface_top_in));
            clFooter.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translate_between_interface_bottom_in));
            title.setVisibility(View.VISIBLE);
            clFooter.setVisibility(View.VISIBLE);
        } else if (!flag && title.getVisibility() != View.GONE) {
            title.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translate_between_interface_top_out));
            clFooter.startAnimation(AnimationUtils.loadAnimation(this, R.anim.translate_between_interface_bottom_out));
            title.setVisibility(View.GONE);
            clFooter.setVisibility(View.GONE);
        }
    }
}
