package leaf.prod.app.adapter.infomation;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramotion.garlandview.inner.InnerItem;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.walletsdk.manager.TokenDataManager;
import leaf.prod.walletsdk.model.NewsHeader;
import leaf.prod.walletsdk.model.response.crawler.News;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-12-22 4:25 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public class NewsBodyItem extends InnerItem {

    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private static SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd HH:mm");

    private TokenDataManager tokenDataManager;

    private View innerLayout;

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

    @BindView(R.id.tv_token)
    public TextView tvToken;

    @BindView(R.id.iv_token)
    public ImageView ivToken;

    private boolean expand = false;

    public NewsBodyItem(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        innerLayout = ((ViewGroup) itemView).getChildAt(0);
        tokenDataManager = TokenDataManager.getInstance(innerLayout.getContext());
        clContent.setExpandInterpolator(new OvershootInterpolator());
        clContent.setCollapseInterpolator(new OvershootInterpolator());
        clContent.setOnClickListener(view -> {
            if (!expand && !clContent.isExpanded() || expand && clContent.isExpanded()) {
                expandView(expand = !expand);
            }
        });
    }

    @Override
    protected View getInnerLayout() {
        return innerLayout;
    }

    @SuppressLint("SetTextI18n")
    public void setContent(News data, NewsHeader.NewsType newsType) {
        if (data == null)
            return;
        try {
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
//            if (newsType == NewsHeader.NewsType.NEWS_INFO) {
//                LyqbLogger.log(data.getToken());
//                Token token = tokenDataManager.getTokenBySymbol(data.getToken());
//                if (token != null) {
//                    ivToken.setImageResource(token.getImageResId());
//                    ivToken.setVisibility(View.VISIBLE);
//                } else {
//                    tvToken.setText(data.getToken());
//                    tvToken.setVisibility(View.VISIBLE);
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void expandView(boolean flag) {
        clContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, flag ? clContent.getTextSize() + 6 : clContent.getTextSize() - 6);
        clContent.toggle();
        ViewGroup.LayoutParams lp = innerLayout.getLayoutParams();
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        innerLayout.setLayoutParams(lp);
    }
}
