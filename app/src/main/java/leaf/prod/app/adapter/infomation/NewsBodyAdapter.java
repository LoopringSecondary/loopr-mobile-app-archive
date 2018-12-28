package leaf.prod.app.adapter.infomation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ramotion.garlandview.inner.InnerAdapter;

import leaf.prod.app.R;
import leaf.prod.walletsdk.model.NewsHeader;
import leaf.prod.walletsdk.model.response.crawler.News;

public class NewsBodyAdapter extends InnerAdapter<NewsBodyItem> {

    private View view;

    private Context context;

    private final List<News> mData = new ArrayList<>();

    private NewsHeader.NewsType newsType;

    public NewsBodyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public NewsBodyItem onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.news_body_item, null);
        return new NewsBodyItem(view);
    }

    @Override
    public void onBindViewHolder(NewsBodyItem holder, int position) {
        holder.setContent(mData.get(position), newsType);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.news_body_item;
    }

    public void addData(@NonNull List<News> innerDataList, NewsHeader.NewsType newsType) {
        this.newsType = newsType;
        mData.addAll(innerDataList);
        notifyDataSetChanged();
    }

    public void clearData() {
        mData.clear();
        notifyDataSetChanged();
    }
}
