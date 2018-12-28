package leaf.prod.app.adapter.infomation;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import leaf.prod.app.R;
import leaf.prod.walletsdk.model.response.crawler.News;
import leaf.prod.walletsdk.service.CrawlerService;

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

    @BindView(R.id.tv_comment)
    public TextView tvComment;

    @BindView(R.id.tv_source)
    public TextView tvSource;

    private CrawlerService crawlerService = new CrawlerService();

    public NewsInfoItem(View itemView) {
        super(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setContent(News data) {
        if (data == null)
            return;
        try {
            tvTime.setText(sdf2.format(sdf1.parse(data.getPublishTime())));
            tvSource.setText(innerLayout.getResources().getString(R.string.news_source) + ":" + data.getSource());
            tvTitle.setText(data.getTitle());
            clContent.setText(data.getContent());
            tvShare.setText(innerLayout.getResources().getString(R.string.news_share) + (data.getForwardNum() > 0 ? data
                    .getForwardNum() : ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
