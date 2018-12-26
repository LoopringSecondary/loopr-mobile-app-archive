package leaf.prod.app.adapter.infomation;

import java.text.SimpleDateFormat;

import android.view.View;
import android.view.ViewGroup;
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

    private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    private View innerLayout;

    @BindView(R.id.tv_time)
    public TextView tvTime;

    @BindView(R.id.tv_title)
    public TextView tvTitle;

    @BindView(R.id.tv_content)
    public TextView tvContent;

    @BindView(R.id.tv_share)
    public TextView tvShare;

    @BindView(R.id.tv_bear)
    public TextView tvBear;

    @BindView(R.id.tv_bull)
    public TextView tvBull;

    public NewsBodyItem(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        innerLayout = ((ViewGroup) itemView).getChildAt(0);
    }

    @Override
    protected View getInnerLayout() {
        return innerLayout;
    }

    void setContent(News data) {
        if (data == null)
            return;
        tvTime.setText(data.getPublishTime());
        tvTitle.setText(data.getTitle());
        tvContent.setText(data.getContent());
        //        tvShare.setText("分享" + data.getShareCount());
        //        tvBear.setText("利空" + data.getBearCount());
        //        tvBull.setText("利好" + data.getBullCount());
    }
}
