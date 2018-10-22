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

public class TokenListAdapter extends BaseQuickAdapter<Token, BaseViewHolder> {

    private List<String> chooseToken;

    public TokenListAdapter(int layoutResId, @Nullable List<Token> data, List<String> chooseToken) {
        super(layoutResId, data);
        this.chooseToken = chooseToken;
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
            if (chooseToken.contains(item.getSymbol()) && !aSwitch.isChecked()) {
                aSwitch.setCheckedImmediately(true);
            } else if (!chooseToken.contains(item.getSymbol()) && aSwitch.isChecked()) {
                aSwitch.setCheckedImmediately(false);
            }
            aSwitch.setVisibility(View.VISIBLE);
        }
    }

    public void setChooseToken(List<String> chooseToken) {
        String[] array = chooseToken.toArray(new String[0]);
        Arrays.sort(array, 3, array.length);
        this.chooseToken = Arrays.asList(array);
    }
}
