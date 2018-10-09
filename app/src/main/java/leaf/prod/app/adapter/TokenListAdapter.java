package leaf.prod.app.adapter;

import java.util.Arrays;
import java.util.List;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kyleduo.switchbutton.SwitchButton;

import leaf.prod.app.R;
import leaf.prod.walletsdk.model.response.data.Token;

/**
 *
 */
public class TokenListAdapter extends BaseQuickAdapter<Token, BaseViewHolder> {

    private List<String> choose_token;

    public TokenListAdapter(int layoutResId, @Nullable List<Token> data, List<String> choose_token) {
        super(layoutResId, data);
        this.choose_token = choose_token;
    }

    @Override
    protected void convert(BaseViewHolder helper, Token item) {
        helper.setText(R.id.wallet_title, item.getSymbol());
        helper.setText(R.id.wallet_name, item.getSource());
        if (item.getImageResId() == 0) {
            helper.setVisible(R.id.wallet_symbol, true);
            helper.setVisible(R.id.wallet_image, false);
            helper.setText(R.id.wallet_symbol, item.getSymbol());
        } else {
            helper.setVisible(R.id.wallet_symbol, false);
            helper.setVisible(R.id.wallet_image, true);
            helper.setImageResource(R.id.wallet_image, item.getImageResId());
        }
        SwitchButton aSwitch = helper.getView(R.id.s_v);
        aSwitch.setTag(item.getSymbol());
        if (Arrays.asList("ETH", "WETH", "LRC").contains(item.getSymbol())) {
            aSwitch.setVisibility(View.GONE);
        } else {
            if (choose_token.contains(item.getSymbol()) && !aSwitch.isChecked()) {
                aSwitch.setChecked(true);
            } else if (!choose_token.contains(item.getSymbol()) && aSwitch.isChecked()) {
                aSwitch.setChecked(false);
            }
            aSwitch.setVisibility(View.VISIBLE);
        }
    }

    public void setChoose_token(List<String> choose_token) {
        this.choose_token = choose_token;
    }
}
