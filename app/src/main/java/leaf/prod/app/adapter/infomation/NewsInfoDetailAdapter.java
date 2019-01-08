package leaf.prod.app.adapter.infomation;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.loopj.android.image.WebImage;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import leaf.prod.app.R;
import leaf.prod.app.activity.infomation.NewsInfoActivity;
import leaf.prod.app.layout.RoundSmartImageView;
import leaf.prod.app.utils.LyqbLogger;
import leaf.prod.walletsdk.manager.NewsDataManager;
import leaf.prod.walletsdk.model.response.crawler.News;
import leaf.prod.walletsdk.util.DpUtil;

public class NewsInfoDetailAdapter extends BaseQuickAdapter<News, BaseViewHolder> {

    private NewsInfoActivity activity;

    private static int margin = 0;

    private List<News> newsList;

    private NewsDataManager newsDataManager;

    public NewsInfoDetailAdapter(int layoutResId, List<News> news, NewsInfoActivity activity) {
        super(layoutResId, news);
        this.newsList = news;
        this.activity = activity;
        margin = DpUtil.dp2Int(activity, 12);
        newsDataManager = NewsDataManager.getInstance(activity);
    }

    @Override
    protected void convert(BaseViewHolder helper, News item) {
        if (item == null)
            return;
        LyqbLogger.log(item.getTitle());
        ((LinearLayout) helper.getView(R.id.ll_content)).removeAllViews();
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_time, item.getPublishTime());
        helper.setText(R.id.tv_source, activity.getResources()
                .getString(R.string.news_source) + ": " + item.getSource());
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
            addImageView(helper, image);
            begin = end = m.end();
        }
        if (begin == 0) {
            addTextView(helper, item.getContent());
        }
        if (end < item.getContent().length() && !item.getContent().substring(end).trim().isEmpty()) {
            addTextView(helper, item.getContent().substring(end).trim());
        }
        ScrollView svContent = helper.getView(R.id.sv_content);
        svContent.scrollTo(0, 0);
        //        svContent.post(() -> {
        ////            svContent.fullScroll(ScrollView.FOCUS_UP);
        //        });
        ((SmartRefreshLayout) helper.getView(R.id.refresh_layout)).setOnRefreshListener(refreshLayout -> {
            activity.goPre();
            ((SmartRefreshLayout) helper.getView(R.id.refresh_layout)).finishRefresh();
        });
        ((SmartRefreshLayout) helper.getView(R.id.refresh_layout)).setOnLoadMoreListener(refreshLayout -> {
            activity.goNext();
            ((SmartRefreshLayout) helper.getView(R.id.refresh_layout)).finishLoadMore();
        });
    }

    public int goPre(int position) {
        if (position > 1) {
            position = position - 1;
        } else {
            News preNews = newsDataManager.getPreNews(newsList.get(0));
            if (preNews == null) {
                position = 0;
            } else {
                newsList.add(0, preNews);
                notifyItemInserted(0);
            }
        }
        notifyDataSetChanged();
        return position;
    }

    public int goNext(int position) {
        if (position < newsList.size() - 2) {
            position++;
        } else {
            News nextNews = newsDataManager.getNextNews(newsList.get(newsList.size() - 1));
            if (nextNews != null) {
                newsList.add(nextNews);
                notifyItemInserted(newsList.size() - 1);
            }
            position++;
        }
        notifyDataSetChanged();
        return position;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    private void addTextView(BaseViewHolder holder, String content) {
        if (content.trim().isEmpty())
            return;
        TextView textView = new TextView(activity);
        textView.setId(View.generateViewId());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, margin, 0, 0);
        textView.setLayoutParams(lp);
        textView.setTextIsSelectable(true);
        textView.setText(content);
        textView.setTextColor(activity.getResources().getColor(R.color.colorNineText));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        textView.setLineSpacing(0, 1.5f);
        ((LinearLayout) holder.getView(R.id.ll_content)).addView(textView);
    }

    private void addImageView(BaseViewHolder holder, String url) {
        WebImage webImage = new WebImage(url);
        RoundSmartImageView imageView = new RoundSmartImageView(activity);
        imageView.setId(View.generateViewId());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, margin, 0, 0);
        imageView.setLayoutParams(lp);
        imageView.setImage(webImage);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        ((LinearLayout) holder.getView(R.id.ll_content)).addView(imageView);
    }
}
