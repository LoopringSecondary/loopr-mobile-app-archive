package leaf.prod.app.adapter.infomation;

import java.text.SimpleDateFormat;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.ramotion.garlandview.inner.InnerItem;

import at.blogc.android.views.ExpandableTextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
import leaf.prod.walletsdk.manager.TokenDataManager;
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

    public void setContent(News data) {
        if (data == null)
            return;
        try {
            tvTime.setText(sdf2.format(sdf1.parse(data.getPublishTime())));
            tvSource.setText("来源:" + data.getSource());
            tvTitle.setText(data.getTitle());
            clContent.setText(data.getContent());
            tvShare.setText("分享" + (data.getForwardNum() > 0 ? data.getForwardNum() : ""));
            tvBear.setText("利空" + (data.getBearIndex() > 0 ? data.getBearIndex() : ""));
            tvBull.setText("利好" + (data.getBullIndex() > 0 ? data.getBullIndex() : ""));
        } catch (Exception e) {
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
