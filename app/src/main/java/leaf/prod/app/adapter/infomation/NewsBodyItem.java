package leaf.prod.app.adapter.infomation;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.ramotion.garlandview.inner.InnerItem;

import butterknife.ButterKnife;
import leaf.prod.walletsdk.model.response.crawler.News;

/**
 * Created with IntelliJ IDEA.
 * User: laiyanyan
 * Time: 2018-12-28 5:21 PM
 * Cooperation: loopring.org 路印协议基金会
 */
public class NewsBodyItem extends InnerItem {

    protected Activity activity;

    protected View innerLayout;

    public NewsBodyItem(View itemView, Activity activity) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.innerLayout = ((ViewGroup) itemView).getChildAt(0);
        this.activity = activity;
    }

    @Override
    protected View getInnerLayout() {
        return innerLayout;
    }

    protected void setContent(News data) {
    }
}
