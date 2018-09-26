/**
 * Created with IntelliJ IDEA.
 * User: kenshin wangchen@loopring.org
 * Time: 2018-09-18 上午11:25
 * Cooperation: loopring.org 路印协议基金会
 */
package leaf.prod.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import leaf.prod.app.R;

import butterknife.ButterKnife;

public class TokenView extends RelativeLayout {

    public TokenView(Context context) {
        super(context);
    }

    public TokenView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TokenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.title_view, this, true);
        ButterKnife.bind(this);
    }
}
