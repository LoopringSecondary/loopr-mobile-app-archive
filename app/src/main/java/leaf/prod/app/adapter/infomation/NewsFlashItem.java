package leaf.prod.app.adapter.infomation;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.OnClick;
import leaf.prod.app.R;
import leaf.prod.app.utils.ButtonClickUtil;
import leaf.prod.app.utils.LyqbLogger;
import leaf.prod.walletsdk.model.response.crawler.News;
import leaf.prod.walletsdk.service.CrawlerService;
import leaf.prod.walletsdk.util.SPUtils;

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

    public NewsFlashItem(View itemView) {
        super(itemView);
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
            tvShare.setText(innerLayout.getResources().getString(R.string.news_share) + (data.getForwardNum() > 0 ? data
                    .getForwardNum() : ""));
            tvBear.setText(innerLayout.getResources()
                    .getString(R.string.news_bear) + (data.getBearIndex() > 0 ? data.getBearIndex() : ""));
            tvBull.setText(innerLayout.getResources()
                    .getString(R.string.news_bull) + (data.getBullIndex() > 0 ? data.getBullIndex() : ""));
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
                    break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setBull() {
        String result = (String) SPUtils.get(innerLayout.getContext(), "news_" + data.getUuid(), "");
        if (result.isEmpty()) {
            SPUtils.put(innerLayout.getContext(), "news_" + data.getUuid(), "bull");
            crawlerService.confirmBull(data.getUuid());
            tvBull.setTypeface(null, Typeface.BOLD);
            tvBear.setTypeface(null, Typeface.NORMAL);
            tvBull.setText(innerLayout.getResources().getString(R.string.news_bull) + "  " + (data.getBullIndex() + 1));
        } else {
            if (result.equals("bull")) {
                SPUtils.put(innerLayout.getContext(), "news_" + data.getUuid(), "");
                crawlerService.cancelBull(data.getUuid());
                tvBull.setTypeface(null, Typeface.NORMAL);
                tvBull.setText(innerLayout.getResources()
                        .getString(R.string.news_bull) + "  " + (data.getBullIndex() - 1 > 0 ? data.getBullIndex() - 1 : ""));
            } else {
                SPUtils.put(innerLayout.getContext(), "news_" + data.getUuid(), "bull");
                crawlerService.confirmBull(data.getUuid());
                crawlerService.cancelBear(data.getUuid());
                tvBear.setTypeface(null, Typeface.NORMAL);
                tvBull.setTypeface(null, Typeface.BOLD);
                tvBull.setText(innerLayout.getResources()
                        .getString(R.string.news_bull) + "  " + (data.getBullIndex() + 1));
                tvBear.setText(innerLayout.getResources()
                        .getString(R.string.news_bear) + "  " + (data.getBearIndex() - 1 > 0 ? data.getBullIndex() - 1 : ""));
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void setBear() {
        String result = (String) SPUtils.get(innerLayout.getContext(), "news_" + data.getUuid(), "");
        if (result.isEmpty()) {
            SPUtils.put(innerLayout.getContext(), "news_" + data.getUuid(), "bear");
            crawlerService.confirmBear(data.getUuid());
            tvBear.setTypeface(null, Typeface.BOLD);
            tvBull.setTypeface(null, Typeface.NORMAL);
            tvBear.setText(innerLayout.getResources().getString(R.string.news_bear) + "  " + (data.getBullIndex() + 1));
        } else {
            if (result.equals("bear")) {
                SPUtils.put(innerLayout.getContext(), "news_" + data.getUuid(), "");
                crawlerService.cancelBear(data.getUuid());
                tvBear.setTypeface(null, Typeface.NORMAL);
                tvBear.setText(innerLayout.getResources()
                        .getString(R.string.news_bear) + "  " + (data.getBearIndex() - 1 > 0 ? data.getBearIndex() - 1 : ""));
            } else {
                SPUtils.put(innerLayout.getContext(), "news_" + data.getUuid(), "bear");
                crawlerService.confirmBear(data.getUuid());
                crawlerService.cancelBull(data.getUuid());
                tvBear.setTypeface(null, Typeface.BOLD);
                tvBull.setTypeface(null, Typeface.NORMAL);
                tvBear.setText(innerLayout.getResources()
                        .getString(R.string.news_bear) + "  " + (data.getBullIndex() + 1));
                tvBull.setText(innerLayout.getResources()
                        .getString(R.string.news_bull) + "  " + (data.getBearIndex() - 1 > 0 ? data.getBullIndex() - 1 : ""));
            }
        }
    }
}
