package leaf.prod.app.adapter.infomation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.loopj.android.image.WebImage;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;

import leaf.prod.app.R;
import leaf.prod.app.activity.infomation.NewsInfoActivity;
import leaf.prod.app.layout.PinchImageView;
import leaf.prod.app.layout.RoundSmartImageView;
import leaf.prod.app.utils.LyqbLogger;
import leaf.prod.walletsdk.manager.NewsDataManager;
import leaf.prod.walletsdk.model.response.crawler.News;
import leaf.prod.walletsdk.service.CrawlerService;
import leaf.prod.walletsdk.util.DateUtil;
import leaf.prod.walletsdk.util.DpUtil;
import leaf.prod.walletsdk.util.LanguageUtil;
import leaf.prod.walletsdk.util.SPUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NewsInfoDetailAdapter extends BaseQuickAdapter<News, BaseViewHolder> {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private NewsInfoActivity activity;

    private RecyclerView recyclerView;

    private static int margin = 0;

    private List<News> newsList;

    private NewsDataManager newsDataManager;

    private int index = 1;

    private LinearLayoutManager layoutManager;

    private PagerSnapHelper pagerSnapHelper;

    private static int animate = 0;

    private List<TextView> textViews = new ArrayList<>();

    private int textSize;

    private static CrawlerService crawlerService;

    public NewsInfoDetailAdapter(int layoutResId, List<News> news, int index, RecyclerView recyclerView, NewsInfoActivity activity) {
        super(layoutResId, news);
        this.activity = activity;
        this.newsList = news;
        this.index = index;
        this.recyclerView = recyclerView;
        if (crawlerService == null) {
            crawlerService = new CrawlerService();
        }
        layoutManager = new LinearLayoutManager(recyclerView.getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        this.recyclerView.setLayoutManager(layoutManager);
        pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(this.recyclerView);
        layoutManager.scrollToPositionWithOffset(index, 0);
        margin = DpUtil.dp2Int(recyclerView.getContext(), 12);
        newsDataManager = NewsDataManager.getInstance(recyclerView.getContext());
    }

    @Override
    protected void convert(BaseViewHolder helper, News item) {
        if (item == null)
            return;
        try {
            crawlerService.confirmReadNum(item.getUuid())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe();
            textSize = (int) SPUtils.get(activity, "news_text_size", 17);
            ((LinearLayout) helper.getView(R.id.ll_content)).removeAllViews();
            helper.setText(R.id.tv_title, item.getTitle());
            helper.setText(R.id.tv_time, DateUtil.formatFriendly(sdf.parse(item.getPublishTime()), LanguageUtil.getSettingLanguage(activity)));
            helper.setText(R.id.tv_source, recyclerView.getContext().getResources()
                    .getString(R.string.news_source) + ": " + item.getSource());
            ConstraintLayout llImageView = helper.getView(R.id.cl_image_view);
            PinchImageView pinchImageView = helper.getView(R.id.image_view);
            pinchImageView.setOnClickListener(view -> {
                llImageView.setVisibility(View.GONE);
                activity.setSwipeBackEnable(true);
            });
            Pattern p = Pattern.compile("<img src=\"([\\s\\S]*?)\">");
            Matcher m = p.matcher(item.getContent());
            int begin = 0;
            int end = 0;
            while (m.find()) {
                String content = item.getContent().substring(begin, m.start());
                String image = item.getContent()
                        .substring(m.start(), m.end())
                        .replace("<img src=\"", "")
                        .replace("\">", "");
                addTextView(helper, content.trim());
                addImageView(helper, image, pinchImageView, llImageView);
                begin = end = m.end();
            }
            if (begin == 0) {
                addTextView(helper, item.getContent());
            }
            if (end < item.getContent().length() && !item.getContent().substring(end).trim().isEmpty()) {
                addTextView(helper, item.getContent().substring(end).trim());
            }
            if (index < newsList.size() - 1) {
                News nextNews = newsList.get(index + 1);
                helper.setText(R.id.tv_next_title, nextNews.getTitle());
                helper.setText(R.id.tv_next_time, nextNews.getPublishTime());
                helper.setText(R.id.tv_next_source, nextNews.getSource());
                helper.setGone(R.id.cl_has_next, true);
                helper.setGone(R.id.cl_end, false);
            } else {
                helper.setGone(R.id.cl_has_next, false);
                helper.setGone(R.id.cl_end, true);
            }
            ScrollView svContent = helper.getView(R.id.sv_content);
            svContent.post(() -> svContent.scrollTo(0, 0));
            svContent.scrollTo(0, 0);
            activity.showTopAndBottom(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                svContent.setOnScrollChangeListener((view, l, t, oldl, oldt) -> {
                    helper.setGone(R.id.head, false);
                    if (oldt < t && ((t - oldt) > 15)) {
                        activity.showTopAndBottom(false);
                    } else if (oldt > t && (oldt - t) > 15) {
                        activity.showTopAndBottom(true);
                    }
                });
            }
            ((SmartRefreshLayout) helper.getView(R.id.refresh_layout)).setOnMultiPurposeListener(new OnMultiPurposeListener() {
                @Override
                public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {
                }

                @Override
                public void onHeaderReleased(RefreshHeader header, int headerHeight, int maxDragHeight) {
                }

                @Override
                public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int maxDragHeight) {
                }

                @Override
                public void onHeaderFinish(RefreshHeader header, boolean success) {
                }

                @Override
                public void onFooterMoving(RefreshFooter footer, boolean isDragging, float percent, int offset, int footerHeight, int maxDragHeight) {
                }

                @Override
                public void onFooterReleased(RefreshFooter footer, int footerHeight, int maxDragHeight) {
                }

                @Override
                public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int maxDragHeight) {
                }

                @Override
                public void onFooterFinish(RefreshFooter footer, boolean success) {
                }

                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    if (index < newsList.size() - 1) {
                        goNext();
                        svContent.startAnimation(AnimationUtils.loadAnimation(recyclerView.getContext(), R.anim.translate_between_interface_top_in));
                        layoutManager.scrollToPositionWithOffset(index, 0);
                        refreshLayout.finishLoadMore();
                    } else {
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    if (index > 0) {
                        goPre();
                        svContent.startAnimation(AnimationUtils.loadAnimation(recyclerView.getContext(), R.anim.translate_between_interface_bottom_in));
                        layoutManager.scrollToPositionWithOffset(index, 0);
                        refreshLayout.finishRefresh(true);
                    } else {
                        refreshLayout.finishRefresh(false);
                    }
                }

                @Override
                public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
                    if (newState.isHeader) {
                        helper.setGone(R.id.head, true);
                    }
                }
            });
            if (animate == 1) {
                svContent.startAnimation(AnimationUtils.loadAnimation(recyclerView.getContext(), R.anim.translate_between_interface_top_in));
            } else if (animate == 2) {
                svContent.startAnimation(AnimationUtils.loadAnimation(recyclerView.getContext(), R.anim.translate_between_interface_bottom_in));
            }
            animate = 0;
        } catch (Exception e) {
            LyqbLogger.log(e.getMessage());
        }
    }

    private void goPre() {
        if (index > 1) {
            index--;
        } else {
            News preNews = newsDataManager.getPreNews(newsList.get(0));
            if (preNews == null) {
                index = 0;
            } else {
                newsList.add(0, preNews);
                notifyItemInserted(0);
            }
        }
        animate = 1;
        notifyDataSetChanged();
    }

    private void goNext() {
        if (index < newsList.size() - 2) {
            index++;
        } else {
            News nextNews = newsDataManager.getNextNews(newsList.get(newsList.size() - 1));
            if (nextNews != null) {
                newsList.add(nextNews);
                notifyItemInserted(newsList.size() - 1);
            } else {
            }
            index++;
        }
        animate = 2;
        notifyDataSetChanged();
    }

    private void addTextView(BaseViewHolder holder, String content) {
        if (content.trim().isEmpty())
            return;
        TextView textView = new TextView(recyclerView.getContext());
        textView.setId(View.generateViewId());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, margin, 0, 0);
        textView.setLayoutParams(lp);
        textView.setTextIsSelectable(true);
        textView.setText(content);
        textView.setTextColor(recyclerView.getContext().getResources().getColor(R.color.colorNineText));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        textView.setLineSpacing(0, 1.7f);
        ((LinearLayout) holder.getView(R.id.ll_content)).addView(textView);
        textViews.add(textView);
    }

    private void addImageView(BaseViewHolder holder, String url, PinchImageView pinchImageView, ConstraintLayout llImageView) {
        WebImage webImage = new WebImage(url);
        RoundSmartImageView imageView = new RoundSmartImageView(recyclerView.getContext());
        imageView.setId(View.generateViewId());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, margin, 0, 0);
        imageView.setLayoutParams(lp);
        imageView.setImage(webImage);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setOnClickListener(view -> {
            pinchImageView.setImageDrawable(imageView.getDrawable());
            pinchImageView.reset();
            activity.setSwipeBackEnable(false);
            llImageView.setVisibility(View.VISIBLE);
        });
        ((LinearLayout) holder.getView(R.id.ll_content)).addView(imageView);
    }

    public void setLetterSize(int size) {
        for (TextView textView : textViews) {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
    }
}
