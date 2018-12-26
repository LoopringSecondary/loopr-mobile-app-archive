package leaf.prod.app.adapter.infomation;

import java.text.SimpleDateFormat;

import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ramotion.garlandview.inner.InnerItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import leaf.prod.app.R;
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

    private View innerLayout;

    @BindView(R.id.tv_time)
    public TextView tvTime;

    @BindView(R.id.tv_title)
    public TextView tvTitle;

    @BindView(R.id.cl_content)
    public ConstraintLayout clContent;

    @BindView(R.id.tv_content)
    public TextView tvContent;

    @BindView(R.id.tv_content_total)
    public TextView tvContentTotal;

    @BindView(R.id.tv_share)
    public TextView tvShare;

    @BindView(R.id.tv_bear)
    public TextView tvBear;

    @BindView(R.id.tv_bull)
    public TextView tvBull;

    @BindView(R.id.tv_source)
    public TextView tvSource;

    @BindView(R.id.iv_token)
    public ImageView ivToken;

    public NewsBodyItem(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        innerLayout = ((ViewGroup) itemView).getChildAt(0);
        clContent.setOnClickListener(view -> {
            if (tvContent.getVisibility() == View.VISIBLE) {
                tvContentTotal.setVisibility(View.VISIBLE);
                tvContent.animate().alpha(0f).setDuration(500);
                tvContentTotal.animate().alpha(1f).setDuration(500);
                tvContent.setVisibility(View.GONE);
            } else {
                tvContent.setVisibility(View.VISIBLE);
                tvContentTotal.animate().alpha(0f).setDuration(500);
                tvContent.animate().alpha(1f).setDuration(500);
                tvContentTotal.setVisibility(View.GONE);
            }
            ViewGroup.LayoutParams lp = clContent.getLayoutParams();
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            clContent.setLayoutParams(lp);
            lp = innerLayout.getLayoutParams();
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            innerLayout.setLayoutParams(lp);
        });
    }

    @Override
    protected View getInnerLayout() {
        return innerLayout;
    }

    void setContent(News data) {
        if (data == null)
            return;
        try {
            tvTime.setText(sdf2.format(sdf1.parse(data.getPublishTime())));
            tvSource.setText("来源:" + data.getSource());
            tvTitle.setText(data.getTitle());
            tvContent.setText(data.getContent());
            tvContentTotal.setText(data.getContent());
            //        tvShare.setText("分享" + data.getShareCount());
            //        tvBear.setText("利空" + data.getBearCount());
            //        tvBull.setText("利好" + data.getBullCount());
        } catch (Exception e) {
        }
    }
}
